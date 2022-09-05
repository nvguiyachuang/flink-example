package com.hello.world.demo.jdk;//package jdk;
//
//import java.util.Objects;
//
//public class Jdk14 {
//    public static void main(String[] args) {
//        Person person = new Person("xxx", 11);
//        String name = person.name();
//        System.out.println(name);
//    }
//}
//
//record Person(String name, int age) {
//    private static String name2;
//}
//
//class PersonOld {
//
//    private final String name;
//    private final int age;
//
//    public PersonOld(String name, int age) {
//        this.name = name;
//        this.age = age;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public int getAge() {
//        return age;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        PersonOld personOld = (PersonOld) o;
//        return age == personOld.age && name.equals(personOld.name);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(name, age);
//    }
//
//    @Override
//    public String toString() {
//        return "PersonOld{" +
//                "name='" + name + '\'' +
//                ", age=" + age +
//                '}';
//    }
//
//}
