package offer_32;

import common.TreeNode;

import java.util.LinkedList;
import java.util.List;

/**
 * Main
 * https://leetcode-cn.com/problems/cong-shang-dao-xia-da-yin-er-cha-shu-iii-lcof/
 * @author damon lin
 * 2020/11/22
 */
public class Main {
    public static void main(String[] args) {

    }
}

/**
 * 从上到下按 之 字形打印二叉树
 * 即先从左到右 然后从右到左 依次类推
 */
class Solution {
    public List<List<Integer>> levelOrder(TreeNode root) {
        if(root == null) {
            return new LinkedList<>();
        }
        LinkedList<TreeNode> queue = new LinkedList<>();
        List<List<Integer>> ret = new LinkedList<>();
        queue.add(root);
        boolean leftDirection = false; // 方向标志位 true 从左到右 false 从右到左
        while (!queue.isEmpty()) {
            int size = queue.size();
            LinkedList<Integer> levelRet = new LinkedList<>();
            for(int i=0;i< size; i++) {
                TreeNode node = queue.pop();
                if(leftDirection) {
                    levelRet.addFirst(node.val);
                } else {
                    levelRet.addLast(node.val);
                }
                // 如果不是 null 则加入队列
                if(node.left != null) {
                    queue.add(node.left);
                }
                if(node.right != null) {
                    queue.add(node.right);
                }
            }
            leftDirection = !leftDirection; // 翻转方向
            ret.add(levelRet);
        }
        return ret;
    }
}
