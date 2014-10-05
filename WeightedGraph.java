/* This class contains the Djikstra algorithm for finding shortest route and cost to a node.
 * It stores this information internally to the class and can be fetched later if necessary
 * via the get methods. The findBestPath(Start start, String end) contains a bug that means it cannot find
 * the route from A to A as it will just return A with a distance of 0. Create this class by calling
 * the constructor and passing in the pathWeights (node and edge). Run the algorithm by calling 
 * findBestPath(ArrayList<String> nodesToTraverse) and passing in a list of the nodes to traverse.
 *
 * Author - Taran Busby
 * Date - 5/10/2014
*/

import java.util.*;
import java.io.*;

public class WeightedGraph {
	
	private static Map<String, Map<String, Double>> pathWeight = null;
	
	private static ArrayList<String> nodesTraversed = null;
	private static ArrayList<String> bestPaths = null;
	private static double shortestDistance = 0.0;
	
	// Constructor, pass it the path weighting - nodes/edges
	public WeightedGraph(Map<String, Map<String, Double>> pathWeighting) {
		pathWeight = pathWeighting;
	}
	
	// setPathWeight allows the pathWeights to be changed at runtime
	// TODO: requires GUI editing of travel.in txt file and updating
	public static void setPathWeight(Map<String, Map<String, Double>> pathWeighting) {
		pathWeight = pathWeighting;
	}
	
	// call findBestPath to find out the best path between start, middle, end nodes
	// pass it an ArrayList of the nodes to traverse in order
	// the variables are stored in this class once run so can be fetched later if necessary
	public static ArrayList<String> findBestPath(ArrayList<String> nodesToTraverse) {
		nodesTraversed = nodesToTraverse;
		bestPaths = new ArrayList<String>();
		shortestDistance = 0.0;
		
		for (int i = 0; i < nodesToTraverse.size(); i++) {
			String startNode = nodesToTraverse.get(i);
			if ((i+1) >= nodesToTraverse.size())
				break;
			String endNode = nodesToTraverse.get(i+1);
			if (bestPaths.size() > 1 && !bestPaths.get(bestPaths.size()-1).equals("Not possible"))
				bestPaths.remove(bestPaths.size()-1);
			bestPaths.addAll(findBestPath(startNode, endNode));
		}
		
		return bestPaths;
	}
	
	public static ArrayList<String> getNodesTraversed() {
		return nodesTraversed;
	}
	
	public static ArrayList<String> getBestPath() {
		return bestPaths;
	}
	
	public static double getDistance() {
		return shortestDistance;
	}
	
	// This bestPath method uses Dijkstra's algorithm
	// there is a bug in this code that I cannot find that blocks A to A or B to B
	// from properly running and finding the loop round to the start
	// TODO: find bug blocking A to A; returns A etc instead of loop
	private static ArrayList<String> findBestPath(String start, String dest) {
		ArrayList<String> bestPath = new ArrayList<String>();
		Map<String, Double> bestDistTotalSoFar = new HashMap<String, Double>();
		Map<String, String> previousNodes = new HashMap<String, String>();
		Map<String, Boolean> bestDistTotalIsKnown = new HashMap<String, Boolean>();
		
		if ((pathWeight.get(start) == null) || (pathWeight.get(dest) == null)) {
			System.err.println("Start or destination not a node\n");
			return null;
		}
		
		for (String nextNode : pathWeight.keySet()) {
			if (pathWeight.get(start).get(nextNode) > 0) {
				bestDistTotalSoFar.put(nextNode, pathWeight.get(start).get(nextNode));
				previousNodes.put(nextNode, start);
			}
			else {
				bestDistTotalSoFar.put(nextNode, Double.MAX_VALUE); 
				previousNodes.put(nextNode, "");
			}
			
			bestDistTotalIsKnown.put(nextNode, false);
		}
		
		bestDistTotalSoFar.put(start, 0.0);
		bestDistTotalIsKnown.put(start, true);
		
		while (!bestDistTotalIsKnown.get(dest)) {
			
			double minBestDist = Double.MAX_VALUE;
			String bestNode = "";
		
			for (String nextNode : pathWeight.keySet()) {
				if (bestDistTotalIsKnown.get(nextNode)) {
					continue;
				}
				if (bestDistTotalSoFar.get(nextNode) < minBestDist && bestDistTotalSoFar.get(nextNode) != 0) {
					minBestDist = bestDistTotalSoFar.get(nextNode);
					bestNode = nextNode;
				}
			}
			
			if (bestNode.equals("")) {
				bestPath.add("Not possible");
				return bestPath;
			}
			
			// Best total distance so far for this node is the actual best total distance
			bestDistTotalIsKnown.put(bestNode, true);
		
			// Check this node for a better route to destination for other nodes whose best distance is not yet known
			for (String nextNode : pathWeight.keySet()) {
				
				if (bestDistTotalIsKnown.get(nextNode)) {
					continue;
				}
				
				if (pathWeight.get(bestNode).get(nextNode) <= 0) {
					continue;
				}
				
				double altPaths = bestDistTotalSoFar.get(bestNode) + pathWeight.get(bestNode).get(nextNode);
				if (altPaths < bestDistTotalSoFar.get(nextNode)) {
					bestDistTotalSoFar.put(nextNode, altPaths);
					previousNodes.put(nextNode, bestNode);
				}
			}
		}
		
		// Get distance to the destination over the best path
		shortestDistance += bestDistTotalSoFar.get(dest);
		
		// Reverse path and store the strings of the best path
		String nextNode = dest;
		bestPath.add(0, nextNode);
		while (!nextNode.equals(start)) {
			nextNode = previousNodes.get(nextNode);
			bestPath.add(0, nextNode);
		}
		
		return bestPath;
		
	}
	
}