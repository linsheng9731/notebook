package leetcode_62;

import utils.Checker;

/**
 * https://leetcode-cn.com/problems/unique-paths/
 * 只能往右或者往下走，所以当前路径来自左边或者上面。即当前路径的走法：
 * dp[i][j] = dp[i-1][j] + dp[i][j-1]
 * 这里在原有的方格基础上多一层方格，方便推理，初始化第一个点 dp[1][1] = 1。
 */
public class Main {

    public static void main(String[] args) {
        Solution ans = new Solution();
        Checker<Integer> checker = new Checker<Integer>();
        checker.check("case1", ans.uniquePaths(3,2), 3);
        checker.check("case2", ans.uniquePaths(7,3), 28);

    }
}


class Solution {
    public int uniquePaths(int m, int n) {
        int[][] dp = new int[m+1][n+1];
        for(int i=1;i<m+1;i++) {
            for(int j=1;j<n+1;j++) {
                if(i==1&&j==1) {
                    dp[i][j] = 1;
                } else {
                    dp[i][j] = dp[i-1][j] + dp[i][j-1];
                }
            }
        }
        return dp[m][n];
    }
}

