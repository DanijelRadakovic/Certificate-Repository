package megatravel.com.cerrepo.task;

import java.util.concurrent.TimeUnit;

public abstract class Task implements Runnable {

    protected String serialNumber;
    protected long delay;
    protected TimeUnit timeUnit;

    Task() {
    }

    Task(String serialNumber, long delay, TimeUnit timeUnit) {
        this.serialNumber = serialNumber;
        this.delay = delay;
        this.timeUnit = timeUnit;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }
}
