package com.hello.world.java.pojo;

import org.apache.flink.core.execution.PipelineExecutorFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

import static org.apache.flink.util.Preconditions.checkNotNull;

public class PipeDemo {
  public static void main(String[] args) {
    final ServiceLoader<PipelineExecutorFactory> loader = ServiceLoader.load(PipelineExecutorFactory.class);

    final List<PipelineExecutorFactory> compatibleFactories = new ArrayList<>();
    final Iterator<PipelineExecutorFactory> factories = loader.iterator();
    while (factories.hasNext()) {
      PipelineExecutorFactory next = factories.next();
      compatibleFactories.add(next);
    }
    System.out.println(compatibleFactories);
  }
}

