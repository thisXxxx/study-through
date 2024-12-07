package studythrough.quartz;

import lombok.Data;
import org.quartz.*;

import java.util.Date;

/**
 * @author gwj
 * @date 2024/11/20 19:31
 */
@Data
//@DisallowConcurrentExecution //这个注解意义在于不让任务并发的执行，只有前一个任务执行完了才会执行下一个
@PersistJobDataAfterExecution //将JobDetail中的jobDataMap持久化，
// 因为Scheduler每次执行都会根据JobDetail创建一个新的Job实例，jobDataMap属于JobDetail，那么每次也是一个新的
public class MyJob implements Job {
    private String name;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        JobDataMap mergeMap = jobExecutionContext.getMergedJobDataMap();
        System.out.println(mergeMap.get("tong"));

        System.out.println("hello job! "+new Date());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("hello job! "+new Date());
    }
}
