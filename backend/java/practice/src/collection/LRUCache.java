package collection;

import java.util.HashMap;
import java.util.LinkedList;

public class LRUCache {
    int cap;
    int count = 0;
    LinkedList<Node> list = new LinkedList<>();
    HashMap<Integer, Node> map = new HashMap<Integer, Node>();

    public LRUCache(int capacity) {
        this.cap = capacity;
    }

    public void put(int key, int value) {
        Node tryNode = map.get(key);
        if(tryNode != null) {
            tryNode.setValue(value); // 最近被访问过
            makeRecently(tryNode);
            return;
        }
        if (count + 1 > cap) { // 容量不够淘汰末尾元素
            Node last = list.removeLast();
            map.remove(last.key);
            count--;
        }
        Node n = new Node(key, value);
        map.put(key, n);
        // 链表头部是最近访问的元素
        list.addFirst(n);
        count++;
    }

    public int get(int key) {
        Node node = map.get(key);
        if (node == null) {
            return -1;
        }
        // 将最近访问过的数据放到链表头部
        makeRecently(node);
        return node.value;
    }

    private void makeRecently(Node node) {
        if(list.size() > 1) {
            list.remove(node);
            list.addFirst(node);
        }

    }

    class Node {
        int key;
        int value;
        Node pre = null;
        Node next = null;

        Node(int key, int value) {
            this.key = key;
            this.value = value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public Node getNext() {
            return next;
        }

        public Node getPre() { return pre; }

        public void setPre(Node pre) {
            this.pre = pre;
        }

        public void setNext(Node next) {
            this.next = next;
        }
    }
}
