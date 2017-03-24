/***
 * 
 * @author Yingyi Zhang
 * @username yxz559
 * @studentID 1544458
 * 
 */

package processGraph;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;

import processImage.ImageOperations;
import model.Graph;
import model.GraphPoint;

public class GraphDataRecover {

	protected static ArrayList<String> graphList = new ArrayList<String>();
	protected static ArrayList<Graph> recoveredGraphs1 = new ArrayList<Graph>();
	protected static ArrayList<Graph> recoveredGraphs2 = new ArrayList<Graph>();
	
	public int start() {
		
		getAllGraphs();
		
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		for (String graph : graphList){
			
			// Load the image
			Mat image = Imgcodecs.imread(graph);
			
			Rect roi = ImageOperations.cutBorder(image);
	        image = image.submat(roi);
			
	        // Detect the positions of X and Y axis
			AxisDetector ad = new AxisDetector();
			int xAxis = ad.detect(image, 'x');
			int yAxis = ad.detect(image, 'y');
			
			//Cut the regions of X axis and Y axis
			Rect roiX = new Rect(yAxis - 23, xAxis, image.width()- (yAxis - 23), image.height()-xAxis);
			Mat roiXMat = image.submat(roiX);
			Rect roiY = new Rect(0, 0, yAxis, xAxis + 16);
			Mat roiYMat = image.submat(roiY);
			
			//Recognise the scale numbers
			ArrayList<Double> xScaleNum = DataRecover.xScaleRecog(roiXMat);
	    	ArrayList<Double> yScaleNum = DataRecover.yScaleRecog(roiYMat);
	    	
	    	if (xScaleNum.size() == 0 || yScaleNum.size() == 0){
	    		
	    		System.err.println("Can not recover scale numbers from this graph.");
	    		continue;
	    		
	    	}
	    	
	    	// Unify some wrong scale numbers
	    	xScaleNum = unifyNumbers(xScaleNum);
	    	yScaleNum = unifyNumbers(yScaleNum);
	    	
	    	// If we didn't get any scale numbers, abandon the graph
	    	if (xScaleNum.size() == 0 || yScaleNum.size() == 0){
	    		
	    		System.err.println("Can not recover data from this graph.");
	    		continue;
	    		
	    	}
	    	else {
	    		
	    		System.out.println("The X axis scale numbers: ");
	    		for (int i = 0; i < xScaleNum.size(); i++){
	    			
	    			System.out.println(xScaleNum.get(i));
	    			
	    		}
	    		System.out.println("The Y axis scale numbers: ");
	    		for (int i = 0; i < yScaleNum.size(); i++){
	    			
	    			System.out.println(yScaleNum.get(i));
	    			
	    		}
	    	}
	    	
	    	// Get the region which contains graph body
	    	Rect curveROI = new Rect(yAxis, 0, image.width() - yAxis, xAxis);
	    	Mat curveROIMat = image.submat(curveROI);
	    	
	    	Rect lineROI = ImageOperations.cutBorder(curveROIMat);
	    	curveROIMat = curveROIMat.submat(lineROI);
	    	
	    	Rect cutAxis = new Rect(6, 0, curveROIMat.width() - 6, curveROIMat.height() - 5);
	    	curveROIMat = curveROIMat.submat(cutAxis);
	    	
	    	Graph g = new Graph(0);
	    	
	    	// Recover the data
	    	try {
	    		g = DataRecover.dataRecover(curveROIMat, xScaleNum, yScaleNum);
	    	}
	    	catch (Exception e){
	    		System.err.println("Failed to recover data from this graph.");
	    		continue;
	    	}
	    	
	    	String graphName = graph.substring(7);
	    	g.setName(graphName);
	    	
	    	// Save the data to a text file for future use
	    	System.out.println("Saving the data of image " + g.getName());
	    	ArrayList<GraphPoint> gp = g.getData();
	    	String dataFilePath = "recoveredGraphs/" + g.getName() + "-data.txt";
	    	File dataFile = new File(dataFilePath);
	    	
	    	// If the file does not exist, create it
	    	if (!dataFile.exists()){
	    		try {
					dataFile.createNewFile();
				} catch (IOException e) {
					System.err.println("Failed to create the data txt file.");
					continue;
				}
	    	}
	    	// Otherwise delete the old file and create a new one
	    	else {
	    		
	    		dataFile.delete();
	    		try {
					dataFile.createNewFile();
				} catch (IOException e) {
					System.err.println("Failed to create the data txt file.");
					continue;
				}
	    		
	    	}
	    	
	    	String sep = System.getProperty("line.separator");
	    	
	    	for (int i = 0; i < gp.size(); i++){
	    		
	    		try {
					BufferedWriter output = new BufferedWriter(new FileWriter(dataFilePath,true));
					output.write(gp.get(i).getCoordinates() + sep);
					output.close();
				} catch (IOException e) {
					System.err.println("Failed to write to the file.");
					continue;
				}
	    		
	    	}
	    	
	    	// Also save the original image to the directory
	    	Imgcodecs.imwrite("recoveredGraphs/" + g.getName(), image);
	    	
	    	// Add different types of graphs into different ArrayLists
	    	if (g.getType() == 1){
	    		
	    		recoveredGraphs1.add(g);
	    		
	    	}
	    	else {
	    		
	    		recoveredGraphs2.add(g);
	    		
	    	}
	    	
		}

		/*
		 * A to be improved feature: detect data plagiarism 
		 */
//		PlagiarismDetect pd = new PlagiarismDetect();
//		pd.detect(recoveredGraphs1, 1);
//		pd.detect(recoveredGraphs2, 2);
		
		return 0;

	}
	
	/**
	 * Get all the graphs' name in the graphs/ directory
	 */
	public static void getAllGraphs(){
		
		String graphDirectory = "graphs/";
		File parentDirectory = new File(graphDirectory);
        File[] fileEntries = parentDirectory.listFiles();
        for (File entry : fileEntries){
        	
        	String graphName = graphDirectory + entry.getName();
        	graphList.add(graphName);
        	
        }
		
	}
	
	/**
	 * @param scaleNum
	 * @return unified scale numbers
	 * 
	 * Unify and correct some wrong scale numbers
	 */
	public static ArrayList<Double> unifyNumbers(ArrayList<Double> scaleNum){
		
    	// Format the decimals, avoid the error like 0.4-0.6=-0.1999999996
    	DecimalFormat df = new DecimalFormat("0.00");
    	double difference = 0;
    	difference = Double.valueOf(df.format(scaleNum.get(0) - scaleNum.get(1)));
    	
    	for (int i = 1; i < scaleNum.size() - 1; i++){
    		
    		// Check whether if the scale numbers are equal difference
    		double diff1 = Double.valueOf(df.format(scaleNum.get(i) - scaleNum.get(i + 1)));
    		
    		if (diff1 != difference){
    			
    			// Check whether if this problem is caused by missing minus sign
    			double diff2 = Double.valueOf(df.format(scaleNum.get(i) - (0 - scaleNum.get(i + 1))));
    			
    			if (diff2 != difference){

    				if (i + 1 == scaleNum.size() - 1){
    					
    					scaleNum.set(i + 1, scaleNum.get(i) - difference);
    					
    				}
    				else {
    					
    					// Check whether if this problem was caused by an OCR error
    					double diff3 = Double.valueOf(df.format(scaleNum.get(i) - scaleNum.get(i + 2)));
    				
    					if (diff3 != 2 * difference){
    					
    						System.err.println("The axis scale numbers are not continuous, can not be recovered.");
    						System.err.println("This error might be caused by too many serious character recognise problems.");
    						scaleNum.clear();
    						return scaleNum;
    					
    					}
    					else {
    					
    						scaleNum.set(i + 1, scaleNum.get(i) - difference);
    					
    					}
    				}
    				
    			}
    			else {
    				
    				scaleNum.set(i + 1, scaleNum.get(i) - difference);
    				
    			}
    			
    		}
    		else {
    			
    			continue;
    			
    		}
    		
    	}
    	
    	return scaleNum;
		
	}

}
