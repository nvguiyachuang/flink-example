package com.hello.world.demo.util;

import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;

public class UnitUtil {

    public static String getNetFileSizeDescription(long size) {
        StringBuilder bytes = new StringBuilder();
        DecimalFormat format = new DecimalFormat("###.0");
        if (size >= 1024 * 1024 * 1024) {
            double i = (size / (1024.0 * 1024.0 * 1024.0));
            bytes.append(format.format(i)).append("GB");
        } else if (size >= 1024 * 1024) {
            double i = (size / (1024.0 * 1024.0));
            bytes.append(format.format(i)).append("MB");
        } else if (size >= 1024) {
            double i = (size / (1024.0));
            bytes.append(format.format(i)).append("KB");
        } else {
            if (size <= 0) {
                bytes.append("0B");
            } else {
                bytes.append((int) size).append("B");
            }
        }
        return bytes.toString();
    }

    public static void main(String[] args) {
        String netFileSizeDescription = getNetFileSizeDescription(1000L );
        System.out.println(netFileSizeDescription);

        String str = " ";
        System.out.println(StringUtils.isEmpty(str));
        System.out.println(StringUtils.isBlank(str));
    }

}
