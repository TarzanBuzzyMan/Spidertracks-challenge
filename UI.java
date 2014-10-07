/* This class contains all the code to make the UI and does simple calls to the WeightedGraph
 * class to recieve results on the data.
 *
 * Author - Taran Busby
 * Date - 6/10/2014
*/

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class UI implements Runnable {
	
	JTextField routePlannedTextField = null;
	JTextField routePossibleTextField = null;
	JTextField routeDistanceTextField = null;
	JTextField routeShortestResultTextField = null;
	JTextField routeShortestDistanceTextField = null;
	
	JLabel routePlannedLabel = null;
	JLabel routePossibleLabel = null;
	JLabel routeDistanceLabel = null;
	JLabel routeShortestResultLabel = null;
	JLabel routeShortestDistanceLabel = null;
	
	JComboBox<String> functionalityComboBox = null;
	JButton calculateButton = null;
	JButton resetButton = null;
	
	JList<String> routeList = null;
	JScrollPane routePane = null;
	String routeToTraverse = "";
	ArrayList<String> routeListToTraverse = null;
	
	String[] cityNodes = null;
	WeightedGraph route = null;
	
	String[] functionalityStrings = new String[] {
		"Calculate Distance", "Journey Planner", "Shortest Route"
	};
	
	// constructor for the UI class, takes a list of the cities and the WeightedGraph class
	// so it can perform calculations when "Calculate" is clicked
	public UI(String[] cities, WeightedGraph routes) {
		cityNodes = cities;
		route = routes;
		routeListToTraverse = new ArrayList<String>();
	}
	
	// the UI is using SwingUtilities.invokeLater() so this method needs to be Overrriden
	public void run() {
		JFrame frame = new JFrame();
		frame.setTitle("GoFetch Railways - Trip planner");
		
		// ================== UI top strip =======================
		
		functionalityComboBox = new JComboBox<String>(functionalityStrings);
		functionalityComboBox.setPreferredSize(new Dimension(175, 30));
		functionalityComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				functionalityComboBox_Change();
			}
		});
		
		calculateButton = new JButton("Calculate");
		calculateButton.setPreferredSize(new Dimension(155, 30));
		calculateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				calculateButton_Click();
			}
		});
		
		resetButton = new JButton("Reset");
		resetButton.setPreferredSize(new Dimension(155, 30));
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				resetButton_Click();
			}
		});
		
		JPanel strip = new JPanel(new FlowLayout(FlowLayout.CENTER));
		strip.add(functionalityComboBox);
		strip.add(calculateButton);
		strip.add(resetButton);
		
		frame.getContentPane().add(strip, BorderLayout.NORTH);
		frame.getRootPane().setDefaultButton(calculateButton);
		
		// ================== UI Right hand Results panel =======================
		
		routePlannedTextField = new JTextField();
		routePlannedTextField.setPreferredSize(new Dimension(350, 30));
		
		routePlannedLabel = new JLabel("Start and end stations to check : ");
		routePlannedLabel.setPreferredSize(new Dimension(50, 30));
		
		JPanel labeledRoutePlanned = new JPanel(new BorderLayout());
		labeledRoutePlanned.add(routePlannedLabel, BorderLayout.NORTH);
		labeledRoutePlanned.add(routePlannedTextField, BorderLayout.CENTER);
		
		
		routePossibleTextField = new JTextField();
		routePossibleTextField.setEditable(false);
		routePossibleTextField.setPreferredSize(new Dimension(350, 30));
		
		routePossibleLabel = new JLabel("This route is : ");
		routePossibleLabel.setPreferredSize(new Dimension(50, 30));
		
		JPanel labeledRoutePossible = new JPanel(new BorderLayout());
		labeledRoutePossible.add(routePossibleLabel, BorderLayout.NORTH);
		labeledRoutePossible.add(routePossibleTextField, BorderLayout.CENTER);
		
		
		routeDistanceTextField = new JTextField();
		routeDistanceTextField.setEditable(false);
		routeDistanceTextField.setPreferredSize(new Dimension(350, 30));
		
		routeDistanceLabel = new JLabel("The distance of the route is : ");
		routeDistanceLabel.setPreferredSize(new Dimension(50, 30));
		
		JPanel labeledRouteDistance = new JPanel(new BorderLayout());
		labeledRouteDistance.add(routeDistanceLabel, BorderLayout.NORTH);
		labeledRouteDistance.add(routeDistanceTextField, BorderLayout.CENTER);
		
		
		routeShortestResultTextField = new JTextField();
		routeShortestResultTextField.setEditable(false);
		routeShortestResultTextField.setPreferredSize(new Dimension(350, 30));
		
		routeShortestResultLabel = new JLabel("You can get there faster by going this way instead : ");
		routeShortestResultLabel.setPreferredSize(new Dimension(50, 30));
		
		JPanel labeledRouteResult = new JPanel(new BorderLayout());
		labeledRouteResult.add(routeShortestResultLabel, BorderLayout.NORTH);
		labeledRouteResult.add(routeShortestResultTextField, BorderLayout.CENTER);
		
		
		routeShortestDistanceLabel = new JLabel("Estimated distance along shortest route : ");
		routeShortestDistanceLabel.setPreferredSize(new Dimension(50, 30));
		
		routeShortestDistanceTextField = new JTextField();
		routeShortestDistanceTextField.setEditable(false);
		routeShortestDistanceTextField.setPreferredSize(new Dimension(350, 30));
		
		JPanel labeledRouteShortestDistance = new JPanel(new BorderLayout());
		labeledRouteShortestDistance.add(routeShortestDistanceLabel, BorderLayout.NORTH);
		labeledRouteShortestDistance.add(routeShortestDistanceTextField, BorderLayout.CENTER);
		
		
		JPanel results = new JPanel(new FlowLayout(FlowLayout.CENTER));
		results.add(labeledRoutePlanned);
		results.add(labeledRoutePossible);
		results.add(labeledRouteDistance);
		results.add(labeledRouteResult);
		results.add(labeledRouteShortestDistance);
		frame.getContentPane().add(results, BorderLayout.CENTER);
		
		// ================== UI Left hand Selection panel =======================
		
		routeList = new JList<String>(cityNodes);
		routeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		routeList.addListSelectionListener(new ListSelectionListener() { 
			public void valueChanged(ListSelectionEvent ev) {
				if (routeList.getSelectedValue() != null && !ev.getValueIsAdjusting()) {
					routeToTraverse += routeList.getSelectedValue()+" - ";
					routePlannedTextField.setText(routeToTraverse);
					routeListToTraverse.add(routeList.getSelectedValue());
					routeList.clearSelection();
				}
			}
		});
		routeList.setPreferredSize(new Dimension(300, 0));
		routePane = new JScrollPane(routeList);
		
		frame.getContentPane().add(routePane, BorderLayout.WEST);
		
		// ================== UI Main frame settings =======================
		
		functionalityComboBox.setSelectedIndex(0);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension windowSize = new Dimension(800, 600);
		frame.setSize(windowSize.width, windowSize.height);
		frame.setLocation(screenSize.width/2-windowSize.width/2, screenSize.height/2-windowSize.height/2);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
	}
	
	// called on "Functionality" ComboBox change
	private void functionalityComboBox_Change() {
		switch (functionalityComboBox.getSelectedIndex()) {
			
			// Calculate Distance
			case 0:
				hideAllResults();
				routeShortestResultLabel.setText("You can get there faster by going this way instead : ");
				
				routePossibleLabel.setVisible(true);
				routePossibleTextField.setVisible(true);
				break;
			
			// Journey Planner
			case 1:
				hideAllResults();
				routeShortestResultLabel.setText("Possible routes are :");
				routeShortestResultTextField.setText("Feature not yet implemented! Sorry for inconvenience");
				routeShortestResultLabel.setVisible(true);
				routeShortestResultTextField.setVisible(true);
				break;
			
			// Shortest Route
			case 2:
				hideAllResults();
				routeShortestResultLabel.setText("Shortest route between stations : ");
				
				routeShortestResultLabel.setVisible(true);
				routeShortestResultTextField.setVisible(true);
				
				routeShortestDistanceLabel.setVisible(true);
				routeShortestDistanceTextField.setVisible(true);
				break;
		}
	}
	
	// called on "Calculate" button click
	private void calculateButton_Click() {
		switch (functionalityComboBox.getSelectedIndex()) {
			
			// Calculate Distance
			case 0:
				findShortestRoute();
				String pathPossible = "";
				if (route.testPathPossible(routeListToTraverse))
					pathPossible = "Possible";
				else
					pathPossible = "Not possible";
				routePossibleTextField.setText(pathPossible);
				double distance = route.getDistance();
				routeDistanceTextField.setText(distance+"");
				if (distance > route.getShortestDistance() || distance <= 0.0) {
					routeShortestResultLabel.setVisible(true);
					routeShortestResultTextField.setVisible(true);
					
					routeShortestDistanceLabel.setVisible(true);
					routeShortestDistanceTextField.setVisible(true);
				}
				if (distance <= 0.0) {
					routeShortestResultLabel.setText("But you can get there by going this way : ");
				} else {
					routeDistanceLabel.setVisible(true);
					routeDistanceTextField.setVisible(true);
				}
				break;
			
			// Journey Planner
			case 1:
				// Functionality not yet fully implemented
				// TODO: Add functionality here, preferable JList<String> returned by
				//		new method in WeightedGraph that would return an ArrayList<ArrayList<String>
				// TODO: Add further user input for max number of stops / exact number of stops
				break;
			
			// Shortest Route
			case 2:
				findShortestRoute();
				break;
		}
	}
	
	// called on "Reset" button click
	private void resetButton_Click() {
		routePlannedTextField.setText("");
		routePossibleTextField.setText("");
		routeDistanceTextField.setText("");
		routeShortestResultTextField.setText("");
		routeShortestDistanceTextField.setText("");
		
		routeToTraverse = "";
		routeListToTraverse = new ArrayList<String>();
		
		routeList.clearSelection();
		
		hideAllResults();
	}
	
	private void hideAllResults() {
		routePossibleLabel.setVisible(false);
		routePossibleTextField.setVisible(false);
		
		routeDistanceLabel.setVisible(false);
		routeDistanceTextField.setVisible(false);
		
		routeShortestResultLabel.setVisible(false);
		routeShortestResultTextField.setVisible(false);
		
		routeShortestDistanceLabel.setVisible(false);
		routeShortestDistanceTextField.setVisible(false);
		
		functionalityComboBox.setSelectedIndex(functionalityComboBox.getSelectedIndex());
	}
	
	// Shortest Route
	private void findShortestRoute() {
		ArrayList<String> bestPath = route.findBestPath(routeListToTraverse);
		if (bestPath.contains("Not possible")) {
			bestPath.removeAll(bestPath);
			bestPath.add("Not possible");
		}
		routeShortestResultTextField.setText(bestPath.toString());
		routeShortestDistanceTextField.setText(route.getShortestDistance()+"");
	}
	
}