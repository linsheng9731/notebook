package leetcode_605;

import utils.Checker;

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
            if(newLine[i] == 0) cnt++;
            if(newLine[i] ==1) cnt = 0;
            if(cnt == 3) {
                canPlaceCnt++;
                cnt = 0;
                i--;
            }
        }
        return canPlaceCnt >= n;
    }
}