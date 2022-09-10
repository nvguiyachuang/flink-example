package com.hello.world.spring.mock.dep;

import org.springframework.beans.factory.annotation.Autowired;

//UserService.java UserService通过@Configuration配置由Spirng IOC管理
public class UserService
{
    @Autowired
    UserMapper userMapper;
    
    public User getOne(Integer id)
    {
       return userMapper.selectOne(id);
    }
}

