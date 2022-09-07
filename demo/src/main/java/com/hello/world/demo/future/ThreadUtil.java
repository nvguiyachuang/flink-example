package com.hello.world.demo.future;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Slf4j
public class ThreadUtil {

//    private static final Logger LOG = LoggerFactory.getLogger(ThreadUtils.class);

    public static ExecutorService newThreadPool(
            int poolSize, int poolQueueSize, long keepAliveMs, String threadPoolName) {
        log.info(
                "Created thread pool {} with core size {}, max size {} and keep alive time {}ms.",
                threadPoolName,
                poolSize,
                poolQueueSize,
                keepAliveMs);

        return new ThreadPoolExecutor(
                poolSize,
                poolQueueSize,
                keepAliveMs,
                TimeUnit.MILLISECONDS,
                new SynchronousQueue<>());
    }

    public static ExecutorService newThreadPool2() {
        return Executors.newCachedThreadPool();
    }
}
