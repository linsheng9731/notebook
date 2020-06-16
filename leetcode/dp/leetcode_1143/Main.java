package leetcode_1143;

import utils.Checker;

/**
 * https://leetcode-cn.com/problems/longest-common-subsequence/
 dp[i][j] 表示 text1 子串 i 和 text2 子串 j 之间最长公共子序列
 dp table:
     a,  ad,    ade,  adec,  adece
 a   1    1      1      1      1
 ac  1    1      1      2      2
 ace 1    1      2      2      3

 **/
public class Main {

    public static void main(String[] args) {
        Solution ans = new Solution();
        Checker<Integer> checker = new Checker<Integer>();
        checker.check("case1", ans.longestCommonSubsequence("abcde", "ace"), 3);
        checker.check("case2", ans.longestCommonSubsequence("abc", "abc"), 3);
        checker.check("case3", ans.longestCommonSubsequence("aaa", "aaaa"), 3);
        checker.check("case4", ans.longestCommonSubsequence("cdefg", "defg"), 4);
    }
}


class Solution {
    public int longestCommonSubsequence(String text1, String text2) {
        int len1 = text1.length(); // 竖的
        int len2 = text2.length(); // 横的
        int[][] dp = new int[len1+1][len2+1]; //多一层 默认都是 0
        if(len1<1 || len2 < 1) {
            return 0;
        }
        for(int i=1;i<=len1;i++) {
            for(int j=1;j<=len2;j++){
                if(text1.charAt(i-1) == text2.charAt(j-1)){
                    dp[i][j] = dp[i-1][j-1] +1; // dp[1][1] = dp[0][0] + 1
                } else {
                    dp[i][j] = Math.max(dp[i-1][j], dp[i][j-1]);
                }
            }
        }

        return dp[len1][len2];
    }
}
