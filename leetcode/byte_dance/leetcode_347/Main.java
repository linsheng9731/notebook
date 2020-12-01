package leetcode_347;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Main
 * https://leetcode-cn.com/problems/top-k-frequent-elements/
 * @author damon lin
 * 2020/11/22
 */
public class Main {

    public static void main(String[] args) {
        int[] input = new int[] {1,1,1,2,2,3};
        int[] res = new Solution().topKFrequent(input, 2);
        for (int i = 0; i < res.length; i++) {
            System.out.println(res[i]);
        }
    }
}

/**
 * 前 K 个高频元素
 * 用 map 统计次数 用优先级队列排序取前 k 个
 */
class Solution {
    public int[] topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            map.put(nums[i], map.getOrDefault(nums[i], 0) + 1);
        }
       int size =  map.entrySet().size();
        PriorityQueue<int[]> queue = new PriorityQueue<>(size, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                return o2[1] - o1[1]; // 逆序排列
            }
        });
        for(Map.Entry<Integer, Integer> entry : map.entrySet()) {
            queue.add(new int[]{entry.getKey(), entry.getValue()});
        }
        int[] res = new int[k]; // 只需要 k 个元素 多了会多出来 0
        for (int i = 0; i < k; i++) {
            res[i] = queue.poll()[0];
        }
        return res;
    }
}
