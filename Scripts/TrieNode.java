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
}