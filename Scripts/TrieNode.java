package Scripts;

public class TrieNode {
    TrieNode[] children;
    TrieNode parent;
    int value;
    String fullPath;

    public TrieNode(TrieNode parent, int value, String fullPath) {
        this.parent = parent;
        this.children = new TrieNode[10];
        this.value = value;
        this.fullPath = fullPath;
    }

    public int height() {
        // a leaf node has height 0
        int max = -1;
        for (TrieNode node : this.children) {
            int height = node.height();
            if (height > max) {
                max = height;
            }
        }
        // if this.children is empty, returns 0, since it's a leaf
        // if not empty, returns the largest height seen + 1
        return max + 1;
    }
}