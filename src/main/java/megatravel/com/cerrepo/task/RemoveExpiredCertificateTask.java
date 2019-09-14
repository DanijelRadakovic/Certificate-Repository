package megatravel.com.cerrepo.task;

import java.util.concurrent.TimeUnit;

public class RemoveExpiredCertificateTask extends Task {

    public RemoveExpiredCertificateTask() {
    }

    public RemoveExpiredCertificateTask(String serialNumber, long delay, TimeUnit timeUnit) {
        super(serialNumber, delay, timeUnit);
    }

    @Override
    public void run() {
        // TODO implement deletion of expired certificate
    }


}
