package offer_54;

import common.TreeNode;

import java.util.LinkedList;
import java.util.List;

/**
 * Main
 * https://leetcode-cn.com/problems/er-cha-sou-suo-shu-de-di-kda-jie-dian-lcof/
 * @author damon lin
 * 2020/11/22
 */
public class Main {

    public static void main(String[] args) {

    }
}

/**
 * 二叉搜索树的第k大节点
 */
class Solution {

    List<Integer> res = new LinkedList<>();

    public int kthLargest(TreeNode root, int k) {
        dfs(root);
        int size = res.size();
        int index = size - k;
        return res.get(index);
    }

    private void dfs(TreeNode root) {
        if(root == null) {
            return ;
        }
        dfs(root.left);
        res.add(root.val);
        dfs(root.right);
    }
}