package com.hello.world.reflections;

@CustAnn
public class CustClass implements Limit{

    @Override
    public void say() {
        System.out.println("CustClass");
    }
}
