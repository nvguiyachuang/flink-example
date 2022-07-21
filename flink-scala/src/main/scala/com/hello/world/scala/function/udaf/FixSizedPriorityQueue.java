package com.hello.world.scala.function.udaf;

import lombok.Data;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;

@Data
public class FixSizedPriorityQueue<E extends Comparable<E>> {
    public PriorityQueue<E> queue;
    public int maxSize;// 堆的最大容量

    public FixSizedPriorityQueue(){
        this.maxSize = 4;
        this.queue = new PriorityQueue<>(maxSize, Comparator.reverseOrder());
    }

    public FixSizedPriorityQueue(int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException();
        }
        this.maxSize = maxSize;
        // 生成最大堆使用o2-o1,生成最小堆使用o1-o2, 并修改 e.compareTo(peek) 比较规则
        this.queue = new PriorityQueue<>(maxSize, Comparator.reverseOrder());
    }

    public FixSizedPriorityQueue(int size, PriorityQueue<E> queue) {
        this.maxSize = size;
        this.queue = queue;
    }

    public void add(E e) {
        if (queue.size() < maxSize) {// 未达到最大容量，直接添加
            queue.add(e);
        } else {// 队列已满
            E peek = queue.peek();
            assert peek != null;
            if (e.compareTo(peek) < 0) {// 将新元素与当前堆顶元素比较，保留较小的元素
                queue.poll();
                queue.add(e);
            }
        }
    }

    public static void main(String[] args) {
        final FixSizedPriorityQueue<Integer> pq = new FixSizedPriorityQueue<>(10);
        Random random = new Random();
        int rNum;
        System.out.println("100 个 0~999 之间的随机数：-----------------------------");
        for (int i = 1; i <= 100; i++) {
            rNum = random.nextInt(1000);
            System.out.print(rNum + ", ");
            pq.add(rNum);
        }
        System.out.println();
        System.out.println("PriorityQueue 本身的遍历是无序的：------------------------");

        for (Integer item : pq.queue) {
            System.out.print(item + ", ");
        }
        System.out.println();
        System.out.println(pq.queue);
        System.out.println("PriorityQueue 排序后的遍历：--------------------------");
        // 直接用内置的 poll() 方法，每次取队首元素（堆顶的最大值）
        while (!pq.queue.isEmpty()) {
            System.out.print(pq.queue.poll() + ", ");
        }
    }

}