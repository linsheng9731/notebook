package leetcode_300;

import utils.Checker;

/**
 * https://leetcode-cn.com/problems/longest-increasing-subsequence/
 * 定义 dp[i] 为以元素 i 为结尾的最长上升子序列长度
 * dp[0] = 1
 * for(int i=0;i<j;i++)
 *  if(a[j] > a[i])
 *      dp[j] = max(dp[i]+1, dp[j])
 *
 * for(int i=0;i<m;i++)
 *    ans = max(ans, dp[i])
 */
public class Main {

    public static void main(String[] args) {
        Solution ans = new Solution();
        Checker<Integer> checker = new Checker<>();
        checker.check("case1", ans.lengthOfLIS(new int[]{10,9,2,5,3,7,101,18}), 4);
        checker.check("case2", ans.lengthOfLIS(new int[]{}), 0);
        checker.check("case3", ans.lengthOfLIS(new int[]{10,9,2,5,3,4}), 3);
    }
}

class Solution {
    public int lengthOfLIS(int[] nums) {
        int len = nums.length;
        if(len == 0 ) {
            return 0;
        }
        // 定义 dp[i] 为以元素 i 为结尾的最长上升子序列长度
        int[]dp = new int[len];
        for(int i=0;i<len;i++) {
            dp[i] = 1;
        }
        for(int i=1;i<len;i++) {
            for(int j=0;j<i;j++) {
                // 如果当前元素大于 num[j]
                // 那么以 j 为结尾的最长上升序列
                // 加上当前元素构成新的上升子序列
                if(nums[i] > nums[j]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
        }
        int ans = 0;
        for(int i=0;i<len;i++) {
            ans = Math.max(ans, dp[i]);
        }
        return ans;
    }
}
