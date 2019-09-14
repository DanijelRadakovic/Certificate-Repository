package megatravel.com.cerrepo.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

@Component
public class TaskManager {

    private static final Logger logger = LoggerFactory.getLogger(TaskManager.class);

    private Map<String, ScheduledFuture<?>> tasks;
    private ScheduledExecutorService executor;

    public TaskManager() {
        this.tasks = new HashMap<>();
        this.executor = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
    }

    public void addTask(Task task) {
        tasks.put(task.serialNumber, executor.schedule(task, task.delay, task.timeUnit));
        logger.info("action=createTask status=success serialNumber={} taskType={}", task.getSerialNumber(),
                task.getClass().getName());
    }

    public void removeTask(String serialNumber) {
        ScheduledFuture<?> task = tasks.remove(serialNumber);
        if (!task.isCancelled() && !task.isDone()) {
            if (!task.cancel(false)) {
                logger.error("action=cancelTask status=failure serialNumber={}", serialNumber);
            } else {
                logger.error("action=cancelTask status=success serialNumber={}", serialNumber);
            }
        }
    }

    @PreDestroy
    private void destroy() {
        executor.shutdown();
    }
}
