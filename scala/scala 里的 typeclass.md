# Scala 里的 typeclass
##引言
TypeClass 是一个强大而灵活的概念，它将特殊的多态性添加到Scala中。
TypeClass 首先在Haskell中引入，作为一种多态的新方法。TypeClass 是一个类（组），在trait中定义某个契约，并且可以添加针对不同类型的具体实，而不需要对原始代码进行任何更改。可以说通过扩展一个简单的 trait 可以实现同样的目标，但是对于 TypeClass，事先并不需要显示的在代码中编写这种需求。

##一个例子
我们来编写一个 TypeClass，它会拥有一个 show 方法，接受任意类型的参数，返回它们的字符串表示。类似于内置的 .toString：

```
trait Show[A] {
  def show(a: A): String
}

object Show {
  val intCanShow: Show[Int] =
    new Show[Int] {
      def show(int: Int): String = s"int $int"
    }
}
```
然后我们可以这样使用：

```
println(intCanShow.show(20))
```
看上去很简单，但是对于调用者而言似乎有些不太方便，因为需要知道太多细节，比如对于 Int 类型需要指定 intCanShow 变量。而且对于不同的类型需要了解的细节是不一样的，总而言之就是不太灵活。针对这个问题我们引入了隐式转化，让编译器自己去寻找合适的 show。

```
object Show {
  def apply[A](implicit sh: Show[A]): Show[A] = sh

  def show[A: Show](a: A) = Show[A].show(a)

  implicit class ShowOps[A: Show](a: A) {
    def show = Show[A].show(a)
  }

  implicit val intCanShow: Show[Int] =
    new Show[Int] {
      def show(int: Int): String = s"int $int"
    }
}
```
所以我们可以这样来使用 show：

```
println(30.show)
```
看上去好很多，调用者终于可以忽略细节。来仔细看一下实现，当编译器遇到 30.show 的时候，他会在语言内置的环境下去寻找，发现 Int 类型没有 show 方法，然后他会抛出一个异常丢给隐式转化。隐式转化会找到 ShowOps 定义，得到 Show[A].show(a)，Show[A] 等价于 Show.apply[A]，同时根据类型 Int 得到 intCanShow，最后调用 `def show(int: Int): String = s"int $int"`。

上面的实现就是 typeclass，让我们添加更多针对不同类型的实现：

```
implicit val stringCanShow: Show[String] =
  new Show[String]{
    def show(str: String): String = s"string $str"
  }
```
这样我们就有了对 Int，String两种类型都有了 show 方法。仔细观察一下上面的实现就可以发现除了针对不同类型做不同操作之外，其他代码对于所有的 typeclass 是有共通的部分的。所以就产生了一些类库来做这个事情。比较常用的是 [Simulacrum](https://github.com/mpilquist/simulacrum)，对于上面的实现如果改用 simulacrum 可以做到非常简化：

```
import simulacrum._

@typeclass trait ShowSim[A] {
  def showSim(a: A): String
}

object ShowSim {
  implicit val stringCanShow: ShowSim[String] =
    str => s"simulacrum string $str"
}
```
只需要定义扩展的名称，以及扩展对于不同类型的具体实现即可。

##小结
对于 typeclass 其实没有特别神奇的地方，它是一种对既有类型增强的通用模式。通过组合 trait，泛型，以及隐式转化就可以很轻松的实现 typeclass。如果在这个基础上使用宏，则可以大大简化代码。


