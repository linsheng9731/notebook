package leetcode_134;

/**
 * Main
 * https://leetcode-cn.com/problems/gas-station/
 * @author damon lin
 * 2020/11/25
 */
public class Main {

    public static void main(String[] args) {

    }
}

/**
 * 加油站
 * 计算每个加油站的剩余油量 得到 remain[i]
 * 累加 remain[i] 得到 sum 记录 起点 i
 * 如果 sum <0 则说明上个区间无法满足需求
 * 重新开始记录起点位置为 下个点即 i+1
 */
class Solution {
    public int canCompleteCircuit(int[] gas, int[] cost) {
        int len = gas.length;
        if(len ==0 ) return -1;
        int[] remain = new int[len];
        for (int i = 0; i < len; i++) {
            remain[i] = gas[i] - cost[i];
        }
        int totoal = 0;
        for (int i = 0; i < len; i++) {
            totoal += remain[i];
        }
        if(totoal < 0 ) {
            return -1;
        }
        int lastStart = 0;
        int sum = 0;
        for (int i = 0; i < len; i++) {
            sum += remain[i];
            if(sum <=0) {
                sum = 0;
                lastStart = i+1; // 当前累加区间的起点
                continue;
            }
        }
        return lastStart;
    }
}
