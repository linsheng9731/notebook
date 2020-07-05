package leetcode_120;

import utils.Checker;
import java.util.Arrays;
import java.util.List;

/**
 * https://leetcode-cn.com/problems/triangle/
 * 定义 dp[i][j] 为第 i 行，第 j 列最小路径和
 * dp[i][j] = min(dp[i-1][j], dp[i-1][j-1])
 * dp[0][0] = triangle[0][0]
 * 多定义一列 简化判断
 *
 */
public class Main {

    public static void main(String[] args) {
        Solution ans = new Solution();
        Checker<Integer> checker= new Checker<Integer>();
        List<List<Integer>> input1 = Arrays.asList(Arrays.asList(2));
        checker.check("case1", ans.minimumTotal(input1), 2);

        List<List<Integer>> input2 = Arrays.asList(Arrays.asList(2), Arrays.asList(2,3));
        checker.check("case2", ans.minimumTotal(input2), 4);

        List<List<Integer>> input3 = Arrays.asList(Arrays.asList());
        checker.check("case3", ans.minimumTotal(input3), 0);

        List<List<Integer>> input4 = Arrays.asList(Arrays.asList(2), Arrays.asList(3,4), Arrays.asList(6,5,7), Arrays.asList(4,1,8,3));
        checker.check("case4", ans.minimumTotal(input4), 11);
    }
}

class Solution {
    public int minimumTotal(List<List<Integer>> triangle) {
        if(triangle.size()==0 || triangle.get(0).size() == 0) {
            return 0;
        }
        int m = triangle.size(); // 有多少行
        int n = triangle.get(m-1).size(); // 最长的一行
        int[][] dp = new int[m][n+1]; // 多定义一列 简化判断
        // 初始化为最大值
        for(int i=0;i<m;i++) {
            for(int j=0;j<n+1;j++) {
                dp[i][j] = Integer.MAX_VALUE;
            }
        }
        dp[0][1] = triangle.get(0).get(0);
        for(int i=1;i<m;i++) {
            // 遍历到当前行最长的位置
            for(int j=1;j<triangle.get(i).size()+1;j++) {
                // 取上一层里最小的路径和 + 当前路径
                dp[i][j] = Math.min(dp[i-1][j], dp[i-1][j-1]) + triangle.get(i).get(j-1);
            }
        }
        int ans = Integer.MAX_VALUE;
        for(int j=1;j<n+1;j++) {
            ans = Math.min(dp[m-1][j], ans);
        }
        return ans;
    }
}