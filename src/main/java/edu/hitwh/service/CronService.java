package edu.hitwh.service;


import com.baomidou.mybatisplus.extension.service.IService;
import edu.hitwh.model.Cron;

public interface CronService extends IService<Cron> {
    void startCron(Cron cron);

    void stopCron(Cron cron);

    void changeCron(Cron cron);
}
