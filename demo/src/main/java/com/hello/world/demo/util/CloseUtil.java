package com.hello.world.demo.util;

public class CloseUtil {

    public static void close(AutoCloseable... autoCloseables) {
        if (null == autoCloseables || autoCloseables.length <= 0) {
            return;
        }

        for (AutoCloseable autoCloseable : autoCloseables) {
            if (null != autoCloseable) {
                try {
                    autoCloseable.close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}
