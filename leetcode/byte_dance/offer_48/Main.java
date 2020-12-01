package offer_48;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Main
 *
 * @author damon lin
 * 2020/11/25
 */
public class Main {
    public static void main(String[] args) {
//        System.out.println(new Solution().lengthOfLongestSubstring("abcs"));
//        System.out.println(new Solution().lengthOfLongestSubstring(" "));
        System.out.println(new Solution().lengthOfLongestSubstring("dvdf"));
    }
}


class Solution {
    public int lengthOfLongestSubstring(String s) {
        if(s.length() == 0) {
            return 0;
        }
        int length = s.length();
        int max = 1;
        // 以 i 为终点的最长不重复子串
        HashMap<Integer, HashSet> res = new HashMap<>();
        HashSet first = new HashSet();
        first.add(s.charAt(0));
        res.put(0, first);
        for (int i = 1; i < length; i++) {
            HashSet set = res.get(i-1);
            HashSet newSet = new HashSet<>(set);
            if(!set.contains(s.charAt(i))) {
                newSet.add(s.charAt(i));
                max = Math.max(max, newSet.size());
                res.put(i, newSet);
            } else {
                int tmp = i;
                newSet.clear();
                while(tmp > 0 && !newSet.contains(s.charAt(tmp))) {
                    newSet.add(s.charAt(tmp));
                    tmp--;
                }
                max = Math.max(max, newSet.size());
                res.put(i, newSet);
            }

        }
        return max;
    }
}


//class Solution {
//    public int lengthOfLongestSubstring(String s) {
//        int length = s.length();
//        int max = 0;
//        HashMap<Integer, HashSet> res = new HashMap<>();
//        for (int i = 0; i < length; i++) {
//            HashSet set = res.getOrDefault(i, new HashSet<CharSequence>());
//            for (int j = i; j < length; j++) {
//                if(set.contains(s.charAt(j))) {
//                   break;
//                }
//                set.add(s.charAt(j));
//                max = Math.max(set.size(), max);
//            }
//            res.put(i, set);
//        }
//        return max;
//    }
//}
