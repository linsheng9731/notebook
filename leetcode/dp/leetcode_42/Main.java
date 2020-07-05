package leetcode_42;

import utils.Checker;

/**
 * https://leetcode-cn.com/problems/lian-xu-zi-shu-zu-de-zui-da-he-lcof/
 */
public class Main {
    public static void main(String[] args) {
        Solution ans = new Solution();
        SolutionDp ansDp = new SolutionDp();
        Checker<Integer> checker = new Checker<>();
        checker.check("case1", ans.maxSubArray(new int[]{-2,1,-3,4,-1,2,1,-5,4}), 6);
        checker.check("case2", ans.maxSubArray(new int[]{-2,-1,-3}), -1);
        checker.check("case3", ans.maxSubArray(new int[]{2,-1,-3}), 2);
        checker.check("case4", ans.maxSubArray(new int[]{}), 0);

        checker.check("case1_", ansDp.maxSubArray(new int[]{-2,1,-3,4,-1,2,1,-5,4}), 6);
        checker.check("case2_", ansDp.maxSubArray(new int[]{-2,-1,-3}), -1);
        checker.check("case3_", ansDp.maxSubArray(new int[]{2,-1,-3}), 2);
        checker.check("case4_", ansDp.maxSubArray(new int[]{}), 0);
    }
}

/**
 *  动态规划解法: 令 dp[i] 为以元素 i 结尾的最大和子数组
 *  注意：这里判断的不是 nums[i]，因为是以元素 i 结尾的，所以无论如何 nums[i] 都要被包含进来。
 *  变化的部分是 dp[i-1]，因为是连续子数组，如果元素 i 要和前面的元素连接成最大和子数组，
 *  dp[i-1] 必须大于 0，否则就不能接成最大和子数组。
 *
 *  if(dp[i-1] > 0)
 *      dp[i] = dp[i-1] + nums[i]
 *  else
 *      dp[i] = nums[i]
 *
 */
class SolutionDp {
    public int maxSubArray(int[] nums) {
        int len = nums.length;
        if(len == 0) {
            return 0;
        }
        int[] dp = new int[len];
        dp[0] = nums[0];
        int ans = Math.max(dp[0], Integer.MIN_VALUE);
        for(int i=1;i<len;i++) {
            if(dp[i-1] > 0) {
                dp[i] = dp[i-1] + nums[i];
            } else {
                dp[i] = nums[i];
            }
            ans = Math.max(ans, dp[i]);
        }
        return ans;
    }
}

/**
 * 贪心解法：寻找第一个大于0的元素，开始累加得到和，如果和大于0，继续累加直到和小于0。
 * 寻找第一个大于0的元素重新开始累加。
 * 定义 ans = 0;
 * sum += nums[i]
 * if(sum >0 )
 *  ans = max(ans, sum)
 */
class Solution {
    public int maxSubArray(int[] nums) {
        if(nums.length == 0) {
            return 0;
        }
        int ans = Integer.MIN_VALUE;
        int sum = 0;
        for(int i=0;i<nums.length;i++) {
            if(sum + nums[i] > 0) { // 如果和大于 0 累加
                sum += nums[i];
                ans = Math.max(ans, sum); // 更新最大值
            } else {
                sum = 0; // 重新开始
            }
        }
        // 没有大于0的元素
        if(ans == Integer.MIN_VALUE) {
            for(int i=0;i<nums.length;i++) {
                ans = Math.max(ans, nums[i]);
            }
        }
        return ans;
    }
}