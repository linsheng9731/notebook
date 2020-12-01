package leetcode_378;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Stack;

/**
 * Main
 * https://leetcode-cn.com/problems/kth-smallest-element-in-a-sorted-matrix/
 * @author damon lin
 * 2020/11/24
 */
public class Main {

    public static void main(String[] args) {
//        int[][] matrix = new int[][] {
//                {1,2,3},
//                {4,5,6},
//                {10,11,11}
//        };

        int[][] matrix = new int[][] {
                {1,2},
                {1,3 }
        };
        int i = new Solution().kthSmallest(matrix, 2);
        System.out.println(i);
    }
}

/**
 * 有序矩阵中第K小的元素
 * 合并有序数组 从小到大排序 取第 k-1 为元素
 */
class Solution {
    public int kthSmallest(int[][] matrix, int k) {
        Comparator<Integer> comparator = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        };
        int n = matrix.length;
        PriorityQueue<Integer> queue = new PriorityQueue<Integer>(n*n, comparator);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                queue.add(matrix[i][j]);
            }
        }
        Stack<Integer> res = new Stack<>();
        for (int i = 0; i < k; i++) {
            res.push(queue.poll());
        }
       return res.pop();
    }
}