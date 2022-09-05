package com.hello.world.flink13.scala.function;

import org.apache.flink.table.annotation.DataTypeHint;
import org.apache.flink.table.annotation.FunctionHint;
import org.apache.flink.table.functions.FunctionContext;
import org.apache.flink.table.functions.TableFunction;
import org.apache.flink.types.Row;

@FunctionHint(output = @DataTypeHint("ROW<word STRING>"))
public class SplitUdtf extends TableFunction<Row> {

    // 可选，open方法可不编写。如果编写，则需要添加声明'import org.apache.flink.table.functions.FunctionContext;'。
    @Override
    public void open(FunctionContext context) {
        // ... ...
    }

    public void eval(String str) {
        String[] split = str.split(",");
        for (String s : split) {
            collect(Row.of(s));
        }
    }

    // 可选，close方法可不编写。
    @Override
    public void close() {
        // ... ...
    }
}