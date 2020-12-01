package common;

/**
 * Helper
 *
 * @author damon lin
 * 2020/11/22
 */
public class Helper {

    public static TreeNode initTree() {
        TreeNode root = new TreeNode(3);
        TreeNode l1 = new TreeNode(3);
        TreeNode r1 = new TreeNode(4);
        TreeNode l2 = new TreeNode(5);
        root.left = l1;
        root.right = r1;
        l1.left = l2;
        return root;
    }

    public static void traversePrint(TreeNode root) {
        if(root == null) {
            return;
        }
        System.out.println(root.val);
        traversePrint(root.left);
        traversePrint(root.right);
    }
}
