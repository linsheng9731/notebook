package test;

/**
 * Main
 *
 * @author damon lin
 * 2020/11/26
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int a = in.nextInt();
        int ans = new Solution().nextInt(a);
        System.out.println(ans);
    }


}

class Solution {

    public int nextInt(int a) {
        List<Integer> nums = new ArrayList<>();
        while(a>10) {
            nums.add(a%10);
            a/=10;
        }
        for (int i = nums.size(); i > 1; i--) {
            if(nums.get(i) > nums.get(i-1)) {
                int tmp = nums.get(i);
                nums.set(i, nums.get(i-1));
                nums.set(i-1, tmp);
                return serialize(nums);
            }
        }
        return -1;
    }

    private int serialize(List<Integer> nums) {
        for (int i = 0; i < nums.size(); i++) {

        }
    }
}


