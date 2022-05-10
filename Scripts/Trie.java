package Scripts;

import java.io.FileWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Trie implements Graph {
    private TrieNode root;
    private Set<TrieNode> nodes;

    // TODO generate a random trie schema
    public Trie() {
        this.root = null;
        this.nodes = new HashSet<TrieNode>();
    }

    // import a trie schema
    public Trie(Map<CallNumber, Integer> scheme) {
        this.nodes = new HashSet<TrieNode>();
        for (CallNumber key : scheme.keySet()) {
            this.root = this.addNode(key.path, scheme.get(key));
        }
    }

    public Set<TrieNode> getNodes() {
        return new HashSet<TrieNode>(this.nodes);
    }

    private TrieNode addNode(String path, int value) {
        return addNode(this.root, null, path, value, path);
    }

    private TrieNode addNode(TrieNode root, TrieNode parent, String path, int value, String fullPath) {
        // if the path is empty, this is the node with value
        if (path.length() == 0) {
            // if the root exists, it was originally constructed as a branch
            // (or it was randomly duplicated, which isn't an issue),
            // but branches can have value (more generic class numbers can have
            // resources in them), so we just update the value 
            if (root != null) {
                root.value = value;
                return root;
            } else {
                TrieNode n = new TrieNode(parent, value, fullPath);
                this.nodes.add(n);
                return n;
            }
        }

        // if this spot is null, create this spot
        if (root == null) {
            // (ignore decimals)
            // 653.1246 is fullPath, we've eaten 653.12, path is 46
            // we want 653.12 (len is 5, give substring 6)
            // 7 - 2 + 1 = 6
            TrieNode n = new TrieNode(parent, -1, fullPath.substring(0, fullPath.length() - path.length() + 1));
            this.nodes.add(n);
            root = n;
        }

        // ascii offset for '0' is 48: https://www.cs.cmu.edu/~pattis/15-1XX/common/handouts/ascii.html
        int spot = (int) path.charAt(0) - 48;
        root.children[spot] = addNode(root.children[spot], root, path.substring(1), value, fullPath);
        return root;
    }

    @Override
    public double averagePathLength() {
        // l = 1/(n * (n-1)) * sum(distance(x, y) for all nodes x, y, x!=y)
        BigInteger totalDistance = new BigInteger("0");
        double size = 1.0 * this.nodes.size();
        // for each pair of nodes
        for (TrieNode i : this.nodes) {
            // if value != -1 (for THIS use case, a branch is not really a "node" because
            // it doesn't have any resources for it that people would look up!)
            if (i.value != -1) {
                for (TrieNode j : this.nodes) {
                    if (j.value != -1) {
                        // find common prefix
                        int pos = 0;
                        while (pos < i.fullPath.length() && pos < j.fullPath.length() && 
                                i.fullPath.charAt(pos) == j.fullPath.charAt(pos)) {
                            pos++;
                        }

                        // num chars up, num chars down
                        // e.g. 653.256 to 651.005 has a distance of 8
                        // 6 -> 5 -> 2 -> 3 -> 5 -> 1 -> 0 -> 0 -> 5
                        int distance = (i.fullPath.length() - pos) + (j.fullPath.length() - pos);
                        BigInteger newDistance = new BigInteger(distance + "");
                        totalDistance = totalDistance.add(newDistance);
                    }
                }
            } else {
                // if it's a branch node, we remove it from the size 
                // because it doesn't count
                size -= 1.0;
            }

            System.out.println("total distance after node " + i.fullPath + ": " + totalDistance);
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
        // TODO Auto-generated method stub
        
    }
}