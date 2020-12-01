package dp;

import java.util.Scanner;


public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Solution ans = new Solution();
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] nums = line.split(" ");
            int[] input = new int[nums.length];
            for (int i = 0; i < nums.length; i++) {
                input[i] = Integer.parseInt(nums[i]);
            }
            int out = ans.lengthOfLIS(input);
            System.out.print(out + "\n");
        }

    }

}

class Solution {
    public int lengthOfLIS(int[] nums) {
        if (nums.length == 0) {
            return 0;
        }
        int[] dp = new int[nums.length];
        dp[0] = 1;
        int maxans = 1;
        for (int i = 1; i < dp.length; i++) {
            int maxval = 0;
            for (int j = 0; j < i; j++) {
                if (nums[i] > nums[j]) {
                    maxval = Math.max(maxval, dp[j]);
                }
            }
            dp[i] = maxval + 1;
            maxans = Math.max(maxans, dp[i]);
        }
        return maxans;
    }
}


