package Scripts;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GenerateScheme {

    public static final int MIN_LENGTH = 3;
    public static final int MAX_LENGTH = 23;

    public static void main(String[] args) {
        // the keys in this map represent the trie encoding
        Map<CallNumber, Integer> scheme = generateScheme();
        Trie t = new Trie(scheme);
        
        try {
            FileWriter f = new FileWriter("scheme.txt");
            for (CallNumber key : scheme.keySet()) {
                f.write(key.toString() + ": " + scheme.get(key) + "\n");
            }
            
            f.close();
        } catch (IOException e) {
            System.out.println("File IO error occurred: " + e.getMessage());
        }
    }

    private static Map<CallNumber, Integer> generateScheme() {
        int size = (int) Math.pow(10, 5);
        Map<CallNumber, Integer> result = new HashMap<CallNumber, Integer>();
        Random r = new Random();
        for (int i = 0; i < size; i++) {
            String num = generateRandomCallNumber(r);
            int numResources = r.nextInt((int) Math.pow(2, 5));

            result.put(new CallNumber(num), numResources);
        }

        return result;
    }
    
    private static String generateRandomCallNumber(Random r) {
        // the longest dewey number is projected to be 23 digits
        // https://oeis.org/A242782/internal
        // and you know what? bet
        int length = r.nextInt(MAX_LENGTH - MIN_LENGTH + 1) + MIN_LENGTH; // between 3 and 23 digits
        String callNumber = "";
        // we do it this way to allow 0's to be as generatable as other
        // decimal digits
        for (int i = 0; i < length; i++) {
            int digit = r.nextInt(10);
            callNumber += digit;
        }
        
        return callNumber;
    }
}

class Trie {
    TrieNode root;

    // TODO generate a random trie schema
    public Trie() {
        this.root = null;
    }

    // import a trie schema
    public Trie(Map<CallNumber, Integer> scheme) {
        for (CallNumber key : scheme.keySet()) {
            this.root = this.addNode(key.path, scheme.get(key));
        }
    }

    private TrieNode addNode(String path, int value) {
        return addNode(this.root, null, path, value);
    }

    private TrieNode addNode(TrieNode root, TrieNode parent, String path, int value) {
        // if the path is empty, this is the node with value
        if (path.length() == 0) {
            // if the root exists, it was originally constructed as a branch,
            // but branches can have value (more generic class numbers can have
            // resources in them), so we just update the value 
            if (root != null) {
                root.value = value;
                return root;
            } else {
                return new TrieNode(parent, value);
            }
        }

        // if this spot is null, create this spot
        if (root == null) {
            root = new TrieNode(parent, -1);
        }

        // ascii offset for '0' is 48: https://www.cs.cmu.edu/~pattis/15-1XX/common/handouts/ascii.html
        int spot = (int) path.charAt(0) - 48;
        root.children[spot] = addNode(root.children[spot], root, path.substring(1), value);
        return root;
    }
}

class TrieNode {
    TrieNode[] children;
    TrieNode parent;
    int value;

    public TrieNode(TrieNode parent, int value) {
        this.parent = parent;
        this.children = new TrieNode[10];
        this.value = value;
    }
}

class CallNumber {
    public String path;

    public CallNumber(String path) {
        this.path = path;
    }

    // we want to turn 23 digit numbers into actual call numbers
    // of the format 333.9999....
    @Override
    public String toString() {
        return path.substring(0, 3) + "." + path.substring(3);
    }
}