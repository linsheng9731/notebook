## 请对比 Exception 和 Error，运行时异常和一般异常有什么区别？

Exception 是程序正常运行中，可以预料的意外情况，可能并且应该被捕获，进行相应处理。Error 是指在正常情况下，不大可能出现的情况，绝大部分的 Error 都会导致程序（比如 JVM 自身）处于非正常的、不可恢复状态。既然是非正常情况，所以不便于也不需要捕获，常见的比如 OutOfMemoryError 之类，都是 Error 的子类。


## ClassNotFoundException和NoClassDefFoundError的区别？

ClassNotFoundException发生在装入阶段。Java支持使用Class.forName方法来动态地加载类，任意一个类的类名如果被作为参数传递给这个方法都将导致该类被加载到JVM内存中，如果这个类在类路径中没有被找到，那么此时就会在运行时抛出ClassNotFoundException异常。
NoClassDefFoundError： 当目前执行的类已经编译，但是找不到它的定义时。如果JVM或者ClassLoader实例尝试加载（可以通过正常的方法调用，也可能是使用new来创建新的对象）类的时候却找不到类的定义。

## final、finally、finalize 有什么不同？

final 修饰的 class 代表不可以继承扩展，final 的变量是不可以修改的，而 final 的方法也是不可以重写的（override）。
finally 则是 Java 保证重点代码一定要被执行的一种机制。
特例：
```
try {
    System.exit(1);
} finally {
    System.out.println("Print from finally");
}
```
finalize 是基础类 java.lang.Object 的一个方法，它的设计目的是保证对象在被垃圾收集前完成特定资源的回收。finalize 机制现在已经不推荐使用，并且在 JDK 9 开始被标记为 deprecated。
