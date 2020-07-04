package SystemLogic;

import java.util.*;

public class GraphForMatch {
    private Map<RideSingleStation, LinkedHashSet<RideSingleStation>> map = new HashMap();

    public void addEdge(RideSingleStation node1, RideSingleStation node2) {
        LinkedHashSet<RideSingleStation> adjacent = map.get(node1);
        if(adjacent==null) {
            adjacent = new LinkedHashSet();
            map.put(node1, adjacent);
        }
        adjacent.add(node2);
    }

    public void addTwoWayVertex(RideSingleStation node1, RideSingleStation node2) {
        addEdge(node1, node2);
        addEdge(node2, node1);
    }

    public boolean isConnected(RideSingleStation node1, RideSingleStation node2) {
        Set adjacent = map.get(node1);
        if(adjacent==null) {
            return false;
        }
        return adjacent.contains(node2);
    }

    public LinkedList<RideSingleStation> adjacentNodes(RideSingleStation last) {
        LinkedHashSet<RideSingleStation> adjacent = map.get(last);
        if(adjacent==null) {
            return new LinkedList();
        }
        return new LinkedList<RideSingleStation>(adjacent);
    }
}
