package com.hello.world.jdk;//package jdk;
//
//public class Jdk15 {
//}
//
//// 使用密封类功能，您可以限制超类的使用。使用new关键字，sealed您可以定义哪些其他类或接口可以扩展或实现当前类。
//abstract sealed class Pet permits Cat, Dog {
//}
//
//// 允许的子类必须定义一个修饰符。如果您不想允许任何其他扩展名，则需要使用final关键字。
//final class Cat extends Pet {
//}
//// 另一方面，您可以打开扩展类。在这种情况下，应使用non-sealed修饰符。
////
////public non-sealed class Dog extends Pet {}
//
//non-sealed class Dog extends Pet {
//}
//
///**
// * 当然，下面的可见声明是不允许的。
// * <p>
// * class Demo extends Cat{}
// * final class Tiger extends Pet {}
// */
//class Demo2 extends Dog {
//}
