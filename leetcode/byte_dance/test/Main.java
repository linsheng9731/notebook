package test;

/**
 * Main
 *
 * @author damon lin
 * 2020/11/26
 */

public class Main {
    public static void main(String[] args) {
        //Scanner in = new Scanner(System.in);
        //int a = in.nextInt();
        //System.out.println(a);

        System.out.println(new Solution().multiply("12", "2"));
        // System.out.println("Hello World!");
    }
}

class Solution {

    public String multiply(String num1, String num2) {
        if(num1.equals("0") || num2.equals("0")) {
            return "0";
        }
        int m = num1.length();
        int n = num2.length();
        int[] ans = new int[m* n];
        for(int i=m-1;i>=0;i--) {
            System.out.print("debug1: ");
            System.out.println(i);
            int tmp = num1.charAt(i) - '0';
            for(int j = n-1;j>=0;j--) {
                System.out.print("debug2: ");
                System.out.println(j);
                int tmp2=  num2.charAt(j) - '0';
                //System.out.print("debug: ");
                //System.out.println(tmp * tmp2);
                ans[i+j+1] += tmp * tmp2;
            }
        }

        for(int i= m+n -1; i>0;i--) {
            ans[i-1] += ans[i] / 10; // 进位值
            ans[i] %= 10; // 剩余的值
        }
        int i= 0 ;
        String ansStr = "";
        while(i<m+n) {
            ansStr+=ans[i];
            i++;
        }
        return ansStr;
    }

}