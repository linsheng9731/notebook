package leetcode_875;

/**
 * https://leetcode-cn.com/problems/koko-eating-bananas/
 */
public class Main {

    public static void main(String[] args) {
        Solution s = new Solution();
        int[] inputs = new int[]{3,6,7,11};
        print(s.minEatingSpeed(inputs, 8) == 4);
    }

    private static void print(Object any) {
        System.out.println(any);
    }
}

class Solution {
    public int minEatingSpeed(int[] piles, int H) {
        // find max time
        int max = 0;
        for(int i=0;i<piles.length;i++) {
            max = Math.max(max, piles[i]);
        }
        // 二分搜索 1 to max
        int left = 1;
        int right = max + 1 ; // 这里加一 下面是小于号
        while(left < right) { // 右边是闭区间 左边是开区间
            int mid = (left + right) / 2;
            int totalTime = 0;
            for(int i=0; i< piles.length; i++) {
                int pileSize = piles[i];
                int time = calTime(mid, pileSize);
                totalTime += time;
            }
            if(totalTime <= H) { // 速度过快 降速
                right = mid;
            } else {
                left = mid + 1; // 提速
            }
        }
        return left;

    }

    private int calTime(int speed, int total) {
        // 整除的结果加上余数
        return (total / speed) + (total % speed > 0 ? 1:0);
    }
}
