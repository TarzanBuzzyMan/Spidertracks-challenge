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
	JTextField routeResultTextField = null;
	JTextField routeDistanceTextField = null;
	
	JLabel routePlannedLabel = null;
	JLabel routeResultLabel = null;
	JLabel routeDistanceLabel = null;
	
	JButton calculateButton = null;
	JButton resetButton = null;
	
	JList<String> routeList = null;
	JScrollPane routePane = null;
	String routeToTraverse = "";
	ArrayList<String> routeListToTraverse = null;
	
	String[] cityNodes = null;
	WeightedGraph route = null;
	
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
		strip.add(calculateButton);
		strip.add(resetButton);
		
		frame.getContentPane().add(strip, BorderLayout.NORTH);
		frame.getRootPane().setDefaultButton(calculateButton);
		
		// ================== UI Right hand Results panel =======================
		
		routePlannedTextField = new JTextField();
		routePlannedTextField.setPreferredSize(new Dimension(350, 30));
		
		routePlannedLabel = new JLabel("Start and end places to calculate : ");
		routePlannedLabel.setPreferredSize(new Dimension(50, 30));
		
		JPanel labeledRoutePlanned = new JPanel(new BorderLayout());
		labeledRoutePlanned.add(routePlannedLabel, BorderLayout.NORTH);
		labeledRoutePlanned.add(routePlannedTextField, BorderLayout.CENTER);
		
		routeResultTextField = new JTextField();
		routeResultTextField.setEditable(false);
		routeResultTextField.setPreferredSize(new Dimension(350, 30));
		
		routeResultLabel = new JLabel("Shortest route : ");
		routeResultLabel.setPreferredSize(new Dimension(50, 30));
		
		JPanel labeledRouteResult = new JPanel(new BorderLayout());
		labeledRouteResult.add(routeResultLabel, BorderLayout.NORTH);
		labeledRouteResult.add(routeResultTextField, BorderLayout.CENTER);
		
		routeDistanceLabel = new JLabel("Estimated distance along route : ");
		routeDistanceLabel.setPreferredSize(new Dimension(50, 30));
		
		routeDistanceTextField = new JTextField();
		routeDistanceTextField.setEditable(false);
		routeDistanceTextField.setPreferredSize(new Dimension(350, 30));
		
		JPanel labeledRouteDistance = new JPanel(new BorderLayout());
		labeledRouteDistance.add(routeDistanceLabel, BorderLayout.NORTH);
		labeledRouteDistance.add(routeDistanceTextField, BorderLayout.CENTER);
		
		JPanel results = new JPanel(new FlowLayout(FlowLayout.CENTER));
		results.add(labeledRoutePlanned);
		results.add(labeledRouteResult);
		results.add(labeledRouteDistance);
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
				}
			}
		});
		routeList.setPreferredSize(new Dimension(300, 0));
		routePane = new JScrollPane(routeList);
		
		frame.getContentPane().add(routePane, BorderLayout.WEST);
		
		// ================== UI Main frame settings =======================
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension windowSize = new Dimension(800, 600);
		frame.setSize(windowSize.width, windowSize.height);
		frame.setLocation(screenSize.width/2-windowSize.width/2, screenSize.height/2-windowSize.height/2);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
	}
	
	// called on "Calculate" button click
	private void calculateButton_Click() {
		ArrayList<String> bestPath = route.findBestPath(routeListToTraverse);
		if (bestPath.contains("Not possible")) {
			bestPath.removeAll(bestPath);
			bestPath.add("Not possible");
		}
		routeResultTextField.setText(bestPath.toString());
		routeDistanceTextField.setText(route.getDistance()+"");
	}
	
	// called on "Reset" button click
	private void resetButton_Click() {
		routePlannedTextField.setText("");
		routeResultTextField.setText("");
		routeDistanceTextField.setText("");
		
		routeToTraverse = "";
		routeListToTraverse = new ArrayList<String>();
		
		routeList.clearSelection();
	}
	
}