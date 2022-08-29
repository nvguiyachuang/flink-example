package com.hello.world.spi;

import java.util.ServiceLoader;

public class MainDemo {
    public static void main(String[] args) {
//    Iterator<CustomInterface> providers = Service.providers(CustomInterface.class);
//
//    while (providers.hasNext()) {
//      CustomInterface ser = providers.next();
//      ser.test();
//    }

        System.out.println("--------------------------------");


        ServiceLoader<CustomInterface> load = ServiceLoader.load(CustomInterface.class);
        for (CustomInterface ser : load) {
            ser.test();
        }
    }
}
