package leetcode_647;

import utils.Checker;

/**
 * https://leetcode-cn.com/problems/palindromic-substrings/
 * 具有不同开始位置或结束位置的子串，即使是由相同的字符组成，也会被计为是不同的子串。
 * 极端情况下每次新增的字符都是相同的，除了本身是一个回文串，还需要向前遍历看是否能组成新的回文。
 * 因为新增的字符会导致终止下标不同，所以即使是相同的内容也是不同的回文串。
 * a =>    a
 * aa =>   a,a,aa
 * aaa =>  a,a,aa,a,aa,aaa,
 * aaaa => a,a,aa,a,aa,aaa,a,aa,aaa,aaaa
 **/

public class Main {

    public static void main(String[]args) {
        Solution ans = new Solution();
        Checker<Integer> checker = new Checker<Integer>();
        checker.check("case1", ans.countSubstrings("a"), 1);
        checker.check("case2", ans.countSubstrings("aa"), 3);
        checker.check("case3", ans.countSubstrings("aaaa"), 10);
    }
}


class Solution {
    public int countSubstrings(String s) {
        int len = s.length();
        int[] dp = new int[len+1];
        if(len ==1) {
            return 1;
        }
        dp[1] = 1;
        // aa
        for(int i=2; i <= len; i++) {
            int cnt = 1; // 新增的字符本身是一个回文
            // 往回遍历
            for(int back = i-1; back>0; back--) {
                // substring(inclusive, exclusive)
                if(isRecur(s.substring(back-1, i))) {
                    // 如果发现能组成回文串在原来的基础上加一
                    cnt++;
                }
            }
            dp[i] = dp[i-1] + cnt;
        }
        return dp[len];
    }

    // 判断是否是回文字符串
    private boolean isRecur(String s) {
        int len = s.length()-1;
        for(int i=0; i< s.length();i++) {
            if(s.charAt(i) != s.charAt(len-i)){
                return false;
            }
        }
        return true;
    }
}

