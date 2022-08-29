package com.hello.world.jdk;//package jdk;
//
//import java.util.function.BiFunction;
//
//public class Jdk10 {
//    public static void main(String[] args) {
//        /*
//-- 简化写法
//public Person getPersonById(Long id) {
//	Optional<Person> personOpt = repository.findById(id);
//	return personOpt.orElseThrow();
//}
//
//-- 复杂写法
//public Person getPersonByIdOldWay(Long id) {
//	Optional<Person> personOpt = repository.findById(id);
//	if (personOpt.isPresent())
//		return personOpt.get();
//	else
//		throw new NoSuchElementException();
//}
//
//-- 简化写法
//public void printPersonById(Long id) {
//	Optional<Person> personOpt = repository.findById(id);
//	personOpt.ifPresentOrElse(
//			System.out::println,
//			() -> System.out.println("Person not found")
//	);
//}
//
//-- 复杂写法
//public void printPersonByIdOldWay(Long id) {
//	Optional<Person> personOpt = repository.findById(id);
//	if (personOpt.isPresent())
//		System.out.println(personOpt.get());
//	else
//		System.out.println("Person not found");
//}
//
//         */
//
//        String result = sumOfString();
//        System.out.println(result);
//    }
//
//    // jdk11
//    // 从Java 10开始，您可以声明没有其类型的局部变量。您只需要定义var关键字而不是类型。
//    // 从Java 11开始，您还可以将其与lambda表达式一起使用，如下所示。
//    public static String sumOfString() {
//        BiFunction<String, String, String> func = (var x, var y) -> x + y;
//        return func.apply("abc", "efg");
//    }
//
//}
