package offer_27;

import common.Helper;
import common.TreeNode;

/**
 * Main
 * https://leetcode-cn.com/problems/er-cha-shu-de-jing-xiang-lcof/
 * @author damon lin
 * 2020/11/22
 */
public class Main {

    public static void main(String[] args) {
        TreeNode root = Helper.initTree();
        TreeNode treeNode = new Solution().mirrorTree(root);
        Helper.traversePrint(treeNode);
    }
}

/**
 * 二叉树的镜像
 * 递归交换左右节点
 */
class Solution {
    public TreeNode mirrorTree(TreeNode root) {
        if(root == null) {
            return null;
        }
        TreeNode tmp = root.left;
        root.left = root.right;
        root.right = tmp;
        mirrorTree(root.left);
        mirrorTree(root.right);
        return root;
    }
}