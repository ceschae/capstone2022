package Scripts;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

// This class implements a self-balancing binary search tree (BST)
public class BinarySearchTree implements Graph {
    private BinarySearchNode root;
    private boolean currentEncodingAccurate;
    private Set<BinarySearchNode> nodes;

    // Converts a Trie into a _balanced_ BST
    public BinarySearchTree(Trie t) {
        this.nodes = new HashSet<BinarySearchNode>();
        for (TrieNode n : t.getNonBranchNodes()) {
            this.root = addNode(this.root, n);
        }
    }

    private BinarySearchNode addNode(BinarySearchNode root, TrieNode n) {
        if (root == null) {
            currentEncodingAccurate = false;
            root = new BinarySearchNode(n.value, n.fullPath);
            this.nodes.add(root);
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
        if (!currentEncodingAccurate) {
            storeBstEncoding(this.root, "");
        }
        // l = 1/(n * (n-1)) * sum(distance(x, y) for all nodes x, y, x!=y)
        BigInteger totalDistance = new BigInteger("0");
        double size = 1.0 * this.nodes.size();
        // for each pair of nodes
        for (BinarySearchNode i : this.nodes) {
            for (BinarySearchNode j : this.nodes) {
                int pos = 0;
                while (pos < i.currentPath.length() && pos < j.currentPath.length() && 
                        i.currentPath.charAt(pos) == j.currentPath.charAt(pos)) {
                    pos++;
                }

                // num chars up, num chars down
                // e.g. 1100101 to 111010 has a distance of 9
                // 1 -> 0 -> 1 -> 0 -> 0 -> 1 -> 1 -> 0 -> 1 -> 0
                int distance = (i.currentPath.length() - pos) + (j.currentPath.length() - pos);
                BigInteger newDistance = new BigInteger(distance + "");
                totalDistance = totalDistance.add(newDistance);
            }
        }

        return new BigDecimal((1.0 / (size * (size - 1))) + "").multiply(new BigDecimal(totalDistance)).doubleValue();
    }

    @Override
    public double clusteringCoefficient() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void outputEncoding(FileWriter f) {
        if (!currentEncodingAccurate) {
            storeBstEncoding(this.root, "");
        }
        try {
            f.write("DO NOT EDIT!! This file is generated by BinarySearchTree.java\n");
            f.write("-------------------------------------------------------------\n\n");
            for (BinarySearchNode node : this.nodes) {
                f.write(node.currentPath + "\n");
            }
        } catch (IOException e) {
            System.out.println("File IO Exception occurred: " + e);
        }
    }
    
    private void storeBstEncoding(BinarySearchNode root, String pathSoFar) {
        if (root != null) {
            storeBstEncoding(root.left, pathSoFar + "0");
            root.currentPath = pathSoFar;
            storeBstEncoding(root.right, pathSoFar + "1");
        }
    }
}
