package graph.scc;

import java.util.List;

public class SCCResult {

    private final List<List<Integer>> components;
    private final int[] componentId;

    public SCCResult(List<List<Integer>> components, int[] componentId) {
        this.components = components;
        this.componentId = componentId;
    }

    public List<List<Integer>> getComponents() {
        return components;
    }

    public int[] getComponentId() {
        return componentId;
    }

    public int getComponentCount() {
        return components.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Number of SCCs: ").append(components.size()).append("\n");
        for (int i = 0; i < components.size(); i++) {
            sb.append("SCC ").append(i).append(" (size ").append(components.get(i).size())
                    .append("): ").append(components.get(i)).append("\n");
        }
        return sb.toString();
    }
}
