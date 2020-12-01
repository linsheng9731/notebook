package LRUCache;

public class Main {

    public static void main(String[] args) {
        LRUCache cache = new LRUCache( 2 /* 缓存容量 */ );

        cache.put(1, 1);
        cache.put(2, 2);
        print(cache.get(1) == 1);       // 返回  1
        cache.put(3, 3);    // 该操作会使得关键字 2 作废
        print(cache.get(2) == -1);       // 返回 -1 (未找到)
        cache.put(4, 4);    // 该操作会使得关键字 1 作废
        print(cache.get(1) == -1);       // 返回 -1 (未找到)
        print(cache.get(3) == 3);       // 返回  3
        print(cache.get(4) == 4);       // 返回  4
    }

    private static void print(Object input) {
        System.out.println(input);
    }
}
