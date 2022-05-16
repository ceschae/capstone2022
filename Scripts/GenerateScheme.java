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
    public static final int NUM_RESOURCES_POW = 5; // 2 ^ NUM_RESOURCES_POW

    public static void main(String[] args) {
        int[] trialsOfN = {100, 200, 500, 
            1000, 2000, 5000, 
            10000, 20000, 50000, 
            100000, 200000, 500000, 
            1000000, 2000000, 5000000};

        try {
            FileWriter f = new FileWriter("schemes/trial_results.txt");
            for (int n : trialsOfN) {
                // the keys in this map represent the trie encoding
                Map<CallNumber, Integer> scheme = generateScheme(n);
                Trie t = new Trie(scheme);
                BinarySearchTree bst = new BinarySearchTree(t);
                OntologyTrie ot = new OntologyTrie(t);
                ot.addNRelationships(n / 5); // add 20% of nodes worth of relationships
                
                FileWriter fTrie = new FileWriter("schemes/trie_scheme_n=" + n + ".txt");
                t.outputEncoding(fTrie);
                fTrie.close();

                FileWriter fBst = new FileWriter("schemes/bst_schema_n=" + n + ".txt");
                bst.outputEncoding(fBst);
                fBst.close();

                FileWriter fOt = new FileWriter("schemes/ot_schema_n=" + n + ".txt");
                ot.outputEncoding(fOt);
                fOt.close();
                

                f.write("\nn = " + n);
                f.write("trie average path length: " + t.averagePathLength());
                f.write("bst average path length:  " + bst.averagePathLength() + "\n");
            }
            
            f.close();

        } catch (IOException e) {
            System.out.println("File IO error occurred: " + e.getMessage());
        }
    }

    // int n = number of nodes to generate with resources represented by it
    private static Map<CallNumber, Integer> generateScheme(int n) {
        Map<CallNumber, Integer> result = new HashMap<CallNumber, Integer>();
        Random r = new Random();
        for (int i = 0; i < n; i++) {
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