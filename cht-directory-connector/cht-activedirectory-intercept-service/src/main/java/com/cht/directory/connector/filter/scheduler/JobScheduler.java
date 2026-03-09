package com.cht.directory.connector.filter.scheduler;

import com.cht.directory.connector.filter.service.PublishPwdService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Map;

@Component
public class JobScheduler {

    private static final Logger log = LoggerFactory.getLogger(PublishPwdService.class);

    PublishPwdService publishPwdService;

    public JobScheduler(PublishPwdService publishPwdService) {
        this.publishPwdService = publishPwdService;
    }

    // 每10分檢查一次 h2 失敗的密碼變更
    @Scheduled(cron = "0 0/10 * * * ?")
    public void timerCron() {
        // 定期從h2取出失敗的密碼變更重試
        publishPwdService.retry();
    }
}
