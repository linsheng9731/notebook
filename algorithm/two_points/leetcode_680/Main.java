package leetcode_680;

import utils.Checker;

/**
 * https://leetcode.com/problems/valid-palindrome-ii/submissions/
 * 去掉一个字符 判断字符串是否可以成为字符串
 * 维护两个首位指针 一个递增 一个递减 判断指针对应的字符是否相等
 * 如果不等 先后移除第一个和第二个字符 重新判断是否是回文
 */
public class Main {

    public static void main(String[] args) {
        String input = "aba";
        boolean ans = true;
        Checker<Boolean> checker = new Checker<Boolean>();
        checker.check("case1", new Solution().validPalindrome(input), ans);

        input = "abba";
        ans = true;
        checker.check("case2", new Solution().validPalindrome(input), ans);

        input = "a";
        ans = true;
        checker.check("case3", new Solution().validPalindrome(input), ans);

        input = "";
        ans = true;
        checker.check("case4", new Solution().validPalindrome(input), ans);

        input = "abc";
        ans = false;
        checker.check("case5", new Solution().validPalindrome(input), ans);

        input = "aaacccaa";
        ans = true;
        checker.check("case6", new Solution().validPalindrome(input), ans);

        input = "aacccaaa";
        ans = true;
        checker.check("case7", new Solution().validPalindrome(input), ans);

        input = "ac";
        ans = true;
        checker.check("case8", new Solution().validPalindrome(input), ans);

        input = "abcd";
        ans = false;
        checker.check("case9", new Solution().validPalindrome(input), ans);
    }

}


class Solution {
    public boolean validPalindrome(String s) {
        int len = s.length();
        int i = 0;
        int j = len - 1;
        while(i < j) {
            if(s.charAt(i) != s.charAt(j)) {
                // rm one char
                return isPalindrome(s.substring(i+1, j+1)) || isPalindrome(s.substring(i, j));
            }
            i++;
            j--;
        }
        return true;
    }

    private boolean isPalindrome(String s) {
        int i=0;
        int j=s.length()-1;
        while (i < j) {
            if(s.charAt(i) != s.charAt(j)) {
                return false;
            }
            i++;
            j--;
        }
        return true;
    }
}