package edu.hitwh.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.hitwh.mapper.MailboxMapper;
import edu.hitwh.model.Email;
import edu.hitwh.response.EmailPageResponse;
import edu.hitwh.request.EmailQueryRequest;
import edu.hitwh.response.EmailDetailResponse;
import edu.hitwh.response.EmailSimpleResponse;
import edu.hitwh.service.FriendService;
import edu.hitwh.service.MailboxService;
import edu.hitwh.service.UserService;
import edu.hitwh.utils.JWTUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MailboxServiceImpl extends ServiceImpl<MailboxMapper, Email> implements MailboxService {
    private final FriendService friendService;
    private final UserService userService;

    /**
     * 查看信箱
     */
    //抽出其中相同的逻辑（收件箱与发件箱逻辑相似）
    //通过泛型获取要修改的字段（是收件方还是发件方） SFunction<Email, ?> uidColumn, SFunction<Email, ?> deletedColumn
    private Page<Email> lambdaquery(SFunction<Email, ?> uidColumn, SFunction<Email, ?> deletedColumn,SFunction<Email,?> topColumn, Page<Email> page, EmailQueryRequest query) {
        return lambdaQuery()
                .select(Email::getId, Email::getTitle, Email::getSenderUid, Email::getAddresseeUid, Email::getSendTime,topColumn)
                .eq(uidColumn, query.getUid())
                .eq(deletedColumn, 0)
                .eq(Email::getVisibility, 0)
                .like(query.getTitle() != null, Email::getTitle, query.getTitle())
                .like(query.getContent() != null, Email::getContent, query.getContent())
                .page(page);
    }

    /**
     * 查看信箱
     *
     * @param currentJwt 当前uid
     * @param box        信箱；0，收件箱，1发件箱
     * @param query      查询条件（用于模糊查询信件标题/内容）
     * @return 查询结果
     */
    @Override
    public EmailPageResponse getEmail(Integer currentJwt, Short box, EmailQueryRequest query) {
        query.setUid(currentJwt);
        //设置分页条件
        Page<Email> page = new Page<>(query.getPageNo(), query.getPageSize());
        //设置排序条件,按发送时间降序排序
        //若查询条件类query中group为null,直接排序
        if (query.getGroup() == null) {
            page.addOrder(new OrderItem("send_time", false));
        } else {
            if (box == 0) {
                //若查询条件中group有分组信息，即用户在查看指定分组的信件时，将置顶信件置顶
                page.addOrder(new OrderItem("inbox_top", false), new OrderItem("send_time", false));
            } else if (box == 1) {
                page.addOrder(new OrderItem("outbox_top", false), new OrderItem("send_time", false));
            }
        }
        //设置查询条件
        Page<Email> page1;
        //如果box=0则查看的是收件箱
        if (box == 0) {
            page1 = lambdaquery(Email::getAddresseeUid, Email::getDeletedAddressee,Email::getInboxTop,page, query);
        }//如果box=1则查看的是发件箱
        else {
            page1 = lambdaquery(Email::getSenderUid, Email::getDeletedSender,Email::getOutboxTop, page, query);
        }
        //解析封装分页结果
        EmailPageResponse pageBean = new EmailPageResponse();
        pageBean.setTotal(page1.getTotal());
        pageBean.setResult(BeanUtil.copyToList(page1.getRecords(), EmailSimpleResponse.class));

        return pageBean;
    }

    /**
     * 查看信件详细信息
     *
     * @param id  信件id
     * @param box 信箱；0，收件箱，1发件箱
     * @return 信件
     */
    @Override
    @Transactional
    public EmailDetailResponse readEmail(Integer id, Short box) {
        //查找信件封装结果
        Email email = lambdaQuery()
                .select(Email::getTitle, Email::getContent, Email::getSenderUid, Email::getAddresseeUid)
                .eq(Email::getId, id)
                .eq(Email::getVisibility, 0)
                .one();
        //如果是收件箱，将read字段更新为1表示已读
        if (box == 0) {
            lambdaUpdate()
                    .set(Email::getRead, 1)
                    .eq(Email::getId, id)
                    .update();
        }

        return BeanUtil.copyProperties(email, EmailDetailResponse.class);
    }

    //处理相同逻辑
    //通过泛型传入要修改的字段 SFunction<Email, Short> column 确定是收件方还是发件方操作
    private void delete(SFunction<Email, Short> column, List<Integer> ids) {
        lambdaUpdate().set(column, 1)
                .in(Email::getId, ids)
                .update();
    }

    /**
     * 批量删除信件
     *
     * @param box 信箱；0，收件箱，1发件箱
     * @param ids 信件们主键id
     */
    @Override
    public void deleteEmail(Short box, List<Integer> ids) {
        switch (box) {
            case 0:
                delete(Email::getDeletedAddressee, ids);
                break;
            case 1:
                delete(Email::getDeletedSender, ids);
                break;
        }
    }

    /*
     * 删除分组信息
     * */
    //处理相同逻辑
    private void deleteGroupColumn(SFunction<Email, String> groupColumn, SFunction<Email, Integer> groupMakerUId, String group) {
        lambdaUpdate()
                .set(groupColumn, null)
                .eq(groupMakerUId, JWTUtils.currentJwt)
                .eq(groupColumn, group)
                .update();
    }

    /**
     * 删除分组信息
     *
     * @param box   信箱；0，收件箱，1发件箱
     * @param group 要删除的分组组名
     */
    @Override
    public void deleteGroup(Short box, String group) {
        //判断box确定操作哪个信箱
        switch (box) {
            case 0:
                deleteGroupColumn(Email::getGroupAddressee, Email::getAddresseeUid, group);
            case 1:
                deleteGroupColumn(Email::getGroupSender, Email::getSenderUid, group);
        }
    }

    /**
     * 置顶(取消置顶)信件
     *
     * @param id 信件id
     * @param i  =0；取消置顶，=1；置顶
     */
    @Override
    public void topEmail(Integer id, short i, Short box) {
        if (box == 0) {
            lambdaUpdate()
                    .set(Email::getInboxTop, i)
                    .eq(Email::getId, id)
                    .update();
        } else {
            lambdaUpdate()
                    .set(Email::getOutboxTop, i)
                    .eq(Email::getId, id)
                    .update();
        }
    }


}
