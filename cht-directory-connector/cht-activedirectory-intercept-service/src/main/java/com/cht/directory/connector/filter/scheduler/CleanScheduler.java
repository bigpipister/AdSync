package com.cht.directory.connector.filter.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class CleanScheduler {

    private static final Logger log = LoggerFactory.getLogger(CleanScheduler.class);

    // 每天凌晨2點刪除 password filter DLL 暫存的密碼變更記錄
    @Scheduled(cron = "0 0 2 * * ?")
    public void timerCron() {
        File file = new File("C:/AdPwdSync/temp.db");
        deleteFile(file);
        log.info("delete temp db file");
    }

    public void deleteFile(File file) {
        if(file.exists()) {//判斷路徑是否存在
            if(file.isFile()){//boolean isFile():測試此抽象路徑名錶示的檔案是否是一個標準檔案。
                file.delete();
            } else{//不是檔案，對於資料夾的操作
                //儲存 路徑D:/1/新建資料夾2  下的所有的檔案和資料夾到listFiles陣列中
                File[] listFiles = file.listFiles();//listFiles方法：返回file路徑下所有檔案和資料夾的絕對路徑
                for (File file2 : listFiles) {
                    /*
                     * 遞迴作用：由外到內先一層一層刪除裡面的檔案 再從最內層 反過來刪除資料夾
                     *    注意：此時的資料夾在上一步的操作之後，裡面的檔案內容已全部刪除
                     *         所以每一層的資料夾都是空的  ==》最後就可以直接刪除了
                     */
                    deleteFile(file2);
                }
            }
            file.delete();
        }
    }

}
