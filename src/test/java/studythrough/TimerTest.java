package studythrough;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author gwj
 * @date 2024/11/20 17:35
 */
public class TimerTest {
    public static void main(String[] args) {
        Timer timer = new Timer();
        for (int i = 0; i < 2; i++) {
            TimerTask task = new FooTimeTask("foo"+i);
            //timer.schedule(task,new Date(),2000);
            timer.scheduleAtFixedRate(task,new Date(),2000);
        }
    }
}

class FooTimeTask extends TimerTask {
    private String name;

    public FooTimeTask(String name) {
        this.name = name;
    }
    @Override
    public void run() {
        try {
            System.out.println("name: "+name+"startTime: "+new Date());
            Thread.sleep(3000);
            System.out.println("name: "+name+"endTime: "+new Date());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
