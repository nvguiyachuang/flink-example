package com.hello.world.demo.future;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class scheduledExecutorServiceDemo {
    public static void main(String[] args) throws InterruptedException {
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

        ScheduledFuture<?> scheduledFuture = scheduledExecutorService.scheduleWithFixedDelay(
                () -> System.out.println("hello world"), 0L, 1000L, TimeUnit.MILLISECONDS);

        TimeUnit.SECONDS.sleep(5);
        // cancel schedule
        scheduledFuture.cancel(false);
        // exit jvm
        scheduledExecutorService.shutdown();
    }
}
