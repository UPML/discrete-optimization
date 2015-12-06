import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Math.min;

/**
 * A single node in the tree of paths
 */
public class Node {
	private Node parent;
	private double parent_cost;

	private double[][] distances;
	private int[] active_set;

	private int index;

	/**
	 * Constructs a new Node
	 *
	 * @param parent This node's parent
	 * @param parent_cost The cost between these nodes
	 * @param distances The 2D array of distance between locations
	 * @param active_set The set of all points (including this node) that are being calculated
	 * @param index The location index of this node
	 */
	public Node(Node parent, double parent_cost, double[][] distances, int[] active_set, int index) {
		this.parent = parent;
		this.parent_cost = parent_cost;
		this.distances = distances;
		this.active_set = active_set;
		this.index = index;
	}

	/**
	 * Check if this node is terminal
	 *
	 * @return Whether or not the node is terminal
	 */
	public boolean isTerminal() {
		return active_set.length == 1;
	}

	/**
	 * Generate and return this node's children
	 *
	 * @return Array of children
	 */
	public ArrayList<Node> generateChildren() {
		ArrayList<Node> children = new ArrayList<>();

		int[] new_set = new int[active_set.length - 1];
		int i = 0;
		for(int location : active_set) {
			if(location == index)
				continue;

			new_set[i] = location;
			i++;
		}

        for (int aNew_set : new_set)
            children.add(new Node(this, distances[index][aNew_set], distances, new_set, aNew_set));

		return children;
	}

	/**
	 * Get the path array up to this point
	 *
	 * @return The path
	 */
	public int[] getPath() {
		int depth = distances.length - active_set.length + 1;
		int[] path = new int[depth];
		getPathIndex(path, depth - 1);
		return path;
	}

	/**
	 * Recursive method to fill in a path array from this point
	 *
	 * @param path The path array
	 * @param i The index of this node
	 */
	public void getPathIndex(int[] path, int i) {
		path[i] = index;
		if(parent != null)
			parent.getPathIndex(path, i - 1);
	}

	/**
	 * Get the lower bound cost of this node
	 *
	 * @return Lower bound cost
	 */
	public double getLowerBound() {
		double value = 0;

		if(active_set.length == 2)
			return getPathCost() + distances[active_set[0] - 1][active_set[1] - 1];

        double minEdge = 1e9;
        for (int anActive_set : active_set) {
            minEdge = min(minEdge, distances[getPath()[getPath().length - 2 ]][anActive_set]);
        }
        value += minEdge;
        minEdge = 1e9;
        for (int anActive_set : active_set) {
            minEdge = min(minEdge, distances[getPath()[0]][anActive_set]);
        }
        value += minEdge;
        value += boruvkaMST();
        // old
		/*for(int location : active_set) {
			double low1 = Double.MAX_VALUE;
			double low2 = Double.MAX_VALUE;

			for(int other: active_set) {
				if(other == location)
					continue;

				double cost = distances[location][other];
				if(cost < low1) {
					low2 = low1;
					low1 = cost;
				}
				else if(cost < low2) {
					low2 = cost;
				}
			}

			value += low1 + low2;
		}*/

		return (getParentCost() + value) * 1.15;
	}

    /**
     *
     * @return Cost of MST on active_set vertexes
     */

    private double boruvkaMST(){
        Double cost = 0.0;
        HashMap<Integer,Integer> comp = new HashMap<>();
        int c = 0;
        for(int a : active_set){
            comp.put(a, c);
            c++;
        }
        int numberComp = active_set.length;
        while(numberComp > 1){
            ArrayList<Pair<Integer, Integer>> minEdges = new ArrayList<>();
            ArrayList<Double> minDist = new ArrayList<>();
            for(int i = 0; i < active_set.length; ++i){
                minDist.add(i, 1e9);
                minEdges.add(i, new Pair<Integer, Integer>(i, -1));
            }
            for(int i = 0; i < active_set.length; ++i){
                for(int j = 0; j < active_set.length; ++j){
                    if(!comp.get(active_set[i]).equals(comp.get(active_set[j]))){
                        double distance = distances[active_set[i] - 1][active_set[j] - 1];
                         if(distance < minDist.get(comp.get(active_set[i]))){
                             minDist.set(comp.get(active_set[i]), distance);
                             minEdges.set(comp.get(active_set[i]), new Pair<Integer, Integer>(active_set[i],active_set[j]));
                         }
                        if(distance < minDist.get(comp.get(active_set[j]))){
                            minDist.set(comp.get(active_set[j]), distance);
                            minEdges.set(comp.get(active_set[j]), new Pair<Integer, Integer>(active_set[i],active_set[j]));
                        }

                    }

                }
            }
            int minEdgesNumber = 0;
            for(int i = 0; i < active_set.length; ++i){
                if(minEdges.get(i).getValue() != -1 && !comp.get(minEdges.get(i).getKey()).equals(comp.get(minEdges.get(i).getValue()))){
                     cost += minDist.get(i);
                     int old = comp.get(minEdges.get(i).getValue());
                     int newK = comp.get(minEdges.get(i).getKey());
                     for(Integer a : comp.keySet()){
                         if(comp.get(a) ==old){
                             comp.replace(a, old, newK);
                         }
                     }
                    minEdgesNumber++;

                }
            }
            numberComp -= minEdgesNumber;
        }
        return cost;
    }
    /**
     * Get the cost of the entire path up to this point
     *
     * @return Cost of path including return
     */

	public double getPathCost() {
		return distances[0][index] + getParentCost();
	}

	/**
	 * Get the cost up to the parent at this point
	 *
	 * @return Cost of path
	 */
	public double getParentCost() {
		if(parent == null)
			return 0;

		return parent_cost + parent.getParentCost();
	}
}
