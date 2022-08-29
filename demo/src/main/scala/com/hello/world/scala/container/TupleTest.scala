package com.hello.world.scala.container

/**
 * 最多插入22个元素
 *
 * @author xx on 2020/5/24 10:45
 */
object TupleTest {
    def main(args: Array[String]): Unit = {
        val tuple: (Int, Int, Int, Int, String) = (1, 2, 3, 4, "aaa")
    
        val iterator = tuple.productIterator
        while (iterator.hasNext){
            println(iterator.next())
        }
        
        for (ele <- tuple.productIterator) {
            println(ele)
        }
    }
}
