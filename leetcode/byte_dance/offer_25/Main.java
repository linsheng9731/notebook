package offer_25;

import common.ListNode;

/**
 * Main
 * https://leetcode-cn.com/problems/he-bing-liang-ge-pai-xu-de-lian-biao-lcof/
 * 剑指 Offer 25. 合并两个排序的链表
 *
 * @author damon lin
 * 2020/11/22
 */
public class Main {

    public static void main(String[] args) {
        ListNode l1 = new ListNode(1);
        ListNode n1 = new ListNode(1);
        l1.next = n1;
        ListNode n2 = new ListNode(2);
        n1.next = n2;
        ListNode n3 = new ListNode(3);
        n2.next = n3;

        ListNode l2 = new ListNode(1);
        ListNode n11 = new ListNode(3);
        l2.next = n11;
        ListNode n22 = new ListNode(4);
        n11.next = n22;
        ListNode n33 = new ListNode(4);
        n22.next = n33;

        ListNode head = (new Solution()).mergeTwoLists(l1, l2);
        ListNode head2 = (new Solution()).mergeTwoLists(null, null);
        while (head.next != null) {
            System.out.println(head.next.val);
            head = head.next;
        }
    }
}

/**
 * 合并两个有序链表为一个有序链表
 * 不断对比两条链表的节点 取小的那个作为新链表的下个节点
 * 直到两条链表都到了末尾
 */
class Solution {

    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        // 如果其中一条是空 直接返回另一个条
        if (l1 == null) {
            return l2;
        }
        if (l2 == null) {
            return l1;
        }
        // 初始化一个空节点这样就不需要特殊处理第一个节点
        ListNode current = new ListNode(-1);
        ListNode head = current;
        while (l1 != null || l2 != null) {
            // 如果 l2 空了 或者 l1 的值小于 l2 取 l1 的节点
            if ((l2 == null) || (l1 != null && l1.val < l2.val)) {
                current.next = l1;
                l1 = l1.next;
            } else {
                current.next = l2;
                l2 = l2.next;
            }
            current = current.next;
        }
        return head;
    }

}
