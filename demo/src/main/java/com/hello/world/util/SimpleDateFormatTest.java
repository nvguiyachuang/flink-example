package com.hello.world.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SimpleDateFormatTest {

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(10, 100, 1,
            TimeUnit.MINUTES,
            new LinkedBlockingQueue<>(1000));

    public void test() {
        while (true) {
            poolExecutor.execute(() -> {
                String dateString = simpleDateFormat.format(new Date());
                try {
                    Date parseDate = simpleDateFormat.parse(dateString);

                    String dateString2 = simpleDateFormat.format(parseDate);

                    System.out.println(dateString.equals(dateString2));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private final static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    public void test2() {
        while (true) {
            poolExecutor.execute(() -> {
                LocalDateTime now = LocalDateTime.now();
                String format = dtf.format(now);

                LocalDateTime parse = LocalDateTime.parse(format, dtf);
                String format1 = dtf.format(parse);

                System.out.println();
                System.out.println(format);
                System.out.println(format1);
                System.out.println(format1.equals(format));
            });
        }
    }

    public static void main(String[] args) {
        new SimpleDateFormatTest().test2();
    }

    /**
     * ThreadLocal
     */
    public void test3() throws ParseException {
        // 可以直接设置初始值
        ThreadLocal<SimpleDateFormat> simpleDateFormat = ThreadLocal.withInitial(() ->
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        );

        // 也可以调用set()方法
        ThreadLocal<SimpleDateFormat> simpleDateFormat2 = new ThreadLocal<>();
        simpleDateFormat.set(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        // 调用get()方法取得值
        System.out.println(simpleDateFormat.get().parse("date"));
        // 移除
        simpleDateFormat.remove();
    }
}