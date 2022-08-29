package com.hello.world.jdk;//package jdk;
//
//public class Jdk12 {
//    public static void main(String[] args) {
//        String s = new Jdk12().newMultiSwitch(1);
//        System.out.println(s);
//    }
//
//    // 新特性
//    public String newMultiSwitch(int day) {
//        return switch (day) {
//            case 1, 2, 3, 4, 5 -> "workday";
//            case 6, 7 -> "weekend";
//            default -> "invalid";
//        };
//    }
//
//    // 旧的复杂写法
//    public String oldMultiSwitch(int day) {
//        switch (day) {
//            case 1:
//            case 2:
//            case 3:
//            case 4:
//            case 5:
//                return "workday";
//            case 6:
//            case 7:
//                return "weekend";
//            default:
//                return "invalid";
//        }
//    }
//
//}
