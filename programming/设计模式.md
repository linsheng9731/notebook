## 设计模式原则的基本原则？

1. 开闭原则：对扩展开放，对修改关闭
2. 里氏代换原则：能用父类的地方一定能用子类
3. 依赖倒转原则：依赖接口，不依赖具体类
4. 迪米特法则，又称最少知道原则：调用者不关心细节
5. 合成复用原则：能用组合就不用继承


## 有哪些常见的设计模式？

大致按照模式的应用目标分类，设计模式可以分为创建型模式、结构型模式和行为型模式。
- 创建型模式，是对对象创建过程的各种问题和解决方案的总结，包括各种工厂模式（Factory、Abstract Factory）、单例模（Singleton）、构建器模式（Builder）、原型模式（ProtoType）。结构型模式，是针对软件设计结构的总结，关注于类、对象继承、组合方式的实践经验。
- 常见的结构型模式，包括桥接模式（Bridge）、适配器模式（Adapter）、装饰者模式（Decorator）、代理模式（Proxy）、组合模式（Composite）、外观模式（Facade）、享元模式（Flyweight）等。行为型模式，是从类或对象之间交互、职责划分等角度总结的模式。
- 比较常见的行为型模式有策略模式（Strategy）、解释器模式（Interpreter）、命令模式（Command）、观察者模式（Observer）、迭代器模式（Iterator）、模板方法模式（Template Method）、访问者模式（Visitor）。

## 单例模式设计要点

1. 两次检查
2. 使用static优化
3. 使用枚举
4. 懒汉模式
5. 恶汉模式

## 在java中有哪些典型的设计模式使用场景？

Factory:
简单来说，按照需求返回一个类型的实例。
java.lang.Class#forName()

Abstract factory:
创建一组有关联的对象实例。这个模式在JDK中也是相当的常见，还有很多的framework例如Spring。我们很容易找到这样的实例。
java.util.ResourceBundle#getBundle()

Builder:
主要用来简化一个复杂的对象的创建。这个模式也可以用来实现一个 Fluent Interface。
java.lang.StringBuilder#append()
java.lang.StringBuffer#append()

Prototype:
使用自己的实例创建另一个实例。有时候，创建一个实例然后再把已有实例的值拷贝过去，是一个很复杂的动作。所以，使用这个模式可以避免这样的复杂性。
java.lang.Object#clone()

Singleton:
只允许一个实例。在 Effective Java中建议使用Emun.
java.lang.Runtime#getRuntime()

Adapter:
把一个接口或是类变成另外一种。
java.util.Arrays#asList()

Bridge:
把抽象和实现解藕，于是接口和实现可在完全独立开来。
JDBC

Decorator:
为一个对象动态的加上一系列的动作，而不需要因为这些动作的不同而产生大量的继承类。这个模式在JDK中几乎无处不在，所以，下面的列表只是一些典型的。
java.io.BufferedInputStream(InputStream)

Proxy:
用一个简单的对象来代替一个复杂的对象。
java.lang.reflect.Proxy
RMI

Chain of responsibility:
把一个对象在一个链接传递直到被处理。在这个链上的所有的对象有相同的接口（抽象类）但却有不同的实现。
javax.servlet.Filter#doFilter()

Command:
把一个或一些命令封装到一个对象中。
java.lang.Runnable

Interpreter:
一个语法解释器的模式。jdk中的正则工具。
java.util.Pattern

Iterator:
提供一种一致的方法来顺序遍历一个容器中的所有元素。
java.util.Iterator

Mediator（中介）:
用来减少对象单的直接通讯的依赖关系。使用一个中间类来管理消息的方向。
java.util.concurrent.ExecutorService#submit()

Null Object:
这个模式用来解决如果一个Collection中没有元素的情况。
java.util.Collections#emptyList()

Memento:
给一个对象的状态做一个快照。Date类在内部使用了一个long型来做这个快照。
java.util.Date

Strategy:
定义一组算法，并把其封装到一个对象中。然后在运行时，可以灵活的使用其中的一个算法。
java.util.Comparator#compare()

Observer:
允许一个对象向所有的侦听的对象广播自己的消息或事件。
java.util.EventListener
javax.servlet.http.HttpSessionBindingListener

