# CyclicDependencies
依赖注入经常遇到的一个问题就是循环依赖。看一个例子：
```
public class Store {
  private final Boss boss;
  //...

  @Inject public Store(Boss boss) {
     this.boss = boss;
     //...
  }
  public void incomingCustomer(Customer customer) {...}
  public Customer getNextCustomer() {...}
}

public class Boss {
  private final Clerk clerk;
  @Inject public Boss(Clerk clerk) {
    this.clerk = clerk;
  }
}

public class Clerk {
  private final Store shop;
  @Inject Clerk(Store shop) {
    this.shop = shop;
  }

  void doSale() {
    Customer sucker = shop.getNextCustomer();
    //...
  }
}
```
上面例子中的依赖链： Store -> Boss -> Clerk -> Store 造成了循环依赖。
解决方案有两种：
- 通过合理的设计解除循环依赖
- 使用 Provider 打破循环链条

## 通过合理的设计解除循环依赖
上面的例子，分析一下造成循环依赖的原因是 clerk 需要 custom 对象，但是 custom 包含在 store 对象里。所以我们可以引入第三方对象 CustomerLine 来存储 custom，让 cler 和 store 都引用 CustomerLine。
```
public class Store {
  private final Boss boss;
  private final CustomerLine line;
  //...

  @Inject public Store(Boss boss, CustomerLine line) {
     this.boss = boss;
     this.line = line;
     //...
  }

  public void incomingCustomer(Customer customer) { line.add(customer); }
}

public class Clerk {
  // 这样就不需要直接引入 store
  private final CustomerLine line;

  @Inject Clerk(CustomerLine line) {
    this.line = line;
  }

  void doSale() {
    Customer sucker = line.getNextCustomer();
    //...
  }
}
```
这样依赖链条就变成： Store -> Boss -> Clerk -> CustomerLine 和 Store -> CustomerLine 从而打破了循环依赖。

## 使用 Provider 打破循环链条
还是上面的例子，通过注入 guice provider 避免 clerk 直接引用 store 从而避免造成循环依赖。
```
public class Clerk {
  private final Provider<Store> shopProvider;
  @Inject Clerk(Provider<Store> shopProvider) {
    this.shopProvider = shopProvider;
  }

  void doSale() {
    Customer sucker = shopProvider.get().getNextCustomer();
    //...
  }
}
```
这样依赖链条就变成： Store -> Boss -> Clerk -> Provider<Store> 。
需要注意的是如果 shop 本身不是 singleton 会造成 provider 每次获取的 shop 都不同。