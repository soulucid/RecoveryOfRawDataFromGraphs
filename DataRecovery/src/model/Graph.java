/***
 * 
 * @author Yingyi Zhang
 * @username yxz559
 * @studentID 1544458
 * 
 */

package model;

import java.util.ArrayList;

/**
 * 
 * Model of a graph
 * 
 */
public class Graph {

	private String name;
	private int type;  // 1-broken line chart, 2-curve chart
	private ArrayList<GraphPoint> data;

	// Constructor function
	public Graph(int type){
		
		this.type = type;
		
	}
	
	/**
	 * @return the graph name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the graph type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @return the graph data
	 */
	public ArrayList<GraphPoint> getData() {
		return data;
	}

	/**
	 * @param data the graph data to set
	 */
	public void setData(ArrayList<GraphPoint> data) {
		this.data = data;
	}
	
}
