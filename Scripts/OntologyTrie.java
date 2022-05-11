package Scripts;

import java.io.FileWriter;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class OntologyTrie extends Trie {
    private Set<Relationship> links;
    private Set<OntologyNode> nodes;

    public OntologyTrie(Trie t) {
        this.links = new HashSet<Relationship>();
        this.nodes = new HashSet<OntologyNode>();
        for (TrieNode parent : t.getNonBranchNodes()) {
            OntologyNode p = new OntologyNode(parent.value, parent.fullPath);
            nodes.add(p);
            for (TrieNode child : parent.children) {
                // don't add relationships between branch nodes
                if (child != null && child.value != -1) {
                    OntologyNode c = new OntologyNode(child.value, child.fullPath);
                    nodes.add(c); // won't add duplicates since it's a hash set and OntologyNode overrides the relevant methods
                    Relationship r = new Relationship(c, Relationship.RelationshipType.NARROW_BROAD, p);
                    // ok i know it's bad to add it in three places. sue me
                    links.add(r);
                    p.links.add(r);
                    c.links.add(r);
                }
            }
        }
    }

    // Adds n random relationships to the set of links
    public void addNRelationships(int n) {
        Random r = new Random();
        Relationship.RelationshipType[] types = Relationship.RelationshipType.values();
        int nodesSize = this.nodes.size();
        OntologyNode[] nodesArray = this.nodes.toArray(new OntologyNode[nodesSize]);
        for (int i = 0; i < n; i++) {
            OntologyNode a = nodesArray[r.nextInt(nodesSize)];
            OntologyNode b = nodesArray[r.nextInt(nodesSize)];
            if (!a.equals(b)) {
                Relationship rel = new Relationship(a, types[r.nextInt(types.length)], b);
                links.add(rel);
                a.links.add(rel);
                b.links.add(rel);
            } else {
                i--;
            }
        }
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
    public void outputEncoding(FileWriter f) {
        // TODO Auto-generated method stub
        
    }
    
}
