# Scala 的 return
##return会打破程序结构
scala中有return，语义和其他语言没什么区别，但是最好不要使用它。我们先看一个小例子:

```
// Add two ints, and use this method to sum a list
def add(n:Int, m:Int): Int = n + m
def sum(ns: Int*): Int = ns.foldLeft(0)(add)

scala> sum(33, 42, 99)
res0: Int = 174

// Same, using return 
def addR(n:Int, m:Int): Int = return n + m
def sumR(ns: Int*): Int = ns.foldLeft(0)(addR)

scala> sumR(33, 42, 99)
res1: Int = 174
```
上面是一个sum函数，一个没有return就一个用了return，结果是一样的。但是，当我们通过内联add来进行重构addR。

```
// Inline add and addR
def sum(ns: Int*): Int = ns.foldLeft(0)((n, m) => n + m) // inlined add

scala> sum(33, 42, 99)
res2: Int = 174 // alright

def sumR(ns: Int*): Int = ns.foldLeft(0)((n, m) => return n + m) // inlined addR

scala> sumR(33, 42, 99)
res3: Int = 33 // um.
```
所以发生了什么？当foldLeft碰到第一个return的时候直接返回了，在内联函数的情况下和我们预想的有些区别。再看看另一个例子：

```
def foo: Int = { 
  val sumR: List[Int] => Int = _.foldLeft(0)((n, m) => return n + m)
  sumR(List(1,2,3)) + sumR(List(4,5,6))
}
```
分开看两个sumR的结果分别是1和4，所以相加的结果应该是5。但是实际结果却是1！。所以return打断了程序的运行，会产生意外的结果。

##return的类型
接着往下看另一个例子：

```
scala> def foo: () => Int = () => return () => 1
foo: () => Int

scala> val x = foo
x: () => Int = <function0>

scala> x()
scala.runtime.NonLocalReturnControl
```
突然冒出一个奇怪的东西，scala.runtime.NonLocalReturnControl。这就要说一说在scala在lambda中的return是如何实现的了。因为对于匿名函数而言return无法达到函数的最外层，所以scala使用了异常机制，scala.runtime.NonLocalReturnControl是继承自NoStackTrace，而NoStackTrace来自Throwable，所以就有了上面的结果。所以在使用for之类的循环结构时，如果恰巧使用了return + lambda，则很有可能会抛出意想不到的异常。

##小结
基于上面的几个场景，建议在任何情况下不要是用return，这个习惯会保护你避开上面几个奇怪的坑。

