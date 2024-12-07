package studythrough.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * @author gwj
 * @date 2024/11/20 19:32
 */
public class TestJob {
    public static void main(String[] args) {


        //jobDataMap可以用于在启动这个定时器时，往任务中传递一些参数
        //任务类中可以通过获取jobDataMap再获取对应键值，
        // 也可以在任务类中定义相关字段，设置好set方法，框架会自动往里面设置值，但是如果jobdetail和trigger里设置相同名称的话，trigger会覆盖掉

        //触发器
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("trigger1","triggerGroup1")
                .usingJobData("trigger","my trigger")
                .usingJobData("name","trigger")
                .usingJobData("tong","triggde")
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(1)
                        .repeatForever())
                .build();
        JobDetail jobDetail = JobBuilder.newJob(MyJob.class)
                .withIdentity("job1","group1")
                .usingJobData("job","gwj's job")
                .usingJobData("name","jobdetail")
                .usingJobData("tong","jobde")
                .build();

        try {
            //调度器
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            //Scheduler每次执行都会根据JobDetail创建一个新的Job实例，这样就可以避免并发访问问题
            scheduler.scheduleJob(jobDetail,trigger);
            scheduler.start();
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }
}
