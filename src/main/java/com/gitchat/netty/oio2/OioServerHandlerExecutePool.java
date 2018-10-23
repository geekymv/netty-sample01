package com.gitchat.netty.oio2;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 采用线程池和任务队列，当有新的客户端接入的时候，将客户端的Socket封装成一个Task(Task implements java.lang.Runnable)
 * 传递到后端的线程池中进行处理
 * JDK1.5 提供的java.util.concurrent.ThreadPoolExecutor 的线程池维护一个消息队列和N个活跃的线程对消息队列中的任务进行处理。
 *
 * 线程池可以设置消息队列的大小和最大线程数，因此，它的资源占用是可控的，无论多少个客户端并发访问，都不会导致系统资源耗尽。
 *
 *
 * 我们知道输入和输出流的读和写操作都是同步阻塞的，阻塞的时间取决于对方I/O 线程的处理速度和网络I/O 的传输速度。
 * 采用线程池和任务队列方式只是对之前I/O 线程模型的一个简单优化，无法从根本上解决同步I/O导致的通信线程阻塞的问题。
 *
 */
public class OioServerHandlerExecutePool {

    private ExecutorService executor;

    private static final int DEFAULT_COREPOOLSIZE;

    static {
        DEFAULT_COREPOOLSIZE = Runtime.getRuntime().availableProcessors() * 2;
    }

    public OioServerHandlerExecutePool(int maxPoolSize, int queueSize) {
        this.executor = new ThreadPoolExecutor(DEFAULT_COREPOOLSIZE,
                maxPoolSize, 120L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(queueSize));
    }

    public void execute(Runnable runnable) {
        executor.execute(runnable);
    }
}
