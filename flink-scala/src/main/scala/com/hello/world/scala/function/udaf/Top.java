package com.hello.world.scala.function.udaf;

import org.apache.flink.table.functions.AggregateFunction;

import java.util.ArrayList;
import java.util.List;

public class Top extends AggregateFunction<String, List<Pair>> {

    @Override
    public List<Pair> createAccumulator() {
        return new ArrayList<>();
    }

    @Override
    public String getValue(List<Pair> acc) {
        return acc.toString();
    }

    public void accumulate(List<Pair> acc, String iValue, Long iWeight) {
        acc.add(new Pair(iValue, iWeight));
    }

    public void retract(List<Pair> acc, String iValue, Long iWeight) {

    }
}
