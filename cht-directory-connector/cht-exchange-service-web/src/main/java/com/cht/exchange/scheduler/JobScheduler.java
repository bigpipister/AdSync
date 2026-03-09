package com.cht.exchange.scheduler;

import com.cht.exchange.commander.ExcCommander;
import com.cht.exchange.queue.Workspace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;

@Component
@Slf4j
public class JobScheduler {

    @Autowired
    Workspace workspace;

    @Autowired
    private ExcCommander excCommander;

    // 每1分鐘檢查一次
    @Scheduled(cron = "0 * * * * *")
    public void timerCron() {
        if (workspace.getCommands().size() == 0) {
            return;
        }
        ArrayList<String> targets = (ArrayList<String>)workspace.getCommands().clone();
        workspace.cleanCommands();
        log.info("command list:" + targets.size());
        for (String item : targets) {
            log.info(item);
        }
        excCommander.runTask(targets);
    }

}
