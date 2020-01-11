package leetcode_452;

import utils.Checker;

import java.util.Arrays;

/**
 * https://leetcode-cn.com/problems/minimum-number-of-arrows-to-burst-balloons/
 * 贪心思想
 * 先将气球直径抽象成线段 按照线段终点从小到大排序
 * 如果需要扎破第一个球 则飞镖最多可以移到第一个球的终点位置 即 points[0][1] 位置
 * 因为是按照终点排序的 所以其他球的终点都在这个位置的右侧
 * 只需要计算扎破第一个球顺便能扎破几个球 即跳过几个球
 * 到无法扎破的时候停止 飞镖个数加一 之后的重复上述逻辑
 */
public class Main {

    public static void main(String[] args) {
        int[][]input = new int[][]{{1,2}};
        int expect = 1;
        int ans = new Solution().findMinArrowShots(input);
        new Checker<Integer>().check("case1", ans, expect);

        input = new int[][]{{10,16}, {2,8}, {1,6}, {7,12}};
        expect = 2;
        ans = new Solution().findMinArrowShots(input);
        new Checker<Integer>().check("case2",  ans, expect);

        input = new int[][]{{1,2}, {2,3}, {3,4}};
        expect = 2;
        ans = new Solution().findMinArrowShots(input);
        new Checker<Integer>().check("case3", ans, expect);

        input = new int[][]{{-1,2}, {1,2}};
        expect = 1;
        ans = new Solution().findMinArrowShots(input);
        new Checker<Integer>().check("case4", ans, expect);

    }
}


class Solution {
    public int findMinArrowShots(int[][] points) {
        if(points.length == 0) return 0;
        int ans = 1;
        Arrays.sort(points, (a, b) -> a[1] - b[1]);
        int currentPos = points[0][1];
        for(int i = 1; i< points.length; i++) {
            if(currentPos >= points[i][0]){
                continue;
            }
            ans++;
            currentPos = points[i][1];
        }
        return  ans;
    }
}