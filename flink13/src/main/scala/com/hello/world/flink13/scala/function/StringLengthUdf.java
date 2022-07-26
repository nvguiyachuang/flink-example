package com.hello.world.flink13.scala.function;

import org.apache.flink.table.functions.FunctionContext;
import org.apache.flink.table.functions.ScalarFunction;

public class StringLengthUdf extends ScalarFunction {
    // 可选，open方法可以不写。
    // 如果编写open方法需要声明'import org.apache.flink.table.functions.FunctionContext;'。
    @Override
    public void open(FunctionContext context) {
    }

    public long eval(String a) {
        return a == null ? 0 : a.length();
    }

    public long eval(String b, String c) {
        return eval(b) + eval(c);
    }

    //可选，close方法可以不写。
    @Override
    public void close() {
    }
}