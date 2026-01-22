package team.weilai.studythrough.controller;

import com.alibaba.excel.EasyExcel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import team.weilai.studythrough.pojo.exam.main.Repo;

import java.io.File;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author gwj
 * @date 2025/2/20 15:04
 */
@AllArgsConstructor
@NoArgsConstructor
public class ExportTask<T> implements Runnable{
    private List<T> data;
    private CountDownLatch countDownLatch;
    private File file;

    @Override
    public void run() {
        EasyExcel.write(file, Repo.class)
                .sheet("仓库")
                .doWrite(data);
        countDownLatch.countDown();
    }
}
