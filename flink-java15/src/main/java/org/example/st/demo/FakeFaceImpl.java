package org.example.st.demo;

public class FakeFaceImpl extends FaceImpl implements FakeFace{
    public static void main(String[] args) {
        FakeFace fakeFace = new FakeFaceImpl();
        fakeFace.say();
    }
}
