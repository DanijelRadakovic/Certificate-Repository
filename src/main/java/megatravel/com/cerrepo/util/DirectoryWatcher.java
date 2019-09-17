package megatravel.com.cerrepo.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;

public class DirectoryWatcher implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(DirectoryWatcher.class);

    private String path;
    private WatchEvent.Kind<?>[] eventKinds;
    private WatchEventHandler handler;

    public DirectoryWatcher() {
    }

    public DirectoryWatcher(String path, WatchEvent.Kind<?>[] eventKinds, WatchEventHandler handler) {
        this.path = path;
        this.eventKinds = eventKinds;
        this.handler = handler;
    }

    private void watchChanges() {
        final Path directory = Paths.get(this.path);
        try (final WatchService watchService = FileSystems.getDefault().newWatchService()) {
            directory.register(watchService, eventKinds);
            while (true) {

                WatchKey key;
                try {
                    key = watchService.take();
                } catch (InterruptedException ex) {
                    return;
                }
                //final WatchKey wk = watchService.take();
                // Prevent receiving two separate ENTRY_MODIFY events: file modified
                // and timestamp updated. Instead, receive one ENTRY_MODIFY event
                // with two counts.
                Thread.sleep(50);
                key.pollEvents().forEach(watchEvent -> handler.handle(watchEvent, path));
                // reset the key
                boolean valid = key.reset();
                if (!valid) {
                    LOGGER.info("action=watchService status=stopped directory={}", path);
                    break;
                }
            }
        } catch (IOException | InterruptedException e) {
            LOGGER.error(e.getMessage());
        }

    }

    @Override
    public void run() {
        watchChanges();
    }

    public interface WatchEventHandler {
        void handle(WatchEvent<?> watchEvent, String directory);
    }
}