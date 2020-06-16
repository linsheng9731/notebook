package leetcode_1025;

import utils.Checker;

/**
 * https://leetcode-cn.com/problems/divisor-game/
 *  dp[x] x 为当前轮到的数字
 *  换个思路 让对方输即可
 *  总结一下 占到偶数即可赢
 dp[1] = false // 0 < x < 1 无法选择 输了
 dp[2] = true // 0 < x < 2 选择 1 减掉后 对方 x=1，dp[1]=false 赢了
 dp[3] = false
 dp[4] =  true
 dp[5] = false
 dp[6] = true
 */
public class Main {

    public static void main(String[] args) {

        Solution1 ans = new Solution1();
        Solution2 ans2 = new Solution2();
        Checker<Boolean> checker = new Checker<Boolean>();
        checker.check("case1", ans.divisorGame(3), false);
        checker.check("case1", ans2.divisorGame(3), false);

        checker.check("case2", ans2.divisorGame(4), true);
        checker.check("case3", ans2.divisorGame(1), false);
        checker.check("case4", ans2.divisorGame(9), false);


    }
}

class Solution1 {
    public boolean divisorGame(int N) {
        return N %2==0;
    }
}

/** dp 解法
 *  dp[x] = true
 *  当存在 step 使得 dp[x-step] = false
 *  同时 (x % step == 0)
 */
class Solution2 {

    public boolean divisorGame(int N) {
        boolean[] dp = new boolean[N+1];
        if(N==1) {
            return false;
        }
        if(N==2) {
            return true;
        }
        dp[1] = false;
        dp[2] = true;
        for(int i=3; i < N+1; i++) {
            dp[i] = false; // 先置为 false
            for(int step=1; step<i; step++) {
                if(!dp[i-step] && i % step==0) {
                    dp[i] = true;
                    break; // 找到了目标
                }
            }
        }
        return dp[N];
    }
}
