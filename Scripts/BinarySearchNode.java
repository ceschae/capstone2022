package Scripts;

public class BinarySearchNode {
    public BinarySearchNode left;
    public BinarySearchNode right;
    public int value;
    public String previousPath;
    public String currentPath;
    public int height;

    public BinarySearchNode(int value, String previousPath) {
        this.value = value;
        this.previousPath = previousPath;
    }

    public int balance() {
        int leftHeight = this.left == null ? -1 : this.left.height;
        int rightHeight = this.right == null ? -1 : this.right.height;
        return leftHeight - rightHeight;
    }
}
