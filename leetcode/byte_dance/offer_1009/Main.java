package offer_1009;

/**
 * Main
 * https://leetcode-cn.com/problems/sorted-matrix-search-lcci/
 * @author damon lin
 * 2020/11/24
 */
public class Main {
    public static void main(String[] args) {
        int[][] matrix = new int[][] {
                {1,   4,  7, 11, 15},
                {2,   5,  8, 12, 19},
                {3,   6,  9, 16, 22},
                {10, 13, 14, 17, 24},
                {18, 21, 23, 26, 30}
        };

        int[][] matrix2 = new int[][] {
                {1},
                {2},
        };
        boolean b = new Solution().searchMatrix(matrix, 21);
        System.out.println(b);
        boolean b2 = new Solution().searchMatrix(matrix2, 2);
        System.out.println(b2);
    }
}

/**
 * 排序矩阵查找
 */
class Solution {
    public boolean searchMatrix(int[][] matrix, int target) {
        if(matrix.length == 0) {
            return false;
        }
        int m = matrix.length; // 行
        int n = matrix[0].length; // 列

        int cnt = 0;
        int mIndex = 0;
        int nIndex = 0;

        int[] one =  matrix[mIndex];
        for (; nIndex < n; nIndex++) {
            if(one[nIndex] == target) {
                return true;
            }
            if(one[nIndex] > target) {
                break;
            }
        }
        nIndex--;
        while (mIndex < m && nIndex >= 0) {
            for (; mIndex < m && nIndex >=0 ; mIndex++) {
                if(matrix[mIndex][nIndex] == target) {
                    return true;
                }
                if(matrix[mIndex][nIndex] > target) {
                    nIndex--; // 往左退一格子
                    mIndex--; // 保持同一行
                }
            }
        }
        return false;
    }
}