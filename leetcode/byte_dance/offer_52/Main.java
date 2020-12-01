package offer_52;

import common.ListNode;

/**
 * Main
 *
 * @author damon lin
 * 2020/11/22
 */
public class Main {

    public static void main(String[] args) {

    }
}


/**
 * 双指针法 两个指针遍历到末尾 然后交换到另一个链表的头
 * 第一个相等的节点就是第一个相交的节点
 */
class Solution {
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        ListNode node1  = headA;
        ListNode node2 = headB;
        while(node1 != node2) { // 如果相交 第一个交点会退出 如果没有相交 两个节点会同时为 null
            node1 = node1 != null ? node1.next : headB; // 这里不能用 next != null 否则不相交的情况就会陷入循环
            node2 = node2 != null ? node2.next : headA;
        }
        return node1;
    }
}