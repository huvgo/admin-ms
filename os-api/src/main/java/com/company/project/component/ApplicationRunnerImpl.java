package com.company.project.component;


import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import com.company.project.modules.engine.controller.OshiServer;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 应用启动后执行
 **/
@Component
public class ApplicationRunnerImpl implements ApplicationRunner {

    private final OshiServer oshiServer;

    public ApplicationRunnerImpl(OshiServer oshiServer) {
        this.oshiServer = oshiServer;
    }

    @Override
    public void run(ApplicationArguments args) {

        CronUtil.schedule("*/2 * * * * *", new Task() {
            @Override
            public void execute() {
                oshiServer.sendNotice();
            }
        });
        CronUtil.setMatchSecond(true);
        CronUtil.start();
    }
}