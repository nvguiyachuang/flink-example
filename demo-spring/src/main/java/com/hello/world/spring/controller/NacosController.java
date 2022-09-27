//package com.hello.world.spring.controller;
//
//import com.alibaba.nacos.api.annotation.NacosInjected;
//import com.alibaba.nacos.api.config.annotation.NacosValue;
//import com.alibaba.nacos.api.exception.NacosException;
//import com.alibaba.nacos.api.naming.NamingService;
//import com.alibaba.nacos.api.naming.pojo.Instance;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cloud.context.config.annotation.RefreshScope;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("nacos")
//@RefreshScope
//public class NacosController {
//
//    @Value("${useLocalCache:false}")
//    private boolean useLocalCache;
//
//    @RequestMapping("/get")
//    public boolean get() {
//        return useLocalCache;
//    }
//
////    @NacosInjected
////    private NamingService namingService;
////
////    @GetMapping(value = "/get2")
////    public List<Instance> get2() throws NacosException {
////        List<Instance> gateway = namingService.getAllInstances("gateway");
////        System.out.println(gateway);
////        return gateway;
////    }
//}
