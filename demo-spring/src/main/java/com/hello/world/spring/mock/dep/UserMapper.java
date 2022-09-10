package com.hello.world.spring.mock.dep;

//UserMapper.java
public interface UserMapper
{
    User selectOne(Integer id);
	
	void print();
}

