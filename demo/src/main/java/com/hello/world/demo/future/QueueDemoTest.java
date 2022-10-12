//package com.hello.world.demo.future;
//
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.Test;
//
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.SynchronousQueue;
//import java.util.concurrent.TimeUnit;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@Slf4j
//public class QueueDemoTest {
//
//    @Test
//    void testUseSynchronousQueue() throws Exception {
//        ExecutorService executor = Executors.newFixedThreadPool(2);
//        SynchronousQueue<Object> synchronousQueue = new SynchronousQueue<>();
//
//        Runnable producer = () -> {
//            Object object = new Object();
//            try {
//                synchronousQueue.put(object);
//            } catch (InterruptedException ex) {
//                log.error(ex.getMessage(), ex);
//            }
//            log.info("produced {}", object);
//        };
//
//        Runnable consumer = () -> {
//            try {
//                Object object = synchronousQueue.take();
//                log.info("consumed {}", object);
//            } catch (InterruptedException ex) {
//                log.error(ex.getMessage(), ex);
//            }
//        };
//
//        executor.submit(producer);
//        executor.submit(consumer);
//
//        boolean b = executor.awaitTermination(50000, TimeUnit.MILLISECONDS);
//        System.out.println(b);
//        executor.shutdown();
//    }
//
//    @Test
//    void testBasic(){
//        String a = "aa";
//        String b = "aa";
//        assertThat(a).isEqualTo(b);
//    }
//
//}
