package edu.hitwh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.hitwh.mapper.CronMapper;
import edu.hitwh.model.Cron;
import edu.hitwh.model.Email;
import edu.hitwh.service.CronService;
import edu.hitwh.service.MailboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class CronServiceImpl extends ServiceImpl<CronMapper, Cron> implements CronService {
    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;
    private final MailboxService mailboxService;

    private final Map<Integer, ScheduledFuture<?>> futureMap = new HashMap<>();

/*    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        return new ThreadPoolTaskScheduler();
    }*/

    /**
     * 启动定时任务
     * @param cron
     */
    @Override
    public void startCron(Cron cron) {
        //1.判断cron是否被执行过
        if(futureMap.containsKey(cron.getId())){
            log.info("任务存在，id:{},emailId:{},executeTime:{}",cron.getId(),cron.getEmailId(),cron.getExecuteTime());
            return;
        }
        //2.判断是否还没过执行时间
        //在springTask中，cron表达式无法对年进行定时，故使用startTime和deadTime来限制定时任务要执行的年份
        if(LocalDate.now().isEqual(cron.getStartTime()) || LocalDate.now().isEqual(cron.getDeadTime()) ||
                (LocalDate.now().isAfter(cron.getStartTime()) && LocalDate.now().isBefore(cron.getDeadTime()))){
            //提取执行时间
            LocalDateTime executeTime = cron.getExecuteTime();
            //组装cron表达式
            DateTimeFormatter cronFormatter = DateTimeFormatter.ofPattern("s m H d M");
            String cronExp = cronFormatter.format(executeTime)+" ?";
            //执行scheduled任务
            ScheduledFuture<?> future = threadPoolTaskScheduler.schedule(new TaskRunnable(cron), new CronTrigger(cronExp));
            //将future传入futureMap集合表示任务启动，避免任务重复启动
            futureMap.put(cron.getId(),future);
            //输出日志
            log.info("任务启动，id:{},emailId:{},executeTime:{}",cron.getId(),cron.getEmailId(),cron.getExecuteTime());
        }
    }

    /**
     * 停止定时任务
     * @param cron
     */
    @Override
    public void stopCron(Cron cron) {
        ScheduledFuture<?> future = futureMap.get(cron.getId());
        if (future != null) {
            future.cancel(true);
            futureMap.remove(cron.getId());
            log.info("任务存在，id:{},emailId:{},executeTime:{}",cron.getId(),cron.getEmailId(),cron.getExecuteTime());
        }
    }

    /**
     * 修改定时任务
     * @param cron
     */
    @Override
    public void changeCron(Cron cron) {
        stopCron(cron);// 先停止，在开启.
        startCron(cron);
    }
    private class TaskRunnable implements Runnable{
        private final Cron cron;

        public TaskRunnable(Cron cron) {
            this.cron = cron;
        }

        @Override
        public void run() {
            //定义任务要做的事，即把visibility字段设为0表示可见，
            // 同时把时间设为设定的发送时间
            // (如果设为当前时间由于定时任务管理器CronManageTask扫面时间间隔问题会导致实际执行时间与预期发送时间不一致)
            mailboxService.lambdaUpdate()
                    .set(Email::getVisibility,(short)0)
                    .set(Email::getSendTime,cron.getExecuteTime())
                    .eq(Email::getId,cron.getEmailId())
                    .update();

        }
    }
}
