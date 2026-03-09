package com.cht.directory.connector.sync;

import com.cht.directory.connector.sync.service.*;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.cht.directory.connector.sync.config.ServiceParameters;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@ComponentScan(basePackages = { "com.cht.directory.connector" })
@EntityScan("com.cht.directory.domain")
@EnableJpaRepositories(basePackages = { "com.cht.directory.repository" })
@Slf4j
public class ActivedirectorySyncApp implements CommandLineRunner {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private ServiceParameters serviceParameters;

    public static void main(String[] args) {

        SpringApplication.run(ActivedirectorySyncApp.class, args).close();
    }

    @Override
    public void run(String... args) throws Exception {

        serviceParameters.parse(args);

        String jobName = serviceParameters.getName().trim();

        // scan
        if (jobName.equalsIgnoreCase("ADWS201X")) {
            ActivedirectoryScanService activedirectoryScanService = context.getBean(ActivedirectoryScanService.class);

            try {

                activedirectoryScanService.doScanDirectory();
            } catch (Exception ex) {

                log.error("{}", ExceptionUtils.getStackTrace(ex));
            }
        // sync
        } else if (jobName.equalsIgnoreCase("ADWS202X")) {
            ActivedirectorySyncService activedirectorySyncService = context.getBean(ActivedirectorySyncService.class);

            try {

                activedirectorySyncService.executeSync();
            } catch (Exception ex) {

                log.error("{}", ExceptionUtils.getStackTrace(ex));
            }
        // pwd
        } else if (jobName.equalsIgnoreCase("ADWS203X")) {
            ActivedirectoryPwdService activedirectoryPwdService = context.getBean(ActivedirectoryPwdService.class);

            try {

                activedirectoryPwdService.savePassWordChange();
            } catch (Exception ex) {

                log.error("{}", ExceptionUtils.getStackTrace(ex));
            }
        // clean
        } else if (jobName.equalsIgnoreCase("ADWS204X")) {
            ActivedirectoryCleanService activedirectoryCleanService = context.getBean(ActivedirectoryCleanService.class);

            try {

                activedirectoryCleanService.executeClean();
            } catch (Exception ex) {

                log.error("{}", ExceptionUtils.getStackTrace(ex));
            }
        // ad transfer
        } else if (jobName.equalsIgnoreCase("ADWS205X")) {
            ActiveDirectoryTransferService activedirectoryTransferService = context.getBean(ActiveDirectoryTransferService.class);

            try {

                activedirectoryTransferService.executeTransfer();
            } catch (Exception ex) {

                log.error("{}", ExceptionUtils.getStackTrace(ex));
            }
        // edir transfer
        } else if (jobName.equalsIgnoreCase("ADWS206X")) {
            EdirectoryTransferService edirectoryTransferService = context.getBean(EdirectoryTransferService.class);

            try {

                edirectoryTransferService.doScanDirectory();
            } catch (Exception ex) {

                log.error("{}", ExceptionUtils.getStackTrace(ex));
            }
        // correction person memberof
        } else if (jobName.equalsIgnoreCase("ADWS901X")) {
            PersonMemberOfCorrectionService personMemberOfCorrectionService = context.getBean(PersonMemberOfCorrectionService.class);

            try {

                personMemberOfCorrectionService.executeCorrection();
            } catch (Exception ex) {

                log.error("{}", ExceptionUtils.getStackTrace(ex));
            }
        // correction group memberof
        } else if (jobName.equalsIgnoreCase("ADWS902X")) {
            GroupMemberOfCorrectionService groupMemberOfCorrectionService = context.getBean(GroupMemberOfCorrectionService.class);

            try {

                groupMemberOfCorrectionService.executeCorrection();
            } catch (Exception ex) {

                log.error("{}", ExceptionUtils.getStackTrace(ex));
            }
        //  test
        } else if (jobName.equalsIgnoreCase("TEST")) {
            ActivedirectorySyncService activedirectorySyncService = context.getBean(ActivedirectorySyncService.class);

            try {

                activedirectorySyncService.addOrganizationPerson();
            } catch (Exception ex) {

                log.error("{}", ExceptionUtils.getStackTrace(ex));
            }
        }

    }
}
