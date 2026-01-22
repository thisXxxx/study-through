package team.weilai.studythrough.controller;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.weilai.studythrough.module.exam.service.RepoService;
import team.weilai.studythrough.pojo.exam.main.Repo;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author gwj
 * @date 2025/2/20 14:12
 */
@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {

    @Autowired
    private RepoService repoService;

    @Resource(name = "threadPool")
    private ThreadPoolTaskExecutor poolTaskExecutor;


    @GetMapping
    public void multiRepo() throws InterruptedException {
        String path = "D:\\";
        long count = repoService.count();
        int pageSize = 1;
        int pageTotal = (int)count;
        log.info("导出数据总量：{}条, 预计导出文件数量：{}件",count, pageTotal);
        CountDownLatch countDownLatch = new CountDownLatch(pageTotal);
        for (int i = 1; i <= pageTotal; i++) {
            Page<Repo> page = new Page<>(i,pageSize);
            Page<Repo> res = repoService.page(page);
            List<Repo> records = res.getRecords();
            ExportTask<Repo> task = new ExportTask<>(records,
                    countDownLatch,
                    new File(path + "repo-" + i + ".xlsx"));
            poolTaskExecutor.execute(task);
        }
        countDownLatch.await();
    }

}
