# User Guide
## Getting Start
Guice 是一个依赖注入框架。每个对象会在构造函数中指定依赖，为了实例化一个对象需要在构造函数中传入依赖参数。比如下面的例子：

```
class BillingService {
  private final CreditCardProcessor processor;
  private final TransactionLog transactionLog;

  @Inject
  BillingService(CreditCardProcessor processor,
      TransactionLog transactionLog) {
    this.processor = processor;
    this.transactionLog = transactionLog;
  }

  public Receipt chargeOrder(PizzaOrder order, CreditCard creditCard) {
    ...
  }
}
```
BillingService 依赖 processor 和 transactionLog 两个接口。对于 processor 有具体的实现类 PaypalCreditCardProcessor，transactionLog 有具体的实现类 DatabaseTransactionLog。所以可以使用 guice 绑定（bind）具体的实现类。

```
public class BillingModule extends AbstractModule {
  @Override
  protected void configure() {

     /*
      * This tells Guice that whenever it sees a dependency on a TransactionLog,
      * it should satisfy the dependency using a DatabaseTransactionLog.
      */
    bind(TransactionLog.class).to(DatabaseTransactionLog.class);

     /*
      * Similarly, this binding tells Guice that when CreditCardProcessor is used in
      * a dependency, that should be satisfied with a PaypalCreditCardProcessor.
      */
    bind(CreditCardProcessor.class).to(PaypalCreditCardProcessor.class);
  }
}
```
这样在 guice 的对象图构建者(Guice's object-graph builder)中就会记录绑定关系。在使用 injector 创建 BillingService 实例的时候，guice 根据 object-graph 注入依赖实例到构造参数中。

```
 public static void main(String[] args) {
    /*
     * Guice.createInjector() takes your Modules, and returns a new Injector
     * instance. Most applications will call this method exactly once, in their
     * main() method.
     */
    Injector injector = Guice.createInjector(new BillingModule());

    /*
     * Now that we've got the injector, we can build objects.
     */
    BillingService billingService = injector.getInstance(BillingService.class);
    ...
  }
```

## Binding
### Linked Binding 
用于一对一绑定具体实现到接口，可以实现链式绑定，比如：

```
public class BillingModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(TransactionLog.class).to(DatabaseTransactionLog.class);
    bind(DatabaseTransactionLog.class).to(MySqlDatabaseTransactionLog.class);
  }
}
```
上面的例子在实例化过程中如果遇到 TransactionLog 最后 injector 会注入 MySqlDatabaseTransactionLog 实现。

### Binding Annotations
用于多个实现对一个接口的绑定。具体步骤是：
 - 先实现一个绑定注解
 - 使用注解标注构造参数
 - 指定绑定注解、接口、具体实现的关系，可以有多条，以此实现 N:1 的映射关系。
 
 看一个例子：

定义绑定注解：
 ```
package example.pizza;

import com.google.inject.BindingAnnotation;
import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

@BindingAnnotation @Target({ FIELD, PARAMETER, METHOD }) @Retention(RUNTIME)
public @interface PayPal {}
 ```

使用注解标注构造参数 processor:
 ```
 public class RealBillingService implements BillingService {

  @Inject
  public RealBillingService(@PayPal CreditCardProcessor processor,
      TransactionLog transactionLog) {
    ...
  }
 ```
指定绑定注解、接口、具体实现的关系:
```
   bind(CreditCardProcessor.class)
        .annotatedWith(PayPal.class)
        .to(PayPalCreditCardProcessor.class);
```
这样在构造 RealBillingService 实例的时候 injector 会注入 PayPalCreditCardProcessor。我们也可以声明多个绑定注解，实现同一个接口在遇到不同注解的时候注入不同的实现。Guice 自身实现了一个 Named 注解，可以免去声明绑定注解这一步：
```
public class RealBillingService implements BillingService {

  @Inject
  public RealBillingService(@Named("Checkout") CreditCardProcessor processor,
      TransactionLog transactionLog) {
    ...
  }

   bind(CreditCardProcessor.class)
        .annotatedWith(Names.named("Checkout"))
        .to(CheckoutCreditCardProcessor.class);
```
injector 根据 Named 注解中的字符串进行绑定，跟自定义注解相比失去了类型检查。

### Instance Bindings
用于类型和值的绑定，比如给一个标注了 JDBC URL 的字符串参数赋值，或者给一个 Int 类型的参数赋值。
```
 bind(String.class)
        .annotatedWith(Names.named("JDBC URL"))
        .toInstance("jdbc:mysql://localhost/pizza");
    bind(Integer.class)
        .annotatedWith(Names.named("login timeout seconds"))
        .toInstance(10);
```

### Provides Methods
用于标注一个实例生成函数，比如：
```
public class BillingModule extends AbstractModule {
  @Override
  protected void configure() {
    ...
  }

  @Provides
  TransactionLog provideTransactionLog() {
    DatabaseTransactionLog transactionLog = new DatabaseTransactionLog();
    transactionLog.setJdbcUrl("jdbc:mysql://localhost/pizza");
    transactionLog.setThreadPoolSize(30);
    return transactionLog;
  }
}
```
provideTransactionLog 函数提供了 TransactionLog 接口的具体实现。Provides 注解也可以结合绑定注解和 Named 注解。比如下面的例子：
```
  @Provides @PayPal
  CreditCardProcessor providePayPalCreditCardProcessor(
      @Named("PayPal API key") String apiKey) {
    PayPalCreditCardProcessor processor = new PayPalCreditCardProcessor();
    processor.setApiKey(apiKey);
    return processor;
  }
```
当 injector 遇到 PayPal 注解的参数时会调用 providePayPalCreditCardProcessor 来生成一个 PayPalCreditCardProcessor 实例。

### Provider Bindings
当遇到比较复杂的的组装场景，guice 提供了 Provider 接口，可以通过实现 Provider 重写 get 方法来实现复杂的对象生成。
```
public interface Provider<T> {
  T get();
}
```
比如下面这个例子，provider 本身的参数需要依赖注入：
```
public class DatabaseTransactionLogProvider implements Provider<TransactionLog> {
  private final Connection connection;

  @Inject
  public DatabaseTransactionLogProvider(Connection connection) {
    this.connection = connection;
  }

  public TransactionLog get() {
    DatabaseTransactionLog transactionLog = new DatabaseTransactionLog();
    transactionLog.setConnection(connection);
    return transactionLog;
  }
}
```
使用 toProvider 方法进行绑定：
```
public class BillingModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(TransactionLog.class)
        .toProvider(DatabaseTransactionLogProvider.class);
  }
```

### 绑定指定的构造函数
当遇到 @inject 注解无法应用于被注入的对象场景，比如使用了某个第三方库，可以使用 bind toConstructor 显示指定需要注入的构造函数。
```
public class BillingModule extends AbstractModule {
  @Override
  protected void configure() {
    try {
      bind(TransactionLog.class).toConstructor(
          DatabaseTransactionLog.class.getConstructor(DatabaseConnection.class));
    } catch (NoSuchMethodException e) {
      addError(e);
    }
  }
}
``` 
上面的例子，TransactionLog 被绑定到了 DatabaseTransactionLog，其中 DatabaseTransactionLog 的构造函数是以 DatabaseConnection 为入参的。

### Guice 自动生成的绑定
guice 会针对被注入对象查找合格的构造函数，所谓合格即：不是私有、没有构造参数的，不需要显式绑定。通过 @ImplementedBy 指示默认实现类型。
```
@ImplementedBy(PayPalCreditCardProcessor.class)
public interface CreditCardProcessor {
  ChargeResult charge(String amount, CreditCard creditCard)
      throws UnreachableException;
}
 
//和上述等同
bind(CreditCardProcessor.class).to(PayPalCreditCardProcessor.class);
```
@ProvidedBy 用于指示默认的 provider 
```
@ProvidedBy(DatabaseTransactionLogProvider.class)
public interface TransactionLog {
  void logConnectException(UnreachableException e);
  void logChargeResult(ChargeResult result);
}
 
//和上述等同
bind(TransactionLog.class)
        .toProvider(DatabaseTransactionLogProvider.class);
```

## Scopes 作用域
Guice 根据设置的 scope 觉得每次是否需要返回新的实例。Scope 的设置有几种：
 - 单例(@Singleton)
 - 一个会话(@SessionScoped) 与 servlet 结合
 - 一次请求 (@RequestScoped) 与 servlet 结合

在具体实现类上加上具体的 scope 注解：
```
@Singleton
public class InMemoryTransactionLog implements TransactionLog {
  /* everything here should be threadsafe! */
}
```
或者在绑定的时候指定（优先级最高）：
```
 bind(TransactionLog.class).to(InMemoryTransactionLog.class).in(Singleton.class);

 @Provides @Singleton
  TransactionLog provideTransactionLog() {
    ...
  }
```

## 注入方式
### 构造函数注入
最常见的注入使用，在构造函数上使用 @Inject 注解。
```
public class RealBillingService implements BillingService {
  private final CreditCardProcessor processorProvider;
  private final TransactionLog transactionLogProvider;

  @Inject
  public RealBillingService(CreditCardProcessor processorProvider,
      TransactionLog transactionLogProvider) {
    this.processorProvider = processorProvider;
    this.transactionLogProvider = transactionLogProvider;
  }
```

### 方法注入
在方法上标注 @Inject 注解，用户注入方法的参数：
```
public class PayPalCreditCardProcessor implements CreditCardProcessor {

  private static final String DEFAULT_API_KEY = "development-use-only";

  private String apiKey = DEFAULT_API_KEY;

  @Inject
  public void setApiKey(@Named("PayPal API key") String apiKey) {
    this.apiKey = apiKey;
  }
```

### 成员变量注入
注入一个类的成员变量：
```
public class DatabaseTransactionLogProvider implements Provider<TransactionLog> {
  @Inject Connection connection;

  public TransactionLog get() {
    return new DatabaseTransactionLog(connection);
  }
}
```

方法注入和成员变量注入使用显示的 injectMembers 进行注入：
```
public static void main(String[] args) {
    Injector injector = Guice.createInjector(...);

    CreditCardProcessor creditCardProcessor = new PayPalCreditCardProcessor();
    injector.injectMembers(creditCardProcessor);
```

### 可选择的注入
方法注入和成员变量注入可以配置成可选择的（optional），遇到该配置 guice 在注入失败的时候会选择忽略。
```
public class PayPalCreditCardProcessor implements CreditCardProcessor {
  private static final String SANDBOX_API_KEY = "development-use-only";

  private String apiKey = SANDBOX_API_KEY;

  @Inject(optional=true)
  public void setApiKey(@Named("PayPal API key") String apiKey) {
    this.apiKey = apiKey;
  }
```

### 注入 providers
在一些场景下可以不直接注入实例，而是选择注入 provider，这样可以实现实例的懒加载，并且灵活控制实例的个数：
```
public class LogFileTransactionLog implements TransactionLog {

  private final Provider<LogFileEntry> logFileProvider;

  @Inject
  public LogFileTransactionLog(Provider<LogFileEntry> logFileProvider) {
    this.logFileProvider = logFileProvider;
  }

  public void logChargeResult(ChargeResult result) {
    LogFileEntry summaryEntry = logFileProvider.get();
    summaryEntry.setText("Charge " + (result.wasSuccessful() ? "success" : "failure"));
    summaryEntry.save();

    if (!result.wasSuccessful()) {
      LogFileEntry detailEntry = logFileProvider.get();
      detailEntry.setText("Failure result: " + result);
      detailEntry.save();
    }
  }
```

## 面向切面编程 AOP
Guice 的动态字节码技术可以包装方法，用于实现切面编程。比如下面的这个例子，一个点单服务在周末的时候不提供服务。
首先定义一个切面注解：
```
// 定义切面注解
@Retention(RetentionPolicy.RUNTIME) @Target(ElementType.METHOD)
@interface NotOnWeekends {}
```
在需要进行切面增强的地方进行标注：
```
public class RealBillingService implements BillingService {

  // 标记切面入口
  @NotOnWeekends
  public Receipt chargeOrder(PizzaOrder order, CreditCard creditCard) {
    ...
  }
}
```
实现一个切面解释器：
```
public class WeekendBlocker implements MethodInterceptor {
  public Object invoke(MethodInvocation invocation) throws Throwable {
    Calendar today = new GregorianCalendar();
    if (today.getDisplayName(DAY_OF_WEEK, LONG, ENGLISH).startsWith("S")) {
      throw new IllegalStateException(
          invocation.getMethod().getName() + " not allowed on weekends!");
    }
    return invocation.proceed();
  }
}
```
绑定切面注解和切面解释器：
```
public class NotOnWeekendsModule extends AbstractModule {
  protected void configure() {
      // 如果一个方法用切面注解标记 会先执行 WeekendBlocker 里的 invoke 方法
    bindInterceptor(Matchers.any(), Matchers.annotatedWith(NotOnWeekends.class),
        new WeekendBlocker());
  }
}
```