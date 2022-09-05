package com.hello.world.demo.util;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Function;

public class BigDecimalUtil {
    public static void main(String[] args) {

        BigDecimal bigDecimal = BigDecimal.valueOf(8888L);
        BigDecimal b = BigDecimal.valueOf(999L);
        BigDecimal[] arr = {bigDecimal, b};
        Optional<BigDecimal> max = Arrays.stream(arr).max(Comparator.comparing(Function.identity()));
        BigDecimal bigDecimal1 = max.get();
        System.out.println(bigDecimal1);

        int i = 1024 * 1024 * 1024;
        System.out.println(i);
        System.out.println(Integer.MAX_VALUE);
    }
}
