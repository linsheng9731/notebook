package leetcode_605;

import utils.Checker;

/**
 * https://leetcode.com/problems/can-place-flowers/
 * 找到连续三个 0 即一个花的位置 在原有的数据的首尾加两个 0
 */
public class Main {

    public static void main(String[] args) {
        int[]input = new int[]{1,0,0,0,1};
        boolean expect = true;
        int n = 1;
        boolean ans = new leetcode_605.Solution().canPlaceFlowers(input, n);
        new Checker<Boolean>().check("case1", ans, expect);

        input = new int[]{1,0,0,0,1};
        expect = false;
        n = 2;
        ans = new leetcode_605.Solution().canPlaceFlowers(input, n);
        new Checker<Boolean>().check("case2", ans, expect);


        input = new int[]{0,0,0,1};
        expect = true;
        n = 1;
        ans = new leetcode_605.Solution().canPlaceFlowers(input, n);
        new Checker<Boolean>().check("case3", ans, expect);

        input = new int[]{0,0,1,0,0,1,0,0};
        expect = true;
        n = 2;
        ans = new leetcode_605.Solution().canPlaceFlowers(input, n);
        new Checker<Boolean>().check("case4", ans, expect);

        input = new int[]{1,0,0,0,0,1};
        expect = false;
        n = 2;
        ans = new leetcode_605.Solution().canPlaceFlowers(input, n);
        new Checker<Boolean>().check("case5", ans, expect);

        input = new int[]{1,0,0,0,0,0,1};
        expect = true;
        n = 2;
        ans = new leetcode_605.Solution().canPlaceFlowers(input, n);
        new Checker<Boolean>().check("case5", ans, expect);


    }
}

class Solution {
    public boolean canPlaceFlowers(int[] flowerbed, int n) {
        int cnt = 0;
        int canPlaceCnt = 0;
        int total = flowerbed.length;
        int[] newLine = new int[flowerbed.length+2];
        int newLineLen = newLine.length;
        newLine[0] =0 ;
        newLine[newLineLen - 1] = 0;
        for(int i=0; i < total; i++) {
            newLine[i+1] = flowerbed[i];
        }
        for(int i=0; i < newLineLen; i++) {
            if(newLine[i] == 0) cnt++; // 每次找到一个空位计数加一
            if(newLine[i] == 1) cnt = 0; // 碰到已经有花的情况清空计数
            if(cnt == 3) {
                canPlaceCnt++; // 找到一个位置
                cnt = 0; // 重新计数
                i--; // 回退一位 覆盖 00001 这种情况
            }
        }
        return canPlaceCnt >= n;
    }
}