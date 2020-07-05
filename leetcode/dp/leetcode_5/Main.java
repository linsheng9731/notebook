package leetcode_5;

import utils.Checker;

/**
 * https://leetcode-cn.com/problems/longest-palindromic-substring/submissions/
 * 最长回文串
 * 思路：dp 动态规划 先找到子问题
 * 比如字符串"abcba" 可以拆解为 a + bcb + a, bcb 可以拆解为 b + c + b
 * 最简单的假设：另 dp[i][j] 记录为起始位置 i，终止位置 j 的字符串的最长回文串。
 * 但是这样无法进行递推，因为 s[i-1] == s[j+1] 无法通过 dp[i][j] 得到 dp[i-1][j+1]
 * 如果另 dp[i][j] 记录起始位置 i，终止位置 j 的字符串是否是回文串，那么如果 s[i-1] == s[j+1]
 * 那么得到 dp[i-1][j+1] 也为回文串，此时可以更新最大长度。
 * 可以简单的得到，dp[t][t] = true
 * 得到定义后，接下来需要找到递推路径，如果i从（0 - len-1），j 从（0 - len-1），进行双层遍历，
 * 子串的长度会一直变。以 abcba 为例，ab,abc,abcb,abcba,bc,bcb[true],bcba,... 可以看到遍历到
 * abcba 时因为还没有遍历过 bcb ，所以得不到正确的结论。
 * 从递推的性质出发，问题范围是一层层递进的，这里以长度为问题范围的划分依据。
 * abcba，长度为1时，每个字母都是回文。长度为2，判断相邻两个是否相等。长度为>3，判断 dp[i][j] && s[i-1] == s[j+1]
 */
public class Main {

    public static void main(String[] args) {
        Solution ans = new Solution();
        Checker<String> checker = new Checker<String>();
        checker.check("case1", ans.longestPalindrome("abedsssfewc"), "sss");
        checker.check("case2", ans.longestPalindrome("cbbd"), "bb");
        checker.check("case3", ans.longestPalindrome("cccbbd"), "ccc");
        checker.check("case4", ans.longestPalindrome("ccbbbd"), "bbb");
        checker.check("case5", ans.longestPalindrome(""), "");
        checker.check("case6", ans.longestPalindrome("bb"), "bb");
        checker.check("case7", ans.longestPalindrome("bab"), "bab");
        checker.check("case8", ans.longestPalindrome("aaaa"), "aaaa");
        checker.check("case9", ans.longestPalindrome("abcba"), "abcba");
    }
}
class Solution {
    public String longestPalindrome(String s) {
        if(s.length()==0) {
            return s;
        }
        if(s.length()==2 && s.charAt(0) == s.charAt(1)) {
            return s;
        }
        boolean[][] dp = new boolean[s.length()+1][s.length()+1];
        int maxLen = 0;
        int startIndex = 0;
        int endIndex = 0;
        for(int t=0;t<s.length()-1;t++) {
            dp[t][t] = true;
            if(s.charAt(t) == s.charAt(t+1)) {
                dp[t][t+1] = true;
            }
        }
        dp[s.length()-1][s.length()-1] = true;
        // abcba
        for (int l=1;l<s.length();l++) {
            for(int i=0;i<s.length()-l;i++) {
                int j = i + l;
                if(l==1) {
                    dp[i][j] = s.charAt(i) == s.charAt(j);
                } else if(dp[i+1][j-1] && s.charAt(i) == s.charAt(j)) {
                        dp[i][j] = true;
                    }
                if(dp[i][j]) {
                    if((j-i)>maxLen) {
                        maxLen = j-i;
                        startIndex = i;
                        endIndex = j;
                    }
                }
                }
            }
        return s.substring(startIndex, endIndex+1);
    }
}