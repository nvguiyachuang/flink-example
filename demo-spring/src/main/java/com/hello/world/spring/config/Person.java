package com.hello.world.spring.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
@Validated
public class Person {

    /**
     * <bean class="Person">
     * <property name="lastName" value="字面量/${key}从环境变量、配置文件中获取值/#{SpEL}"></property>      * <bean/>
     */

    //lastName必须是邮箱格式
//    @Email
    @Value("${test.person.last-name}")
    private String lastName;
    @Value("#{11*2}")
    private Integer age;
    @Value("true")
    private Boolean boss;

    private Date birth;
    private Map<String, Object> maps;
    private List<Object> lists;
    private Dog dog;
}