package string;

public class StringTest {

    public static void main(String[] args) {
        String s1 = "test";
        String s2 = new String("test");
        String s3 = s1.intern();

        System.out.println(s1 == s2);
        System.out.println(s1.equals(s2));
        System.out.println(s1 == s3);
        System.out.println(s3 == s2);
        System.out.println(s3 == s2);
    }
}
