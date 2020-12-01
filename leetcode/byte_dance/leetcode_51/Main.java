package leetcode_51;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Main
 * https://leetcode-cn.com/problems/n-queens/
 * @author damon lin
 * 2020/11/25
 */
public class Main {

    public static void main(String[] args) {
        List<List<String>> lists = new Solution().solveNQueens(4);
        for (int i = 0; i < lists.size(); i++) {
            for (int j = 0; j < lists.get(i).size(); j++) {
                System.out.println(lists.get(i).get(j));
            }
            System.out.println();
        }
    }
}

/**
 * N 皇后
 * 回溯遍历 如果合法则向下一层遍历 如果没有合适的接直接返回到上级
 */
class Solution {

    int total = 0; // 总共有几层
    List<List<String>> ans = new LinkedList<>();

    public List<List<String>> solveNQueens(int n) {
        String[][] chess = new String[n][n];
        total = n;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                chess[i][j] = ".";
            }
        }
        search(chess, 0);
        return ans;
    }

    public void search(String[][] chess, int n) {
        if(n == total) {
            List<String> res = new ArrayList<>();
            for (int i = 0; i < chess.length; i++) {
                String[] chess1 = chess[i];
                String s = "";
                for (int j = 0; j < chess1.length; j++) {
                    s += chess1[j];
                }
                res.add(s);
            }
            ans.add(res);
            return;
        }
        // 遍历当前层
        for (int i = 0; i < total; i++) {
            if(isValid(chess, n, i)) {
                chess[n][i] = "Q";
                search(chess, n+1);
                chess[n][i] = ".";
            }
        }
        // 遍历完后没有结果
    }

    /**
     * 检测是否有碰撞
     * 只需要检测在当前层之前的位置即可 因为下面的层还没搜索到
     */
    private boolean isValid(String[][] chess, int indexI, int indexJ) {
        for (int i = indexI; i >=0 ; i--) {
            if(chess[i][indexJ] == "Q") { // 纵向检测
                return false;
            }
        }
        for(int i = indexI, j = indexJ; i>=0 && j >=0 ; i--,j--) {
            if(chess[i][j] == "Q") {
                return false;
            }
        }

        for(int i = indexI, j = indexJ; i>=0 && j <total ; i--,j++) {
            if(chess[i][j] == "Q") {
                return false;
            }
        }
        return true;
    }
}