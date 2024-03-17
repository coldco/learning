package edu.hitwh.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.hitwh.exception.GroupDuplicateException;
import edu.hitwh.exception.UsernameDuplicateException;
import edu.hitwh.mapper.FriendsMapper;
import edu.hitwh.mapper.UserMapper;
import edu.hitwh.model.User;
import edu.hitwh.request.UserEnrollRequest;
import edu.hitwh.response.UserPageResponse;
import edu.hitwh.request.UserQueryRequest;
import edu.hitwh.response.UserResponse;
import edu.hitwh.service.UserService;
import edu.hitwh.utils.JWTUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserMapper userMapper;

    private final FriendsMapper friendsMapper;

    /**
     * md5加密
     * @param password 密码
     * @return 加密结果
     */
    @Override
    public String md5Encrypt(String password){
        return DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 生成uid
     * @return 返回uid
     */
    @Override
    public int uidGenerate() {
        long num = lambdaQuery().count();
        long uid = num + 2023;
        return (int) uid;
    }

    /**
     * 分页模糊查找用户
     * @param userQuery 查找条件
     * @return 分页查询结果
     */
    @Override
    public UserPageResponse select(UserQueryRequest userQuery) {
        //1.构建分页条件
        Page<User> page = new Page<>(userQuery.getPageNo(), userQuery.getPageSize());
        page.addOrder(new OrderItem("uid", true));
        //2.构建查询条件
        Page<User> page1 = lambdaQuery()
                .select(User::getUsername,User::getUid)
                .like(userQuery.getUsername() != null, User::getUsername, userQuery.getUsername())
                .eq(userQuery.getUid() != null, User::getUid, userQuery.getUid())
                .page(page);
        //解析查询结果
        UserPageResponse pageBean = new UserPageResponse();
        pageBean.setTotal(page1.getTotal());
        List<UserResponse> userResponses = BeanUtil.copyToList(page1.getRecords(), UserResponse.class);
        pageBean.setResult(userResponses);
        return pageBean;
    }

    /**
     * 新增用户
     * @param user 用户信息：用户名，密码
     * @return 生成的uid
     */
    @Transactional
    @Override
    public int saveNewUser(UserEnrollRequest user) {
        User newUser = new User();
        newUser = BeanUtil.copyProperties(user,User.class);
        //加密密码
        newUser.setPassword(md5Encrypt(newUser.getPassword()));
        try {
            //生成账号
            int uid = uidGenerate();
            newUser.setUid(uid);
            //新增用户
            save(newUser);
            //创建默认分组“我的好友”
            friendsMapper.insertGroup(uid, "我的好友");
            return uid;
        } catch (Exception e) {
            throw new UsernameDuplicateException("用户名重复");
        }

    }

    /**
     * 根据uid返回用户名
     * @param uid uid
     * @return 用户名
     */
    @Override
    public String selectUsernameByUid(Integer uid) {
        QueryWrapper<User> query = new QueryWrapper<User>()
                .select("username")
                .eq("uid", uid);
        User user = userMapper.selectOne(query);
        return user.getUsername();
    }

    /**
     * 根据用户名返回uid
     * @param username 用户名
     * @return uid
     */
    @Override
    public Integer selectUidByUsername(String username) {
        QueryWrapper<User> query = new QueryWrapper<User>()
                .select("uid")
                .eq("username", username);
        User user = userMapper.selectOne(query);
        return user.getUid();
    }

    /**
     * 屏蔽用户/取消屏蔽用户
     * @param currentJwt 当前用户uid
     * @param uid 要操作的对象uid
     * @param flag true为屏蔽，false为取消屏蔽
     */
    @Override
    public void blackUser(Integer currentJwt, Integer uid, boolean flag) {
        if (flag) {
            userMapper.insertBlackObject(currentJwt, uid);
        }
        else{
            userMapper.deleteBlackObject(currentJwt, uid);
        }
    }


    /**
     * 判断用户是否被屏蔽
     * @param addresseeUid 收件人uid
     * @param senderUid 发件人uid
     * @return true:被屏蔽，false:未被屏蔽
     */
    @Override
    public boolean isBlacked(Integer addresseeUid,Integer senderUid) {
        return (userMapper.selectBlack(addresseeUid,senderUid)==null);
    }

    /**
     * 新建信箱分组（在好友中我将好友分组的信息存放在了一个新的表中
     * 我想尝试另一种方法，将分组信息以json字符串的形式存放在用户表中）
     * <p>
     * 以下方法均用于在user表中对分组进行操作
     * */
    //处理通用逻辑
      //更新关于分组的字段
    private void updateMailGroupColumn(Short box, List<String> groups) {
        //将groups转为json格式字符串
        String json = JSONObject.toJSONString(groups);
        switch (box) {
            case 0:
                lambdaUpdate().set(User::getInboxGroup, json).eq(User::getUid, JWTUtils.currentJwt).update();
                break;
            case 1:
                lambdaUpdate().set(User::getOutboxGroup, json).eq(User::getUid, JWTUtils.currentJwt).update();
                break;
        }
    }
      //获取分组信息并将其转换为字符串数组
        //查看分组的请求正好用这个方法，所以改成public
    @Override
    public List<String> getMailGroup(Short box) {
        //获取当前用户的信箱分组信息
        String json = switch (box) {
            case 0 -> getOne(new QueryWrapper<User>().eq("uid", JWTUtils.currentJwt)).getInboxGroup();
            case 1 -> getOne(new QueryWrapper<User>().eq("uid", JWTUtils.currentJwt)).getOutboxGroup();
            default -> null;
        };
        //将json格式字符串转换为字符串数组
        return JSONObject.parseArray(json, String.class);
    }

    /**
     * 信件分组
     * @param box 信箱,0为收件箱，1为发件箱
     * @param group 要新增的组名
     */
    @Override
    public void newMailGroup(Short box, String group) {
        List<String> groups = getMailGroup(box);
        //若groups为空，则直接插入数据
        if (groups == null) {
            groups = new ArrayList<>();
            groups.add(group);
        }
        //若json格式字符串不为空，则判断新增加的组名是否与已有的组名重复
        else if (!groups.contains(group)) {
            //不重复，则新增组
            groups.add(group);
        }
        //重复，不新增，抛出异常
        else throw new GroupDuplicateException("组名重复");
        //更新相关字段
        updateMailGroupColumn(box, groups);
    }

    /**
     * 要删除的组名
     * @param box 信箱,0为收件箱，1为发件箱
     * @param group 由前端通过回显获得的要删除的组名
     */
    @Override
    public void deleteMailGroup(Short box, String group) {
        List<String> groups = getMailGroup(box);
        groups.remove(group);
        updateMailGroupColumn(box,groups);
    }


}
