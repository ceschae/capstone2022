package Scripts;

public class CallNumber implements Comparable<CallNumber> {
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

    @Override
    public int compareTo(CallNumber other) {
        return this.path.compareTo(other.path);
    }
}
