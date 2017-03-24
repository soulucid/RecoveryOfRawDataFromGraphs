/***
 * 
 * @author Yingyi Zhang
 * @username yxz559
 * @studentID 1544458
 * 
 */

package model;

/**
 * 
 * Model of a point in the graph
 * 
 */
public class GraphPoint {
	
	private double x;
	private double y;
	
	// Constructor function
	public GraphPoint(double x, double y){
		
		this.setX(x);
		this.setY(y);
		
	}
	
	/**
	 * Print the graph point
	 */
	public void print(){
		
		System.out.println("(" + this.getX() + ", " + this.getY() + ")");
		
	}
	
	/**
	 * @return (x,y)
	 * 
	 * Return the graph point as a string
	 */
	public String getCoordinates(){
		
		String coOrdinates = "(" + this.getX() + ", " + this.getY() + ")";
		
		return coOrdinates;
		
	}

	/**
	 * @return the x
	 */
	public double getX() {
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public double getY() {
		return y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(double y) {
		this.y = y;
	}

}
