package leetcode_122;

import utils.Checker;

/**
 * https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock-ii/description
 * 贪心思想 局部最优解 = 当前价格 - 上一次价格 > 0 即可获得利润
 * 全局最优解 = 所有局部最优解累加
 */
public class Main {
    public static void main(String[] args) {
       int[] prices = new int[]{7,1,5,3,6,4};
       int ans = 7;
       Checker<Integer> checker = new Checker<Integer>();
       checker.check("case1", new Solution().maxProfit(prices), ans);

       prices = new int[]{7};
       ans = 0;
       checker.check("case2", new Solution().maxProfit(prices), ans);

        prices = new int[]{1,2,3,4,5};
        ans = 4;
        checker.check("case3", new Solution().maxProfit(prices), ans);

        prices = new int[]{5,4,3,2,1};
        ans = 0;
        checker.check("case4", new Solution().maxProfit(prices), ans);


    }
}

class Solution {
    public int maxProfit(int[] prices) {
        int profit = 0;
        for(int i = 1; i< prices.length; i++) {
            if (prices[i] > prices[i-1]) {
                profit += prices[i] - prices[i-1];
                continue;
            }
        }
        return profit;
    }
}