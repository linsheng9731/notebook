package leetcode_102;

import common.TreeNode;

import java.util.LinkedList;
import java.util.List;

/**
 * Main
 * https://leetcode-cn.com/problems/binary-tree-level-order-traversal/
 * @author damon lin
 * 2020/11/22
 */
public class Main {

    public static void main(String[] args) {
        TreeNode root = new TreeNode(2);
        TreeNode l1 = new TreeNode(3);
        TreeNode r1 = new TreeNode(4);
        TreeNode l2 = new TreeNode(5);
        root.left = l1;
        root.right = r1;
        l1.left = l2;
        List<List<Integer>> lists = new Solution().levelOrder(root);
        for (int i = 0; i < lists.size(); i++) {
            List<Integer> integers = lists.get(i);
            for (int j = 0; j < integers.size(); j++) {
                System.out.print(integers.get(j));
                System.out.print(" ");
            }
            System.out.println();
        }

    }
}

/**
 *  二叉树的层序遍历
 */
class Solution {
    public List<List<Integer>> levelOrder(TreeNode root) {
        if(root == null) {
            return new LinkedList<>();
        }
        LinkedList<TreeNode> queue = new LinkedList<>(); // 使用一个容器存放所有下层节点
        queue.add(root);
        List<List<Integer>> ret = new LinkedList<List<Integer>>();  // 存储所有层
        while(!queue.isEmpty()) {
            int size = queue.size();
            List<Integer> levelRet = new LinkedList<>(); // 存储当前层的结果
            for(int i=0; i< size; i++) {
                TreeNode node =  queue.pop(); // 访问容器中的第一个节点并移除
                levelRet.add(node.val);
                if(node.left != null) {
                    queue.add(node.left);
                }
                if(node.right != null) {
                    queue.add(node.right);
                }
            }
            ret.add(levelRet);
        }
        return ret;
    }
}