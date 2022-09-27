//package com.hello.world.spring.order;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.cloud.context.config.annotation.RefreshScope;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//
//@Component
//@Order(value = 3)
//@RefreshScope
//public class Order3 implements ApplicationRunner {
//
//    @Value("${useLocalCache:false}")
//    private boolean useLocalCache;
//
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//        System.out.println("ApplicationRunner 3");
//        System.out.println("conf val:" + useLocalCache);
//    }
//}
