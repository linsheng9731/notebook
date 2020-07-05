package leetcode_96;

import utils.Checker;

/**
 * 给定一个整数 n，求以 1 ... n 为节点组成的二叉搜索树有多少种？
 * https://leetcode-cn.com/problems/unique-binary-search-trees/
 *  令 dp[i] 为长度为 i 的序列能构建的最多搜索二叉树个数
 *  很容易得到 dp[0]=1 dp[1]=1
 *  令 g[j] 为以 j 为根构建的搜索二叉树个数
 *  那么 dp[i] = sum(g[j]) 0<=j<i
 *  g[j] = dp[j] * dp[i-j]
 *  那么 dp[i] = sum(dp[j]*dp[i-j])
 *  以 n = 3 为例，[1,2,3]
 *  dp[0]*dp[2] + dp[1]*dp[1] + dp[2]*dp[0] = 2 + 1 + 2 = 5
 *  dp[2] = dp[0]*dp[1] = dp[1]*dp[0]
 */
public class Main {

    public static void main(String[] args) {
        Solution ans = new Solution();
        Checker<Integer> checker = new Checker<Integer>();
        checker.check("case1", ans.numTrees(3), 5);
    }
}

class Solution {
    public int numTrees(int n) {
        int[] dp = new int[n+1];
        if(n<2) {
            return 1;
        }
        dp[0] = 1;
        dp[1] = 1;
        for(int i=2;i<n+1;i++) {
            for(int j=0;j<i;j++) {
                // n=3
                // dp[2] = dp[0]*dp[1] + dp[1]*dp[0] = 2
                // dp[3] = dp[0]*dp[2] + dp[1]*dp[1] + dp[2]*dp[0] = 5
                dp[i] += dp[j]*dp[i-j-1];
            }
        }
        return dp[n];
    }
}
