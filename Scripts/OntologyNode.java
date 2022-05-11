package Scripts;

import java.util.HashSet;
import java.util.Set;

public class OntologyNode implements Comparable<OntologyNode> {
    Set<Relationship> links;
    int value;
    String path;

    public OntologyNode(int value, String path) {
        this.links = new HashSet<Relationship>();
        this.value = value;
        this.path = path;
    }

    // OntologyNodes are sorted by their call number
    @Override
    public int compareTo(OntologyNode o) {
        return this.path.compareTo(o.path);
    }

    // OntologyNodes are hashed by their call number
    @Override
    public int hashCode() {
        return this.path.hashCode();
    }

    // OntologyNodes test equality with their call numbers
    @Override
    public boolean equals(Object o) {
        if (o instanceof OntologyNode) {
            return this.path.equals(((OntologyNode) o).path);
        } else {
            return false;
        }
    }
}
