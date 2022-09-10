package com.hello.world.spring.mock.dep;

public class UserMapperImpl implements UserMapper{
   @Override
   public User selectOne(Integer id) {
      return null;
   }

   @Override
   public void print() {
      System.out.println("test");
   }
}
