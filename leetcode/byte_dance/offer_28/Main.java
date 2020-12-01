package offer_28;

import common.TreeNode;

/**
 * Main
 * https://leetcode-cn.com/problems/dui-cheng-de-er-cha-shu-lcof/
 * @author damon lin
 * 2020/11/22
 */
public class Main {

    public static void main(String[] args) {
        TreeNode root = new TreeNode(1);
        TreeNode l1 = new TreeNode(2);
        TreeNode r1 = new TreeNode(2);
        TreeNode l2 = new TreeNode(3);
        TreeNode r2 = new TreeNode(4);
        TreeNode l22 = new TreeNode(4);
        TreeNode r22 = new TreeNode(3);
        TreeNode l3 = new TreeNode(5);
        TreeNode r3 = new TreeNode(5);
        TreeNode l33 = new TreeNode(7);
        TreeNode r33 = new TreeNode(6);
        TreeNode l333 = new TreeNode(6);
        TreeNode r333 = new TreeNode(7);
        TreeNode l3333 = new TreeNode(5);
        TreeNode r3333 = new TreeNode(5);

        root.left = l1;
        root.right = r1;
        l1.left = l2;
        l1.right = r2;
        r1.left = l22;
        r1.right = r22;
        l2.left = l3;
        l2.right = r3;
        r2.left = l33;
        r2.right = r33;
        l22.left = l333;
        l22.right = r333;
        r22.left = l3333;
        r22.right = r3333;

        boolean symmetric = new Solution().isSymmetric(root);
        System.out.println(symmetric);
    }
}

/**
 * 对称的二叉树
 * 对称二叉树的任意对称节点值相等
 * 且 左节点的左子节点 == 右节点的右子节点相等
 * 且 左节点的右子节点 == 右节点的左子节点相等
 */
class Solution {
    public boolean isSymmetric(TreeNode root) {
        if(root == null) {
            return true;
        }
        return recur(root.left, root.right);
    }

    private boolean recur(TreeNode left, TreeNode right) {
        if(left == null && right == null) {
            return true;
        }
        if(left == null || right == null || left.val != right.val) {
            return false;
        }
        return recur(left.left, right.right) && recur(left.right, right.left);
    }
}