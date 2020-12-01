package leetcode_111;

import common.TreeNode;

/**
 * Main
 * https://leetcode-cn.com/problems/minimum-depth-of-binary-tree/
 * @author damon lin
 * 2020/11/22
 */
public class Main {

    public static void main(String[] args) {
        TreeNode root = new TreeNode(2);
        TreeNode r1 = new TreeNode(3);
        TreeNode r2 = new TreeNode(4);
        TreeNode r3 = new TreeNode(5);
        TreeNode r4 = new TreeNode(6);
        root.right = r1;
        r1.right = r2;
        r2.right = r3;
        r3.right = r4;
        System.out.println(new Solution().minDepth(root));
    }
}

/**
 * 二叉树的最小深度
 * dfs 同时记录树的深度
 */
class Solution {
    public int minDepth(TreeNode root) {
       return dfs(root, 0);
    }

    private int dfs(TreeNode root, int height) {
        if(root == null) {
            return height;
        }
        height += 1; // 访问当前节点加一
        if(root.left == null && root.right == null) {  // 如果是叶子节点直接返回当前深度
            return height;
        }
        int leftHeight = dfs(root.left, height); // 左子树的深度
        int rightHeight = dfs(root.right, height); // 右子树的深度
        if(root.left != null && root.right != null) { // 不是叶子节点 取左右子树的最小深度
            return Math.min(leftHeight, rightHeight);
        }
        if(root.left != null) { // 只存在左子树 返回左子树的高度
            return leftHeight;
        } else {
            return rightHeight; // 只存在右子树 返回右子树的高度
        }
    }
}