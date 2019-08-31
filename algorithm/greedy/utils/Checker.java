package utils;

public class Checker<T> {

    public void check (String name,  T result, T expect) {
        if(result != expect) {
            System.out.println(name + " find error case output:" + result + " expect:" + expect);
        } else {
            System.out.println(name + " pass.");
        }
    }
}
