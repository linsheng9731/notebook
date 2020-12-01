package leetcode_72;

import utils.Checker;

/**
 * https://leetcode-cn.com/problems/edit-distance/submissions/
 * 二维 dp
 * dp[i][j] 代币字符串1子串 text1[i] 和 字符串2  text2[j] 的编辑距离
 * 如果两个字符相等，那么不需要编辑，取 dp[i-1][j] 的编辑距离即可。
 * 如果不相等，取 dp[i-1][j]（添加操作）,dp[i][j-1]（删除操作）,dp[i-1][j-1]（替换操作） 最小值加一即可。
 * if(word1.charAt(i-1) == word2.charAt(j-1))
 *           dp[i][j] = dp[i-1][j-1]
 *      else
 *           dp[i][j] = Math.min(Math.min(dp[i-1][j], dp[i][j-1]), dp[i-1][j-1]) + 1
 *
 */
public class Main {

    public static void main(String[] args) {

        Solution ans = new Solution();
        Checker<Integer> checker = new Checker<>();
        checker.check("case1", ans.minDistance("horse", "ros"), 3);
        checker.check("case2", ans.minDistance("intention", "execution"), 5);
        checker.check("case3", ans.minDistance("", "ros"), 3);
        checker.check("case4", ans.minDistance("", ""), 0);

    }
}

class Solution {
    public int minDistance(String word1, String word2) {
        int len1 = word1.length();
        int len2 = word2.length();
        int[][] dp = new int[len1+1][len2+1];
        for(int i=0;i<=len1;i++) {
            dp[i][0] = i;
        }
        for(int j=0;j<=len2;j++) {
            dp[0][j] = j;
        }
        for(int i=1;i<=len1;i++) {
            for(int j=1;j<=len2;j++) {
                if(word1.charAt(i-1) == word2.charAt(j-1)){
                    dp[i][j] = dp[i-1][j-1];
                } else {
                    dp[i][j] = Math.min(Math.min(dp[i-1][j], dp[i][j-1]), dp[i-1][j-1]) + 1;
                }
            }
        }
        return dp[len1][len2];
    }
}