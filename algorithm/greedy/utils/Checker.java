package utils;

public class Checker<T> {

    public void check (String name,  T ans, T target) {
        if(ans != target) {
            System.out.println(name + " find error case output:" + ans + " expect:" + target);
        }
    }
}
