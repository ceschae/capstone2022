package Scripts;

import java.io.FileWriter;

public interface Graph {
    public double averagePathLength();
    public double clusteringCoefficient();
    public void outputEncoding(FileWriter f);
}
