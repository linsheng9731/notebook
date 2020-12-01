package leetcode_15;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Main
 * https://leetcode-cn.com/problems/3sum/
 * @author damon lin
 * 2020/11/24
 */
public class Main {
    public static void main(String[] args) {
        int[] nums = new int[] {-1,0,1,2,-1,-4};
        // -4,-1,-1,0,1,2
        List<List<Integer>> lists = new Solution().threeSum(nums);
        for (int i = 0; i < lists.size(); i++) {
            List<Integer> integers = lists.get(i);
            for (int j = 0; j < integers.size(); j++) {
                System.out.println(integers.get(j));
            }
        }
    }
}

/**
 * 三数之和
 * 从小到大排序
 * 定义三个指针用于组合搜索 本质上是保持取任意一个元素 查找对应的负数
 * a , b = a+1 , c = n-1
 * 保持 ab 不动，c 往左移动，如果发现 n[c] + n[b] > -n[a] 则左移 c 否则右移 a
 * c 和 b 相遇重置 a 重新开始一轮搜索知道 a 到了数组的尽头
 * 对于重复元素：跳过，避免出现重复解
 */
class Solution {

    ArrayList<List<Integer>> res = new ArrayList<>();
    public List<List<Integer>> threeSum(int[] sortedNums) {
        Arrays.sort(sortedNums);
        if(sortedNums.length < 3) {
            return new ArrayList<>();
        }
        int a = 0;
        int b = 1;
        int len = sortedNums.length;
        int c = sortedNums.length - 1;
        for (a = 0; a < len - 2; a++) {
            // 重复的 a 也需要去除
            if(a>0 && sortedNums[a] == sortedNums[a-1]) continue;
            b = a + 1;
            c = len -1;
            while(b < c) {
                int sum = sortedNums[b] + sortedNums[c] + sortedNums[a];
                if( sum == 0) {
                    // 找到当前 a、b 对应的解 c 因为 ab 确定了 所以c肯定是唯一的
                    res.add(Arrays.asList(sortedNums[a], sortedNums[b], sortedNums[c]));
                    // 对于重复的 bc 都需要跳过
                    while(b < c && sortedNums[b] == sortedNums[b+1]) b++;
                    while(b < c && sortedNums[c] == sortedNums[c-1]) c--;
                    // 进入下一个 b 因为 b 变大了 所以c肯定要变小 所以也左移
                    b++;
                    c--;
                } else if(sum > 0) {
                    c--; // 偏大
                } else {
                    b++; // 偏小
                }
            }
        }
        return res;
    }
}
