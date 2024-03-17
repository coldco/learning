package edu.hitwh.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import edu.hitwh.exception.DeleteDefaultGroupException;
import edu.hitwh.exception.GroupDuplicateException;
import edu.hitwh.mapper.FriendsMapper;
import edu.hitwh.model.*;
import edu.hitwh.response.*;
import edu.hitwh.request.FriendQueryRequest;
import edu.hitwh.request.PageQueryRequest;
import edu.hitwh.service.FriendService;
import edu.hitwh.service.UserService;
import edu.hitwh.utils.JWTUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl extends ServiceImpl<FriendsMapper, Friends> implements FriendService {
    private final FriendsMapper friendsMapper;
    private final UserService userService;


    /**
     * 保存好友请求
     *
     * @param currentJwt 当前用户uid
     * @param uid        接受请求的用户uid
     */
    @Override
    public void saveFriendRequest(Integer currentJwt, Integer uid) {
        //判断2人是否发送过好友申请
        if (!friendsMapper.selectRequest(currentJwt, uid)) {
            FriendRequest friendRequest = new FriendRequest(uid, currentJwt, (short) 0, LocalDateTime.now(), null);
            friendsMapper.insertFriendRequest(friendRequest);
        } else {
            throw new RuntimeException("已发送过好友请求");
        }
    }


    /**
     * 查看自己收到的好友请求
     *
     * @param pageQueryRequest 分页条件
     * @param currentJwt       当前用户uid
     * @return 分页结果
     */
    @Override
    public FriendRequestPageResponse getFriendRequests(PageQueryRequest pageQueryRequest, Integer currentJwt) {
        //1.配置分页条件
        PageHelper.startPage((int) pageQueryRequest.getPageNo(), (int) pageQueryRequest.getPageSize());
        //2.执行分页查询条件
        List<FriendRequest> friendRequestRespons = friendsMapper.selectFriendRequests(currentJwt);
        Page<FriendRequest> friendRequestPage = (Page<FriendRequest>) friendRequestRespons;
        //封装pageBean对象
        FriendRequestPageResponse pageBean = new FriendRequestPageResponse();
        pageBean.setResult(friendRequestPage.getResult());
        pageBean.setTotal(friendRequestPage.getTotal());
        return pageBean;
    }

    /**
     * 同意好友请求,在好友表friends中插入好友关系
     *
     * @param uid        对方uid
     * @param currentJwt 当前用户uid
     */
    @Transactional
    @Override
    public void agreeFriendRequest(Integer uid, Integer currentJwt) {
        //更新friend_request表中state
        friendsMapper.updateFriendRequestState(uid, currentJwt);
        //将要建立好友关系的人的信息传入实体类中
        Friends newFriend = new Friends();
        newFriend.setFriendUid(uid);
        newFriend.setUserUid(currentJwt);
        //得到好友的用户名作为默认好友备注
        newFriend.setRemark(userService.selectUsernameByUid(uid));
        //将好友默认分组为“我的好友”
        //获取“我的好友”的主键id
        int defaultGroupId = friendsMapper.selectDefaultGroupId(currentJwt);
        //设置默认分组
        newFriend.setGroupId(defaultGroupId);
        //新增好友关系
        save(newFriend);
        //以friendUid作为UserUid新增一个好友关系(在执行查找好友列表时只需要以user_uid作为当前用户uid),与上面逻辑一模一样
        Friends newFriend2 = new Friends();
        newFriend2.setFriendUid(currentJwt);
        newFriend2.setRemark(userService.selectUsernameByUid(currentJwt));
        int defaultGroupId2 = friendsMapper.selectDefaultGroupId(uid);
        newFriend2.setGroupId(defaultGroupId2);
        newFriend2.setUserUid(uid);
        save(newFriend2);
    }

    /**
     * 发送好友关系绑定申请
     *
     * @param uid        对方uid
     * @param currentJwt 当前用户uid
     * @param type       好友关系类型，1；基友，2：闺蜜，3：情侣
     */
    @Override
    public void saveFriendRelationRequest(Integer uid, Integer currentJwt, Short type) {
        //判断是否发送过好友关系绑定请求
        if (!friendsMapper.selectRelationRequest(uid, currentJwt)) {
            Relation relation = new Relation(uid, currentJwt, type, (short) 0, null);
            friendsMapper.insertRelationRequest(relation);
        } else {
            throw new RuntimeException("已发送过好友关系绑定请求");
        }
    }


    /**
     * 查看自己收到的好友关系绑定申请
     *
     * @param currentJwt 当前用户uid
     * @return 查询结果
     */
    @Override
    public List<Relation> getRelationRequests(Integer currentJwt) {
        return friendsMapper.selectRelationRequests(currentJwt);
    }

    /**
     * 同意好友关系绑定，在friends表中更新好友关系类型
     *
     * @param uid        对方uid
     * @param currentJwt 当前uid
     * @param type       好友关系类型（用于更新friends字段，可以遍历好友关系请求表查询，但我觉得前端在查询好友关系申请表的时候可以返回）
     */
    @Override
    public void agreeRelationRequest(Integer uid, Integer currentJwt, Short type) {
        //更新friend_relationship表中state为1
        friendsMapper.updateRelationStateTo(uid, currentJwt, (short) 1);
        //获取关系类型
        String typeName = switch (type) {
            case 1 -> "基友";
            case 2 -> "闺蜜";
            case 3 -> "情侣";
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
        //更新字段
        lambdaUpdate()
                .setSql("relation = " + "'" + typeName + "'")
                .and(wp -> wp.eq(Friends::getFriendUid, uid).eq(Friends::getUserUid, currentJwt))
                .or(wp -> wp.eq(Friends::getFriendUid, currentJwt).eq(Friends::getUserUid, uid))
                .update();
    }

    /**
     * 取消好友关系绑定； 更新好友关系绑定请求表中的状态，将好友关系表中的好友关系设为空
     *
     * @param uid        对方uid
     * @param currentJwt 当前uid
     */
    @Override
    public void cancelRelation(Integer uid, Integer currentJwt) {
        //1.更新好友关系绑定请求表中的状态
        friendsMapper.updateRelationStateTo(uid, currentJwt, (short) 0);
        //2.将好友关系表中的好友关系设为空
        lambdaUpdate()
                .setSql("set relation = null ")
                .and(wp -> wp.eq(Friends::getFriendUid, uid).eq(Friends::getUserUid, currentJwt))
                .or(wp -> wp.eq(Friends::getFriendUid, currentJwt).eq(Friends::getUserUid, uid))
                .update();
    }

    /**
     * 修改好友备注时回显
     * @param uid 对方uid
     * @param currentJwt 当前uid
     * @return 备注
     */
    @Override
    public String getRemark(Integer uid, Integer currentJwt) {
        LambdaQueryWrapper<Friends> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Friends::getRemark)
                .eq(Friends::getUserUid, currentJwt)
                .eq(Friends::getFriendUid, uid);
        return getOne(wrapper).getRemark();
    }

    /**
     * 修改好友备注
     * @param currentJwt 当前uid
     * @param uid 对方uid
     * @param remark 修改后的备注值
     */
    @Override
    public void updateRemark(Integer currentJwt, Integer uid, String remark) {
        lambdaUpdate().setSql(remark != null, "remark =" + remark)
                .eq(Friends::getUserUid, currentJwt)
                .eq(Friends::getFriendUid, uid)
                .update();
    }


    /**
     * 查看分组
     * @param currentJwt 当前uid
     * @return 所有分组
     */
    @Override
    public List<Group> getGroup(Integer currentJwt) {
        return friendsMapper.selectGroup(currentJwt);
    }

    /**
     * 新增分组
     * @param group 新增组名
     * @param currentJwt 当前uid
     */
    @Override
    public void saveGroup(String group, Integer currentJwt) {
        //获取已有的分组，检查组名是否重复
        for (Group group1 : friendsMapper.selectGroup(currentJwt)) {
            if (group1.getGroup().equals(group)) {
                throw new GroupDuplicateException("组名重复");
            }
        }
        //组名未重复，新增分组
        friendsMapper.insertGroup(currentJwt, group);
    }

    /**
     * 删除分组
     * @param groupId 分组对应的主键id（由前端分组页面返回）
     */
    @Override
    public void deleteGroup(Integer groupId) {
        //判断groupId对应组名是否为默认分组名
        int defaultGroupId = friendsMapper.selectDefaultGroupId(JWTUtils.currentJwt);
        if (groupId == defaultGroupId) {
            throw new DeleteDefaultGroupException("禁止删除默认组");
        }
        //groupId对应组名非默认组名
        //删除friend_groups表中的相应数据
        friendsMapper.deleteGroup(groupId);
        //更新friends中的分组字段为默认分组
        lambdaUpdate().setSql("group_id =" + defaultGroupId)
                .eq(Friends::getGroupId, groupId);
    }


    /**
     * 屏蔽/取消屏蔽好友
     * @param uid 对方uid
     * @param currentJwt 当前uid
     * @param state black字段修改值，0：取消屏蔽，1：屏蔽
     */
    @Override
    public void blackFriend(Integer uid, Integer currentJwt, Short state) {
        lambdaUpdate().set(Friends::getBlacked,state)
                .eq(Friends::getFriendUid, uid)
                .eq(Friends::getUserUid, currentJwt)
                .update();
    }

    /**
     * 添加好友到组
     * @param groupId 组的主键id，添加好友时发送查看分组的请求，后端返回当前用户所有分组数据，前端传回id
     * @param uid 对方uid
     * @param currentJwt 当前uid
     */
    @Override
    public void addFriendToGroup(Integer groupId, Integer uid, Integer currentJwt) {
        lambdaUpdate()
                .set(Friends::getGroupId,groupId)
                .eq(Friends::getFriendUid, uid)
                .eq(Friends::getUserUid, currentJwt)
                .update();
    }


    /**
     * 分页模糊查询好友列表
     * @param friendQuery 查询条件
     * @param currentJwt 当前uid
     * @return 查询结果
     */
    @Override
    public FriendPageResponse getFriendPage(FriendQueryRequest friendQuery, Integer currentJwt) {
        //获得当前用户uid
        friendQuery.setUserUid(currentJwt);
        //1.设置分页条件
        PageHelper.startPage((int) friendQuery.getPageNo(), (int) friendQuery.getPageSize());
        //2.执行查询
        List<FriendResponse> friendsList = friendsMapper.selectFriends(friendQuery);
        //3.将查询结果转为分页查询结果
        Page<FriendResponse> friendsPage = (Page<FriendResponse>) friendsList;
        //3.封装pageBean
        FriendPageResponse pageBean = new FriendPageResponse();
        pageBean.setTotal(friendsPage.getTotal());
        pageBean.setResult(friendsPage.getResult());
        return pageBean;
    }

    /**
     * 查看好友精确信息
     *
     * @param uid 好友uid
     * @return 查询结果
     */
    @Override
    public UserResponse getFriendOne(Integer uid) {
        //查询好友即查询好友的账号和用户名，这些都存储在tb_user表中，故直接从该表查询即可
        User one = userService.lambdaQuery()
                .select(User::getUid, User::getUsername)
                .eq(User::getUid, uid)
                .one();
        return BeanUtil.copyProperties(one,UserResponse.class);
    }

    /**
     * 批量删除好友
     * @param uids 好友们的uid
     * @param currentJwt 当前uid
     */
    @Override
    @Transactional
    public void deleteFriend(List<Integer> uids, Integer currentJwt) {
        //将friends表中的deleted修改为1进行假删除
        lambdaUpdate()
                .setSql("deleted = 1")
                .eq(Friends::getUserUid, currentJwt)
                .in(Friends::getFriendUid, uids)
                .update();
        //删除好友发送过的好友申请
        friendsMapper.deleteFriendRequest(uids, currentJwt);
        //删除好友发送过的好友关系绑定申请
        friendsMapper.deleteRelationRequest(uids, currentJwt);
    }

}
