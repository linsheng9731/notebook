package offer_34;

import common.Helper;
import common.TreeNode;

import java.util.LinkedList;
import java.util.List;

/**
 * Main
 * https://leetcode-cn.com/problems/er-cha-shu-zhong-he-wei-mou-yi-zhi-de-lu-jing-lcof/
 * @author damon lin
 * 2020/11/22
 */
public class Main {

    public static void main(String[] args) {

        TreeNode root = Helper.initTree();
        List<List<Integer>> lists = new Solution().pathSum(root, 7);
        for (int i = 0; i < lists.size(); i++) {
            List<Integer> integers = lists.get(i);
            for (int j = 0; j < integers.size(); j++) {
                System.out.println(integers.get(j));
            }
        }
    }
}

/**
 * 二叉树中和为某一值的路径
 * dfs 深度优先遍历 使用全局变量记录路径和最终结果
 * todo: 切记：函数传递对象是对象引用的拷贝，对于数组所有调用都是同一个 并不会重新生成一个对象 !!!
 */
class Solution {

    List<List<Integer>> res = new LinkedList<>();
    LinkedList<Integer> path = new LinkedList<>();

    public List<List<Integer>> pathSum(TreeNode root, int sum) {
        dfs(root, 0, sum);
        return res;
    }

    private void dfs(TreeNode root, int current, int target) {
        if(root == null) { // 不在这里判断 否则会重复添加
            return;
        }
        path.addLast(root.val);  // 加入当前节点
        current += root.val;
        // 是叶子节点 且和满足条件
        if(current == target && root.left == null && root.right == null) {
            LinkedList clone =(LinkedList)path.clone();
            res.add(clone);
            return;
        }
        dfs(root.left, current, target);
        dfs(root.right, current, target);
        path.removeLast(); // 在路径里去掉当前节点 并返回到上一层
    }
}