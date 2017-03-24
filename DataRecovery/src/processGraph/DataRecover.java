/***
 * 
 * @author Yingyi Zhang
 * @username yxz559
 * @studentID 1544458
 * 
 */

package processGraph;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import model.Graph;
import model.GraphPoint;
import processImage.ImageOperations;

/**
 * 
 * Data recovery functions, include recognise
 * scale numbers, separate the curves, recover
 * the data of all the data points
 * 
 */
public class DataRecover {
	
	/**
	 * @param xAxis
	 * @return the x axis scale numbers
	 * 
	 * X axis scale numbers recognition
	 */
	public static ArrayList<Double> xScaleRecog(Mat xAxis){
		
		// Load the OpenCV library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        ArrayList<Integer> bound = new ArrayList<Integer>();
        ArrayList<Double> xScaleNumbers = new ArrayList<Double>();
		
        // If the X scale numbers region is too small, the picture
        // might not be pre-processed correctly, abandon it
        if (xAxis.height() <= 10){
        	
        	System.err.println("Can not recognise X axis scale numbers.");
        	return xScaleNumbers;
        	
        }
        
		Mat processedX = ImageOperations.preProcess(xAxis, 1);
		
		// Extract the scale number region
		for (int i = 0; i <= 0.95 * processedX.height(); i++){
			
			int countPixel = 0;
			
			for (int j = 0; j < processedX.width(); j++){
				
				byte[] data = new byte[1];
				processedX.get(i, j, data);
				if (data[0] != 0){
					
					countPixel ++;
					
				}
				
			}
			
			if (countPixel >= processedX.width() * 0.99){
				
				bound.add(i);
				
			}
			
		}
		
		// Find the up bound and down bound of the region which contains X scale numbers
		int boundFlag = 0;
		int upBoundFlag = 0;
		int scaleNumberUp = 0;
		int scaleNumberDown = 0;
		
		for(int m = 0; m < bound.size() - 1 && boundFlag == 0; m++){
			
			if (bound.get(m + 1) != bound.get(m) + 1){
				
				if (upBoundFlag == 0){
					
					scaleNumberUp = bound.get(m);
					upBoundFlag = 1;
					
				}
				
				if (bound.get(m + 1) - bound.get(m) < 15){
					
					continue;
					
				}
				else {
					
					scaleNumberDown = bound.get(m + 1);
					boundFlag = 1;
				
				}
				
			}
			
		}
 		
//		System.out.println("The up bound of x scale numbers region is: " + scaleNumberUp);
//		System.out.println("The down bound of x scale number region is: " + scaleNumberDown);
		
		if (scaleNumberUp == 0 && scaleNumberDown == 0){
			
			if (bound.size() == 0){
				
				scaleNumberUp = 0;
				scaleNumberDown = processedX.height() - 1;
				
			}
			else {
			
				scaleNumberUp = bound.get(bound.size() - 1);
				scaleNumberDown = processedX.height() - 1;
				
			}
			
		}
		
		Rect roiNumber = new Rect(0, scaleNumberUp, processedX.width(), scaleNumberDown - scaleNumberUp);
		Mat roiNumberMat = new Mat();
		// Cut the x axis ROI out
		try {
			
			roiNumberMat = processedX.submat(roiNumber);
		}
		catch (Exception e){
			System.err.println("Can't extract the X axis scale number region correctly.");
			return xScaleNumbers;
		}

 		// Sharpen the image to improve the number recognition result
 		Imgproc.pyrUp(roiNumberMat, roiNumberMat);
 		roiNumberMat = ImageOperations.SharpenImage(roiNumberMat);
		
 		// Recognise X axis scale numbers using TesseractOCR
		BufferedImage buffImg = ImageOperations.transformToImg(roiNumberMat);
//		ImageOperations.showImage(buffImg);
		ITesseract instance = new Tesseract();
		String xNumberResult = "";
		
		try {
			
			xNumberResult = instance.doOCR(buffImg);
//			System.out.println(xNumberResult);
			
		} catch(TesseractException e) {
			
			System.err.println(e.getMessage());
			
		}
		
		// Return an empty ArrayList if there is no x axis scale numbers recognised
		if (xNumberResult.isEmpty()){
			
			System.err.println("Can not recognise X axis scale numbers.");
			return xScaleNumbers;
			
		}
		
		// Split the string to get X axis scale numbers
		String[] xScale = xNumberResult.trim().split(" +");
		
		try {
			xScaleNumbers = scaleRecover(xScale);
		}
		catch (Exception e){
			System.err.println("The X scale numbers contain characters other than numbers,"
					+ " so can't be recovered.");
		}
		
		return xScaleNumbers;
		
	}
	
	/**
	 * @param yAxis
	 * @return the y axis scale numbers
	 * 
	 * Y axis scale numbers recognition
	 */
	public static ArrayList<Double> yScaleRecog(Mat yAxis){
		
		// Load the OpenCV library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        ArrayList<Integer> bound = new ArrayList<Integer>();
        ArrayList<Double> yScaleNumbers = new ArrayList<Double>();
		
		Mat processedY = ImageOperations.preProcess(yAxis, 2);
		
		// Extract the scale number region
		for (int j = (int)(0.15 * processedY.width()); j < processedY.width(); j++){
			
			int countPixel = 0;
			
			for (int i = 0; i < processedY.height(); i++){
				
				byte[] data = new byte[1];
				processedY.get(i, j, data);
				if (data[0] != 0){
					
					countPixel ++;
					
				}
				
			}
			
			if (countPixel == yAxis.height()){
				
				bound.add(j);
				
			}
			
		}

		// Find the left bound and right bound of the region which contains Y scale numbers
		int boundFlag = 0;
		int scaleNumberLeft = 0;
		int scaleNumberRight = 0;
 		
		for (int n = 0; n < bound.size() && boundFlag == 0; n++){
			
			if (bound.get(n) + 1 != bound.get(n + 1)){
				
				scaleNumberLeft = bound.get(n);
				boundFlag = 1;
				
			}
			
		}
		boundFlag = 0;
		for (int n = bound.size() - 1; n >=0 && boundFlag == 0; n--){
			
			if (bound.get(n) - 1 != bound.get(n - 1)){
				
				scaleNumberRight = bound.get(n);
				boundFlag = 1;
				
			}
			
		}
		
//		System.out.println("The left bound of y scale numbers region is: " + scaleNumberLeft);
//		System.out.println("The right bound of y scale numbers region is: " + scaleNumberRight);
		
 		Rect roiNumber = new Rect(scaleNumberLeft, 0, scaleNumberRight - scaleNumberLeft, processedY.height());
 		Mat roiNumberMat = new Mat();
 		// Cut the y axis ROI out
 		try {
 			roiNumberMat = processedY.submat(roiNumber);
 		}
 		catch (Exception e){
 			System.err.println("Can't extract the Y axis scale number region correctly.");
 			return yScaleNumbers;
 		}
 		
		// Sharpen the image to improve the number recognition result
 		Imgproc.pyrUp(roiNumberMat, roiNumberMat);
 		roiNumberMat = ImageOperations.SharpenImage(roiNumberMat);
		
 		// Recognise Y axis scale numbers using TesseractOCR
		BufferedImage buffImg = ImageOperations.transformToImg(roiNumberMat);
//		ImageOperations.showImage(buffImg);
		ITesseract instance = new Tesseract();
		String yNumberResult = "";
		
		try {
			
			yNumberResult = instance.doOCR(buffImg);
//			System.out.println(yNumberResult);
			
		} catch(TesseractException e) {
			
			System.err.println(e.getMessage());
			
		}
		
		// Return an empty ArrayList if there is no y axis scale numbers recognised
		if (yNumberResult.isEmpty()){
			
			System.err.println("Can not recognise Y axis scale numbers.");
			return yScaleNumbers;
			
		}
		
		// Y axis scale numbers
		String[] yScale = yNumberResult.trim().split("\n+");
		
		try {
			yScaleNumbers = scaleRecover(yScale);
		}
		catch (Exception e){
			System.err.println("The Y scale numbers contain characters other than numbers,"
					+ " so can't be recovered.");
		}
		
		return yScaleNumbers;
		
	}	
	
	/**
	 * @param scaleNumber
	 * @return the recover result
	 * 
	 * Recover the scale numbers from string to double
	 */
	public static ArrayList<Double> scaleRecover(String[] scaleNumber){
		
		ArrayList<Double> scale = new ArrayList<Double>(); 
			
		for (String s : scaleNumber){
			
			// Replace some unusual characters
			s = textReplace(s);
			
			if (s.startsWith("(") || 
					s.startsWith("`") || 
					s.startsWith("'") || 
					s.startsWith("¡®") || 
					s.isEmpty()){
				
				continue;
				
			}
			
			scale.add(Double.parseDouble(s));
			
		}
		
		return scale;
		
	}
	
	/**
	 * @param graphBody
	 * @param xNums
	 * @param yNums
	 * @return A graph object
	 * 
	 * Recover the data of all the data points
	 */
	public static Graph dataRecover(Mat graphBody, ArrayList<Double> xNums, ArrayList<Double> yNums){
		
		// Load the OpenCV library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        
        // Detect is there any straight lines in the graph body
        Mat grayMat = new Mat();
        Imgproc.cvtColor(graphBody, grayMat, Imgproc.COLOR_BGR2GRAY);
		Imgproc.Canny(grayMat, grayMat, 50, 200);
		
		Mat lines = new Mat();
		int threshold = (grayMat.width() / 100) * 18;
		Imgproc.HoughLines(grayMat, lines, 1, Math.PI/270, threshold);
        
        graphBody = ImageOperations.preProcess(graphBody, 1);
        
        // If there are straight lines, then this graph is a broken line chart
        if (lines.rows() > 0){
        	
        	Graph graph = new Graph(1);
        	graph.setData(recoverBrokenLineChart(graphBody, xNums, yNums));
        	return graph;
        	
        }
        // Else this is a curve chart
        else {
        	
        	Graph graph = new Graph(2);
        	graph.setData(recoverCurveChart(graphBody, xNums, yNums));
        	return graph;
        	
        }
		
	}
	
	/**
	 * @param mat
	 * @param xNums
	 * @param yNums
	 * @return ArrayList<GraphPoint> contains the recovered data
	 * 
	 * Recover the data of broken line chart
	 */
	public static ArrayList<GraphPoint> recoverBrokenLineChart(Mat mat, ArrayList<Double> xNums, ArrayList<Double> yNums){
		
		ArrayList<GraphPoint> gpList = new ArrayList<GraphPoint>();
		
		int step = Math.round((float)mat.width()/(float)(xNums.size() - 1));
		
		// Get the data points in the graph
		for (int j = 0; j <mat.width(); j += step){
    		
    		for (int i = 0; i < 0.9 * mat.height(); i++){
    			
    			byte[] data = new byte[1];
				mat.get(i, j, data);
				if (data[0] == 0){
					
					GraphPoint gp = new GraphPoint(j, i);
					gpList.add(gp);
					break;
					
				}
    			
    		}
    		
		}
		
		// Add the missed last data point
		if (gpList.size() < xNums.size()){
			
			for (int i = 0; i < 0.9 * mat.height(); i++){
			
				byte[] data = new byte[1];
				mat.get(i, mat.width() - 1, data);
				if (data[0] == 0){
				
					GraphPoint gp = new GraphPoint(mat.width() - 1, i);
					gpList.add(gp);
					break;
				
				}
			
			}
		}
		
		// Transform the data point from pixel coordinates to real data coordinates
		ArrayList<GraphPoint> gpList2 = new ArrayList<GraphPoint>();
    	double differenceY = yNums.get(1) - yNums.get(0);
    	
    	for (int j = 0; j < gpList.size(); j++){
    		
    		double positionY = (gpList.get(j).getY()/mat.height())*(yNums.size()- 1);
    		double Y = (positionY - Math.floor(positionY)) * differenceY + yNums.get((int)Math.floor(positionY));
    		double X = xNums.get(j);
    		GraphPoint gp = new GraphPoint(X, Y);
    		gpList2.add(gp);
    		
    	}
		
		return gpList2;
		
	}
	
	/**
	 * @param mat
	 * @param xNums
	 * @param yNums
	 * @return ArrayList<GraphPoint> contains the recovered data
	 * 
	 * Recover the data of curve chart
	 */
	public static ArrayList<GraphPoint> recoverCurveChart(Mat mat, ArrayList<Double> xNums, ArrayList<Double> yNums){
		
		ArrayList<GraphPoint> gpList = new ArrayList<GraphPoint>();
    	
		// In curve chart, pick points in 10 pixels' step along X axis
    	for (int j = 10; j <mat.width(); j += 10){
    		
    		for (int i = 0; i < 0.9 * mat.height(); i++){
    			
    			byte[] data = new byte[1];
				mat.get(i, j, data);
				if (data[0] == 0){
					
					GraphPoint gp = new GraphPoint(j, i);
					gpList.add(gp);
					break;
					
				}
    			
    		}
    		
    	}
    	
    	// Transform the data point from pixel coordinates to real data coordinates
    	ArrayList<GraphPoint> gpList2 = new ArrayList<GraphPoint>();
    	double differenceX = xNums.get(1) - xNums.get(0);
    	double differenceY = yNums.get(1) - yNums.get(0);
    	
    	for (int j = 0; j < gpList.size(); j++){
    		
    		double positionX = (gpList.get(j).getX()/mat.width())*(xNums.size()- 1);
    		double positionY = (gpList.get(j).getY()/mat.height())*(yNums.size()- 1);
    		double X = (positionX - Math.floor(positionX)) * differenceX + xNums.get((int)Math.floor(positionX));
    		double Y = (positionY - Math.floor(positionY)) * differenceY + yNums.get((int)Math.floor(positionY));
    		GraphPoint gp = new GraphPoint(X, Y);
    		gpList2.add(gp);
    		
    	}
    	
    	return gpList2;
		
	}
	
	/**
	 * @param s
	 * @return processed string s
	 * 
	 * Replace some unusual characters in a string, 
	 * in order to improve the accuracy of OCR
	 */
	public static String textReplace(String s){
		
		if (s.contains("o")){
			
			s = s.replace("o", "0");
			
		}
		if (s.contains("O")){
			
			s = s.replace("O", "0");
			
		}
		if (s.contains("S")){
			
			s = s.replace("S", "5");
			
		}
		if (s.contains("s")){
			
			s = s.replace("s", "5");
			
		}
		if (s.contains("I")){
			
			s = s.replace("I", "1");
			
		}
		if (s.contains("[")){
			
			s = s.replace("[", "1");
			
		}
		if (s.contains("l")){
			
			s = s.replace("l", "1");
			
		}
		if (s.contains("¡ª")){
			
			s = s.replace("¡ª", "-");
			
		}
		if (s.contains("Z")){
			
			s = s.replace("Z", "2");
			
		}
		if (s.contains("~")){
			
			s = s.replace("~", "-");
			
		}
		if (s.contains("..")){
			
			s = s.replace("..", "");
			
		}
		if (s.contains("¡®")){
			
			s = s.replace("¡®", "");
			
		}
		if (s.contains("V")){
			
			s = s.replace("V", "");
			
		}
		if (s.endsWith(".")){
			
			s = s.substring(0, s.length() - 1);
			
		}
		if (s.contains("\")")){
			
			s = s.replace("\")", "9");
			
		}
		if (s.contains("U")){
			
			s = s.replace("U", "0");
			
		}
		if (s.contains("1C")){
			
			s = s.replace("1C", "E");
			
		}
		if (s.contains(")")){
			
			s = s.replace(")", "");
			
		}
		if (s.contains("]")){
			
			s = s.replace("]", "1");
			
		}
		if (s.startsWith(".")){
			
			s = s.replace(".", "-");
			
		}
		if (s.startsWith("{")){
			
			s = s.replace("{", "-");
			
		}
		
		return s;
	
	}
	
}
