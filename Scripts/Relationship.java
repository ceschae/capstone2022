package Scripts;

public class Relationship {
    RelationshipType type;
    OntologyNode a;
    OntologyNode b;

    public enum RelationshipType {
        NARROW_BROAD, // narrow/broad is meant to be a two-way representation of the "is-a" relationship
        WHOLE_PART, 
        RELATED_SUBJECT,
        BEFORE_AFTER
    }
    
    // this constructor will be called to semantically define the relationship
    // in the closest way we would "say" it in English. for example, this constructor
    // could be called like: 
    //    Relationship r = new Relationship("cat", RelationshipType.NARROW_BROAD, "mammal")
    //    Relationship r = new Relationship("book", RelationshipType.WHOLE_PART, "chapter")
    public Relationship(OntologyNode a, RelationshipType type, OntologyNode b) {
        this.type = type;
        this.a = a;
        this.b = b;
    }
}