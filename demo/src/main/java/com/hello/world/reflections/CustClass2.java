package com.hello.world.reflections;

@CustAnn
public class CustClass2 implements Limit{

    @Override
    public void say() {
        System.out.println("CustClass2");
    }
}
