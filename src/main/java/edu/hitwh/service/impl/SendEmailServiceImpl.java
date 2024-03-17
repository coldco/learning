package edu.hitwh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.hitwh.exception.TitleNullException;
import edu.hitwh.mapper.UserMapper;
import edu.hitwh.model.*;
import edu.hitwh.request.SendEmailRequest;
import edu.hitwh.response.EmailSendResponse;
import edu.hitwh.response.ResultResponse;
import edu.hitwh.service.*;
import edu.hitwh.utils.JWTUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class SendEmailServiceImpl implements SendEmailService {
    private final MailboxService mailboxService;
    private final FriendService friendService;
    private final UserService userService;
    private final UserMapper userMapper;
    private final CronService cronService;

    /**
     * 发送信件
     *
     * @param sendEmailRequest:信件内容（包括发件人，收件人，标题，内容）
     * @param visibility:0表示可见，1表示不可见（用于延时信件的隐藏）
     * @return 返回提示信息
     */
    @Override
    public ResultResponse<EmailSendResponse> sendEmail(SendEmailRequest sendEmailRequest, Short visibility) {
        Email email = new Email();
        EmailSendResponse emailSendResponse = new EmailSendResponse();
        sendEmailRequest.setSenderUid(JWTUtils.currentJwt);
        //1.判断是否被对方删除（或二人是否是好友关系）
        if (!friendService.lambdaQuery()
                .select(Friends::getId)
                .eq(Friends::getUserUid, sendEmailRequest.getSenderUid())
                .eq(Friends::getFriendUid, sendEmailRequest.getAddresseeUid())
                .eq(Friends::getDeleted, 0)
                .exists()) {
            //新增信件信息
            //将sendEmailRequest中的数据拷贝到email中
            BeanUtils.copyProperties(sendEmailRequest, email);
            //设置发送时间为当前时间
            email.setSendTime(LocalDateTime.now());
            //设置visibility字段
            email.setVisibility(visibility);
            //新增字段
            try {
                mailboxService.save(email);
            } catch (Exception e) {
                throw new TitleNullException("信件标题为空");
            }
            //发送成功，但不是好友关系删除，返回提示信息
            emailSendResponse.setMessage("发送成功但你们还不是好友关系");
            emailSendResponse.setId(email.getId());
            return ResultResponse.success(emailSendResponse);

        }
        //2.判断是否被对方屏蔽
        if (userService.isBlacked(sendEmailRequest.getAddresseeUid(), sendEmailRequest.getSenderUid())) {
            //被屏蔽，无法发送信件，直接返回提示信息
            return ResultResponse.error("你被该用户屏蔽");
        }
        //3.正常发送信息
        //新增信件信息
        //将sendEmailRequest中的数据拷贝到email中
        BeanUtils.copyProperties(sendEmailRequest, email);
        //设置发送时间
        email.setSendTime(LocalDateTime.now());
        //新增字段
        try {
            mailboxService.save(email);
        } catch (Exception e) {
            throw new TitleNullException("信件标题为空");
        }
        //发送成功，返回提示信息
        emailSendResponse.setMessage("发送成功");
        emailSendResponse.setId(email.getId());
        return ResultResponse.success(emailSendResponse);
    }

    /**
     * 发送随机信件
     * @param title:标题
     * @param content：内容
     */
    @Override
    public void sendRandomEmail(String title, String content) {
        //1获取所有与当前用户不是好友关系也没有屏蔽当前用户的用户的uid
        //1.1获取所有用户uid(不包括当前用户)
        List<Integer> strangerUid = new ArrayList<>();
        List<User> allUsers = userService.list(new QueryWrapper<User>().ne("uid", JWTUtils.currentJwt));
        for (User user : allUsers) {
            strangerUid.add(user.getUid());
        }
        //1.2获取所有好友,将好友uid移除（因为要发给陌生人）
        List<Friends> allFriends = friendService.list(new QueryWrapper<Friends>().eq("user_uid", JWTUtils.currentJwt).eq("deleted", 0));
        for (Friends allFriend : allFriends) {
            strangerUid.remove(allFriend.getFriendUid());
        }
        //1.3获取所有屏蔽用户的用户的uid（别人屏蔽你，你自然无法发送信息）,移除这些uid
        List<Integer> whoBlankUser = userMapper.selectWhoBlackUser(JWTUtils.currentJwt);
        for (Integer wbu : whoBlankUser) {
            strangerUid.remove(wbu);
        }
        //得到所有符合条件的陌生人
        //2，从中随机得到一个用户uid
        //2.1，得到陌生人的数量
        int amount = strangerUid.size();
        //2.2根据size随机生成0~size-1的索引
        Random r = new Random();
        int index = r.nextInt(amount);
        //2.3获取随机uid
        Integer randomUid = strangerUid.get(index);
        //3发送随机信件
        //3.1封装sendEmailRequest
        //3.1.1修改title为title+"随机信件"。（我觉得这样更好体现陌生人信件）
        title = title + "/随机信件/";
        SendEmailRequest sendEmailRequest = new SendEmailRequest(JWTUtils.currentJwt, randomUid, title, content);
        //3.2将sendEmailRequest中的数据拷贝到email中
        Email email = new Email();
        BeanUtils.copyProperties(sendEmailRequest, email);
        //设置发送时间为当前时间
        email.setSendTime(LocalDateTime.now());
        //设置visibility字段
        email.setVisibility((short) 0);
        //新增字段(发送成功)
        try {
            mailboxService.save(email);
        } catch (Exception e) {
            throw new TitleNullException("信件标题为空");
        }
    }

    /**
     * 发送延时信件
     * @param sendTime：发送时间
     * @param sendEmailRequest：发送内容
     * @return 提示信息
     */
    @Override
    public ResultResponse<EmailSendResponse> sendScheduledEmail(LocalDateTime sendTime, SendEmailRequest sendEmailRequest){
        //发送信件
        ResultResponse<EmailSendResponse> resultResponse = sendEmail(sendEmailRequest,(short)1);
        //获得信件主键id
        EmailSendResponse emailSendResponse = (EmailSendResponse) resultResponse.getData();
        Integer id = emailSendResponse.getId();
        //解析传入的sendTime获取年限
        int startYear = sendTime.getYear();
        int deadYear = startYear+1;
        LocalDate startTime = LocalDate.of(startYear,1,1);
        LocalDate deadTime = LocalDate.of(deadYear,1,1);
        //传入cron
        Cron cron = new Cron(null,id,sendTime,startTime,deadTime);
        //更新数据库表中的字段
        cronService.save(cron);
        return resultResponse;
    }

}
