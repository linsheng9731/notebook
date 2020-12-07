package lcp_18;

import java.util.Arrays;

/**
 * Main
 *
 * @author damon lin
 * 2020/12/2
 */
public class Main {

    public static void main(String[] args) {

    }
}

class Solution {
    public int breakfastNumber(int[] staple, int[] drinks, int x) {
        Arrays.sort(staple);
        Arrays.sort(drinks);
        int ret = 0;
        for (int i = staple.length; i >=0; i--) {
            for (int j = 0; j < drinks.length; j++) {
                if(staple[i] + drinks[j] < x) {
                    ret+= 1;
                    ret %= 1000000007;
                } else {
                    break;
                }
            }
        }
        return ret;
    }
}