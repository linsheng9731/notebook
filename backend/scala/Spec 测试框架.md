# Spec
## 前言
spec 是一个行为描述测试框架，官方的一句话介绍是这样的：

>Specify your software using both text and Scala code

使用文本和 scala 代码来描述你的程序。具体我们可以感受一下他的风格：
文本说明和测试代码分离的风格：

```
class HelloWorldSpec extends Specification { 
def is = s2"""

  This is a specification for the 'Hello world' string

  The 'Hello world' string should
    contain 11 characters                             $e1
    start with 'Hello'                                $e2
    end with 'world'                                  $e3
                                                      """
  def e1 = "Hello world" must haveSize(11)
  def e2 = "Hello world" must startWith("Hello")
  def e3 = "Hello world" must endWith("world")

```
说明和代码混合的风格：

```
class HelloWorldSpec extends Specification {
  "This is a specification for the 'Hello world' string".txt

  "The 'Hello world' string should" >> {
    "contain 11 characters" >> {
      "Hello world" must haveSize(11)
    }
    "start with 'Hello'" >> {
      "Hello world" must startWith("Hello")
    }
    "end with 'world'" >> {
      "Hello world" must endWith("world")
    }
  }
}
```
和一般的测试框架不一样的地方在于 spec 强调测试应该是可读的一份说明文档，一个好的测试例子应该对应相应的功能点，这个软件有几个功能点应该通过看测试用例就一清二楚。

## 快速开始

1. 安装 sbt
2. 添加 spec2 依赖到 build.sb ：

```
 // Read here for optional jars and dependencies
libraryDependencies ++= Seq("org.specs2" %% "specs2-core" % "3.7.2" % "test")

scalacOptions in Test ++= Seq("-Yrangepos")
```
3. 编写测试用例

```
import org.specs2._

class QuickStartSpec extends Specification { def is = s2"""

 This is my first specification
   it is working                 $ok
   really working!               $ok
                                 """
}
```
4. 运行

```
> sbt
> testOnly QuickStartSpec
[info] QuickStartSpec
[info]
[info]  This is my first specification
[info]    + it is working
[info]    + really working!
[info]
[info] Total for specification QuickStartSpec
[info] Finished in 23 ms
[info] 2 examples, 0 failure, 0 error
```
 
## 风格
从上面我们看到了 spec 支持的两种风格，第一种分离的风格从文本阅读上来说比较清楚，但是缺点也很明显，如果我们的测试因为代码改动挂掉了，那么我们就要从代码和文本之间切换来理解测试逻辑。两种风格在使用上也有稍许不同，分离风格只读取最后一次判断的结果，比如下面的例子:

```
// this will never fail!
s2"""
  my example on strings $e1
"""
  def e1 = {
    // because this expectation will not be returned,...
    "hello" must have size (10000)
    "hello" must startWith("hell")
  }
```
第一个判断应该是错的，不过整体的结果却是正确的。因为在分离风格里面 spec 只读取最后一个判断结果， spec 希望使用者能遵循一个说明点一个用例的原则，尽量的将代码设计的足够小。当然，如果你一定要在一个例子里测试多个功能点也可以像下面这样做：

```
s2"""
  my example on strings $e1
"""
  def e1 = ("hello" must have size (10000)) and
           ("hello" must startWith("hell"))
```
反之，在混合风格里失败的断言会抛出一个异常，终止当前的测试。

```
class MySpecification extends org.specs2.mutable.Specification {
  "This is my example" >> {
    1 must_== 2 // this fails
    1 must_== 1 // this is not executed
  }
}
```
## 断言
在测试框架里断言可能是我们最常用的功能了，spec提供了各种类型的断言供选择，可以说相当的强大易用！

最简单的判断相等性：

| Matcher | Comment |
| --- | --- |
| 1 must beEqualTo(1) | the normal way |
| 1 must be(1) | with a symbol |
| 1 must 1  | my favorite! |
| 1 mustEqual 1 | if you dislike underscores |
| 1 should== 1 | for should lovers |
| 1 === 1  | the ultimate shortcut |
| 1 must be equalTo(1)  | with a literate style |

对于上面的相等性断言有一个需要特别注意的就是 === ，对于 scala 本身而言 == 判断两个对象的引用是否相等，但是对于 spec 的 === 则采用了深度对比，所以在 scala 里 Array(1,2,3) == Array(1,2,3) 不成立。但是在 spec 里 Array(1,2,3) === Array(1,2,3) 是正确。 

检测类型相等性：

|Matcher|	Comment
|---|---|
|beTypedEqualTo	|typed equality. a must beTypedEqualTo(b) will not work if a and b don’t have compatible types
|be_===	|synonym for beTypedEqualTo
|a ==== b|	synonym for a must beTypedEqualTo(b)
|a must_=== b|	similar to a must_== b but will not typecheck if a and b don’t have the same type
|be_==~|	check if (a: A) == conv(b: B) when there is an implicit conversion conv from B to A
|beTheSameAs|	reference equality: check if a eq b (a must be(b) also works)
|be|	a must be(b): synonym for beTheSameAs
|beTrue, beFalse|	shortcuts for Boolean equality

除了上面这些断言之外 spec 还提供了许多有意思的断言，

|Matcher|commnet|
|---|---|
|beMatching|check if a string matches a regular expression
|must contain| Seq(1, 2, 3, 4) must contain(2, 4)
|beBetween|5 must beBetween(3, 6)
|beSome/beRight/beLeft|check Option and Either instances
|must haveKey/must haveValues/must havePair|the matchers for map

检测异常：

|Matcher|commnet|
|---|---|
|throwA[ExceptionType]|check if a block of code throws an exception of the given type|
|throwA[ExceptionType] (message = "boom")|check if the exception message is as expected

检测 Future：
对于 Future 的处理你可以使用 scala.concurrent.Await 的 result 方法来获取值，然后同步的使用任意 matcher 来进行断言。

```
Await.result(Future.successful(1), 60 seconds) mustEqual 1
```
spec 提供了更加直观的做法：

```
Future { Thread.sleep(100); 1 } must be_>(0).await(retries = 2, timeout = 100.millis)

// only retries, timeout is 1.second
Future { Thread.sleep(100); 1 } must be_>(0).retryAwait(retries = 2)

// only timeout, retries = 0
Future { Thread.sleep(100); 1 } must be_>(0).awaitFor(100.millis)
```
在任意 matcher 后面添加等待操作，效果和上面是等价的。

检测 case class ：

```
// case class for a Cat
case class Cat(name: String = "", age: Int = 0, kitten: Seq[Cat] = Seq())
// a given cat
val cat = Cat(name = "Kitty", age = 6, kitten = Seq(Cat("Oreo", 1), Cat("Ella", 2)))

//检测 cese class 类型
cat must matchA[Cat]

//检测属性
def is[A](a: A) = be_==(a)
cat must matchA[Cat].age(is(6))

//级联检测
cat must matchA[Cat]
  .name("Kitty")
  .age(is(6))
  .kitten((_:Seq[Cat]) must haveSize(2))
```

检测 Json 的合法性：
你可以简单的使用 mustEqual 来判断两个 Json 对象是否完全相等，也可以判断两个 Json 对象的部分路径是否相等，像下面这样:

```
// taken from an example in the Lift project
val person = """{
  "person": {
    "name": "Joe",
    "age": 35,
    "spouse": {
      "person": {
        "name": "Marilyn",
        "age": 33
      }
    }
  }
}
"""

person must /("p.*".r) */ ".*on".r /("age" -> "\\d+\\.\\d".r)
person must /("p.*".r) */ ".*on".r /("age" -> startWith("3"))
person must /("p.*".r) */ ".*on".r /("age" -> (be_>(30) ^^ ((_:String).toInt)))
```
## 后记
这篇初体验来自官方的一些整理，自己平时在工作中也总结了一些 spec 和 play 的结合实践，将会陆续整理出来分享给大家。

