package com.hello.world.reflections;

import org.reflections.Reflections;

import java.util.Set;

/**
 * Reflections 详细介绍
 * Reflections 通过扫描 classpath，索引元数据，允许在运行时查询这些元数据，也可以保存收集项目中多个模块的元数据信息。
 *
 * 使用 Reflections 可以查询以下元数据信息：
 * 1）获得某个类型的所有子类型
 * 2）获得标记了某个注解的所有类型／成员变量，支持注解参数匹配。
 * 3）使用正则表达式获得所有匹配的资源文件
 * 4）获得所有特定签名（包括参数，参数注解，返回值）的方法
 */
public class Main {
    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Reflections reflections = new Reflections("reflections");
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(CustAnn.class);

        for (Class<?> aClass : typesAnnotatedWith) {
            Class<?> forName = Class.forName(aClass.getName());
            Object o = forName.newInstance();
            Limit custClass = (Limit)o;

            custClass.say();
        }
    }
}
