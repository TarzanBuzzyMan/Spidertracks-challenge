/* This class contains the main method, the method to read input from file and parse it into 
 * the edge weighted data structure and fill in the impossible routes with -1.0
 * It also sets up the UI and WeightedGraph algorithm class
 *
 * Author - Taran Busby
 * Date - 2/10/2014
*/

import java.util.*;
import java.io.*;
import javax.swing.*;

public class TravelPlanner {
	
	private static Set<String> citiesList = new HashSet<String>();
	private static Map<String, Map<String, Double>> pathsWeighted = new HashMap<String, Map<String, Double>>();
	
	private static ArrayList<String> startDestCities = new ArrayList<String>();
	
	public static void main(String[] args) {
		
		pathsWeighted = parseFile("routes.in");
		
		for (String k : pathsWeighted.keySet()) {
			citiesList.add(k);
			for (String k2 : pathsWeighted.get(k).keySet()) {
				citiesList.add(k2);
			}
		}
		pathsWeighted = fillWithNotPossibleRoutes(pathsWeighted, citiesList);
		
		WeightedGraph route = new WeightedGraph(pathsWeighted);
		
		String[] citiesArray = citiesList.toArray(new String[citiesList.size()]);
		Arrays.sort(citiesArray);
		UI ux = new UI(citiesArray, route);
		
		try {
			SwingUtilities.invokeLater(ux);
		} catch (Exception ex) {
			System.err.format("*** [%3d] %s %s %n", Thread.currentThread().getId(), ex, ex.getMessage());
		}
		
	//	printDebug(pathsWeighted);
		
		
		
	}
	
	// Parses the travel.in txt file into the pathWeight data type
	private static Map<String,Map<String,Double>> parseFile(String fileName) {
		
		Map<String, Map<String, Double>> pathWeighted = new HashMap<String, Map<String, Double>>();
		
		BufferedReader buffReader = null;
		Scanner scanner = null;
		
		try {
			buffReader = new BufferedReader(new FileReader(fileName));
			
			String line = null;
			while ((line = buffReader.readLine()) != null) {
				scanner = new Scanner(line);
				String[] strs = scanner.nextLine().split(",");
				
				String city1 = strs[0];
				String city2 = strs[1];
				double distance = Double.parseDouble(strs[2]);
				
				pathWeighted.put(city1, modifyMap(pathWeighted, city1, city2, distance));
				
			}
			
		} catch (FileNotFoundException fileException){
			System.out.println("File was not found, please place " + fileName + " in folder GoFetch/routes/");
			fileException.printStackTrace();
		} catch (IOException ioException) {
			System.out.println("IO exception, not sure what caused this. Either try again or contact us and provide the following message");
			ioException.printStackTrace();
		} finally {
			try {
				buffReader.close();
				scanner.close();	
			} catch (IOException ioException) {
				System.out.println("Unable to close Scanner or BufferedReader after use");
				ioException.printStackTrace();
			}
		}
		
		return pathWeighted;
	}
	
	// Creates a dictionary to add to Map<String, Map<String, Double>> or modifies the existing one
	private static Map<String, Double> modifyMap(Map<String, Map<String, Double>> mapToFetchFrom, String node1, String node2, double weight) {
		if (mapToFetchFrom.get(node1) == null) {
			Map<String, Double> dict = new HashMap<String, Double>();
			dict.put(node2, weight);
			return dict;
		} else {
			Map<String, Double> dict = mapToFetchFrom.get(node1);
			dict.put(node2, weight);
			return dict;
		}
	}
	
	// Puts -1.0 in every place that there is not a possible route
	private static Map<String, Map<String, Double>> fillWithNotPossibleRoutes(Map<String, Map<String, Double>> pathWeights, Set<String> cities) {
		Iterator iterateCity1 = cities.iterator();
		while (iterateCity1.hasNext()) {
			String city1 = (String) iterateCity1.next();
			
			Iterator iterateCity2 = cities.iterator();
			while (iterateCity2.hasNext()) {
				String city2 = (String) iterateCity2.next();
				
				if (true/*!city2.equals(city1)*/) {
					if (pathWeights.get(city1) == null) {
						Map<String, Double> toCityAndDistanceDict = new HashMap<String, Double>();
						toCityAndDistanceDict.put(city2, -1.0);
						pathWeights.put(city1, toCityAndDistanceDict);
					}
					if (pathWeights.get(city1).get(city2) == null) {
						Map<String, Double> toCityAndDistanceDict = pathWeights.get(city1);
						toCityAndDistanceDict.put(city2, -1.0);
						pathWeights.put(city1, toCityAndDistanceDict);
					}
				} else {
					if (pathWeights.get(city1) == null) {
						Map<String, Double> toCityAndDistanceDict = new HashMap<String, Double>();
						toCityAndDistanceDict.put(city2, -1.0);
						pathWeights.put(city1, toCityAndDistanceDict);
					}
					if (pathWeights.get(city1).get(city2) == null) {
						Map<String, Double> toCityAndDistanceDict = pathWeights.get(city1);
						toCityAndDistanceDict.put(city2, -1.0);
						pathWeights.put(city1, toCityAndDistanceDict);
					}
				}
			}
		}
		return pathWeights;
	}
	
	// Prints out the pathWeighting in a readable format
	private static void printDebug(Map<String, Map<String, Double>> mapToPrint) {
		boolean firstRow = true;
		for (String k : mapToPrint.keySet()) {
			Map<String, Double> dict = mapToPrint.get(k);
			
			if (firstRow) {
				System.out.format("%16s", "");
				for (String k2 : dict.keySet()) {
					System.out.format("%12s", k2);
				}
				System.out.println();
				firstRow = false;
			}
			
			System.out.format("%18s", k);
			for (String k2 : dict.keySet()) {
				System.out.format("%10.2f   ", dict.get(k2));
			}
			System.out.println();
		}
	}
	
}