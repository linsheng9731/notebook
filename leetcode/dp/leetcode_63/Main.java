package leetcode_63;

import com.sun.tools.javac.comp.Check;
import utils.Checker;

/**
 * https://leetcode-cn.com/problems/unique-paths-ii/
 * 有障碍物表示 obstacleGrid[i][j] = 1
 * 多一层初始化为 0 可以简化判断
 * dp[0][x] = 0
 * dp[x][0] = 0
 * 如果只有一个 且不为障碍则 dp[1][1] = 1
 * for(i:1-m)
 *    for(j:1-n)
 *      if(obstacleGrid[i][j] == 1)
 *          dp[i][j] = 0
 *      else
 *          dp[i][j] = dp[i-1][j] + dp[i][j-1]
 */
public class Main {
    public static void main(String[] args) {
        Solution ans = new Solution();
        Checker<Integer> checker = new Checker<Integer>();
        int[][]input1 = {{0,0,0}, {0,1,0}, {0,0,0}};
        checker.check("case1", ans.uniquePathsWithObstacles(input1), 2);
        int[][]input2 = {{0,0,0}, {0,0,0}, {0,0,0}};
        checker.check("case2", ans.uniquePathsWithObstacles(input2), 6);
        int[][]input3 = {{1}};
        checker.check("case3", ans.uniquePathsWithObstacles(input3), 0);
    }
}

class Solution {
    public int uniquePathsWithObstacles(int[][] obstacleGrid) {
        if(obstacleGrid.length == 0) {
            return 0;
        }
        int m = obstacleGrid.length;
        int n = obstacleGrid[0].length;
        int[][] dp = new int[m+1][n+1];
        for(int i=1;i<m+1;i++) {
            for(int j=1;j<n+1;j++) {
                if(i==1 && j==1) {
                    if(obstacleGrid[i-1][j-1] == 1) {
                        dp[i][j] = 0;
                    } else {
                        dp[i][j] = 1;
                    }
                } else {
                    if(obstacleGrid[i-1][j-1] == 1){
                        dp[i][j] = 0;
                    } else {
                        dp[i][j] = dp[i-1][j] + dp[i][j-1];
                    }
                }
            }
        }
        return dp[m][n];
    }
}