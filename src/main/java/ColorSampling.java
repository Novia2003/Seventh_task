import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ColorSampling {
    private final Graph graph;
    private final HashMap<Integer, String> map = new HashMap<>();

    public ColorSampling(Graph graph) {
        this.graph = graph;
    }

    public Map<Integer, String> assignColorVertices() {
        int vertex = graph.vertexCount();
        String[] array = new String[]{"red", "green", "blue", "purple", "orange", "yellow", "cyan", "pink"};

        for (int i = 0; i < vertex; i++) {
            map.put(i, null);
        }

        for (int i = 0; i < vertex; i++) {
            Set<String> adjacentColors = findAdjacentColors(i);
            int currentColor = 0;

            while (adjacentColors.contains(array[currentColor])) {
                currentColor++;
            }

            map.put(i, array[currentColor]);
        }

        return map;
    }

    private Set<String> findAdjacentColors(int v) {
        Set<String> set = new HashSet<>();
        for (Integer adj : graph.adjacencies(v)) {
            set.add(map.get(adj));
        }

        return set;
    }
}