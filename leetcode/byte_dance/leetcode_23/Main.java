package leetcode_23;

import common.ListNode;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Main
 * https://leetcode-cn.com/problems/merge-k-sorted-lists/submissions/
 * @author damon lin
 * 2020/11/25
 */
public class Main {
    public static void main(String[] args) {
        new Solution().mergeKLists(new ListNode[]{});
    }
}

/**
 * 合并有序链表
 * 使用优先级队列 取每个有序队列的头结点放入优先级队列
 * 每次只需要取优先级队列中的最小元素 并 更新队列 直到队列为空
 */
class Solution {
    public ListNode mergeKLists(ListNode[] lists) {
        Comparator<ListNode> comparator = new Comparator<ListNode>() {
            @Override
            public int compare(ListNode o1, ListNode o2) {
                return o1.val - o2.val;
            }
        };
        PriorityQueue<ListNode> queue = new PriorityQueue<>(comparator);
        for (int i = 0; i < lists.length; i++) {
            if(lists[i] != null) {
                queue.add(lists[i]);
            }
        }
        ListNode dummyNode = new ListNode(0);
        ListNode ptr = new ListNode(0);
        dummyNode.next = ptr;
        while(!queue.isEmpty()) {
            ListNode node = queue.poll();
            if(node.next != null) {
                queue.add(node.next);
            }
            ptr.next = node;
            ptr = node;
        }
        return dummyNode.next.next;
    }
}
