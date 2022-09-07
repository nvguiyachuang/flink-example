package com.hello.world.demo.config;

import com.hello.world.demo.scala.generic.Dog;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 将配置文件中配置的每一个属性的值，映射到这个组件中
 * @ConfigurationProperties：告诉SpringBoot将本类中的所有属性和配置文件中相关的配置进行绑定；
 *      prefix = "testdemo.person"：配置文件中哪个下面的所有属性进行一一映射(yml文件中的参数名要和JavaBean中的属性名保持一致)
 * 只有这个组件是容器中的组件，才能容器提供的@ConfigurationProperties功能；
 *
 * <a href="https://www.cnblogs.com/vegetableDD/p/11968261.html">source link</a>
 */
@Component
@ConfigurationProperties(prefix = "testdemo.person")
public class PersonPojo {

    private String lastName;
    private Integer age;
    private Boolean boss;
    private Date birth;

    private Map<String,Object> maps;
    private List<Object> lists;
    private Dog dog;
}