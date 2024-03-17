package edu.hitwh.manageTask;

import edu.hitwh.model.Cron;
import edu.hitwh.service.CronService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class CronManageTask {
    @Lazy
    private final CronService cronService;
    //定义每半个小时扫描一次
    @Scheduled(cron = "0 0/30 * * * ?")
    public void cronManage() {
        log.info("定时任务启动");
        List<Cron> list = cronService.list();
        list.forEach(cron -> {
            if (LocalDate.now().isAfter(cron.getDeadTime())) {
                cronService.stopCron(cron);
                cronService.removeById(cron.getId());
                log.info("任务存在，id:{},emailId:{},executeTime:{}",cron.getId(),cron.getEmailId(),cron.getExecuteTime());
            } else {
                log.info("任务存在，id:{},emailId:{},executeTime:{}",cron.getId(),cron.getEmailId(),cron.getExecuteTime());
                cronService.startCron(cron);
            }
        });
    }
}

