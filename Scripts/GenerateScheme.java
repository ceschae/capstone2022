package Scripts;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GenerateScheme {

    public static final int MIN_LENGTH = 3;
    public static final int MAX_LENGTH = 23;
    public static final int NUM_RELATIONSHIPS = 20;
    public static final int NUM_NODES_POW = 2; // 10 ^ NUM_NODES_POW
    public static final int NUM_RESOURCES_POW = 5; // 2 ^ NUM_RESOURCES_POW

    public static void main(String[] args) {
        // the keys in this map represent the trie encoding
        Map<CallNumber, Integer> scheme = generateScheme();
        Trie t = new Trie(scheme);
        BinarySearchTree bst = new BinarySearchTree(t);
        OntologyTrie ot = new OntologyTrie(t);
        ot.addNRelationships(NUM_RELATIONSHIPS);
        
        try {
            FileWriter fTrie = new FileWriter("trie_scheme.txt");
            t.outputEncoding(fTrie);
            fTrie.close();

            FileWriter fBst = new FileWriter("bst_schema.txt");
            bst.outputEncoding(fBst);
            fBst.close();
        } catch (IOException e) {
            System.out.println("File IO error occurred: " + e.getMessage());
        }

        System.out.println("trie average path length: " + t.averagePathLength());
        System.out.println("bst average path length:  " + bst.averagePathLength());
    }

    private static Map<CallNumber, Integer> generateScheme() {
        int size = (int) Math.pow(10, NUM_NODES_POW); // that's enough slices!!
        Map<CallNumber, Integer> result = new HashMap<CallNumber, Integer>();
        Random r = new Random();
        for (int i = 0; i < size; i++) {
            String num = generateRandomCallNumber(r);
            int numResources = r.nextInt((int) Math.pow(2, NUM_RESOURCES_POW));

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