package com.hello.world.jdk;//package jdk;
//
//import java.util.List;
//import java.util.Map;
//
//public class Jdk9 {
//    public static void main(String[] args) {
//        List<String> fruits = List.of("apple", "banana", "orange");
//        System.out.println(fruits);
//        Map<Integer, String> numbers = Map.of(1, "one", 2,"two", 3, "three");
//        System.out.println(numbers);
//    }
//}
//
//// 接口中定义私有方法
//interface ExampleInterface {
//
//    private void printMsg(String methodName) {
//        System.out.println("Calling interface");
//        System.out.println("Interface method: " + methodName);
//    }
//
//    default void method1() {
//        printMsg("method1");
//    }
//
//    default void method2() {
//        printMsg("method2");
//    }
//}
