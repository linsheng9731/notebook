package gc;

import java.lang.ref.WeakReference;

public class WeakReferenceTest {
    public static void main(String[] args) {
        Salad salad = new Salad(new Apple()); // 这里不能先赋值给变量 会产生强引用!
        System.out.println(salad.get());
        System.gc();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(salad.get() == null);
    }
}

class Apple {
    public Apple() {

    }
}

class Salad extends WeakReference<Apple> {
    public  Salad(Apple ap) {
        super(ap);
    }
}