package de.keeeks.channelcreator.util;

import java.util.concurrent.*;

public final class Scheduler {
    private static final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(8);
    private static final ExecutorService executorService = Executors.newCachedThreadPool();

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            scheduledExecutorService.shutdown();
            executorService.shutdown();
        }));
    }

    public static void execute(Runnable runnable) {
        executorService.execute(runnable);
    }

    public static ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
        return scheduledExecutorService.schedule(command, delay, unit);
    }

    public static <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
        return scheduledExecutorService.schedule(callable, delay, unit);
    }

    public static ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        return scheduledExecutorService.scheduleAtFixedRate(command, initialDelay, period, unit);
    }

    public static ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
        return scheduledExecutorService.scheduleWithFixedDelay(command, initialDelay, delay, unit);
    }


}