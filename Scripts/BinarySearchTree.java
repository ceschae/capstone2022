package Scripts;

// This class implements a self-balancing binary search tree (BST)
public class BinarySearchTree implements Graph {
    private BinarySearchNode root;

    // Converts a Trie into a _balanced_ BST
    public BinarySearchTree(Trie t) {
        for (TrieNode n : t.getNodes()) {
            this.root = addNode(this.root, n);
        }
    }

    private BinarySearchNode addNode(BinarySearchNode root, TrieNode n) {
        if (root == null) {
            root = new BinarySearchNode(n.value, n.fullPath);
        } else {
            if (n.fullPath.compareTo(root.previousPath) < 0) {
                // go left, new path is alphabetically "earlier" than this node
                root.left = addNode(root.left, n);
            } else if (n.fullPath.compareTo(root.previousPath) > 0) {
                // go right, new path is alphabetically "later" than this node
                root.right = addNode(root.right, n);
            } // else this node already exists, so we don't modify the tree

            root.height = Math.max(root.left == null ? -1 : root.left.height, root.right == null ? -1 : root.right.height) + 1;

            root = rebalance(root);
        }

        return root;
    }

    private BinarySearchNode rebalance(BinarySearchNode root) {
        int balance = root.balance();
        int leftBalance = root.left == null ? 0 : root.left.balance();
        int rightBalance = root.right == null ? 0 : root.right.balance();
        if (balance < -1 && rightBalance == -1) {
            return rotateLeft(root);
        } else if (balance > 1 && leftBalance == 1) {
            return rotateRight(root);
        } else if (balance < -1 && rightBalance == 1) {
            return rotateRightLeft(root);
        } else if (balance > 1 && leftBalance == -1) {
            return rotateLeftRight(root);
        } else {
            // subtree is balanced
            return root;
        }
    }

    private BinarySearchNode rotateLeft(BinarySearchNode root) {
        BinarySearchNode right = root.right;
        root.right = right.left;
        right.left = root;

        root.height = Math.max(root.left == null ? -1 : root.left.height, root.right == null ? -1 : root.right.height) + 1;
        right.height = Math.max(right.left == null ? -1 : right.left.height, right.right == null ? -1 : right.right.height) + 1;
        
        return right;
    }

    private BinarySearchNode rotateRight(BinarySearchNode root) {
        BinarySearchNode left = root.left;
        root.left = left.right;
        left.right = root;

        root.height = Math.max(root.left == null ? -1 : root.left.height, root.right == null ? -1 : root.right.height) + 1;
        left.height = Math.max(left.left == null ? -1 : left.left.height, left.right == null ? -1 : left.right.height) + 1;
        
        return left;
    }

    private BinarySearchNode rotateLeftRight(BinarySearchNode root) {
        root.left = rotateLeft(root.left);
        root = rotateRight(root);

        root.height = Math.max(root.left == null ? -1 : root.left.height, root.right == null ? -1 : root.right.height) + 1;
        
        return root;
    }

    private BinarySearchNode rotateRightLeft(BinarySearchNode root) {
        root.right = rotateRight(root.right);
        root = rotateLeft(root);

        root.height = Math.max(root.left == null ? -1 : root.left.height, root.right == null ? -1 : root.right.height) + 1;
        
        return root;
    }

    @Override
    public double averagePathLength() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double clusteringCoefficient() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void outputEncoding() {
        // TODO Auto-generated method stub
        
    }
    
}
