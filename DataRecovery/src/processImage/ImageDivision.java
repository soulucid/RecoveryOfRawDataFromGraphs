/***
 * 
 * @author Yingyi Zhang
 * @username yxz559
 * @studentID 1544458
 * 
 */

package processImage;

import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;

/**
 * 
 * This class is used to pre-process images, 
 * so it will be easier for us to recognise 
 * and process graphs.
 * 
 */
public class ImageDivision {
	
    static String filepath;
    static String imageName;
    static int count;

    /**
     * @param filepath
     * 
     * Constructor function
     */
    public ImageDivision(String filepath){
    	
    	this.filepath = filepath;
    	this.imageName = filepath.substring(7, filepath.length() - 4);
    	this.count = 0;
    	
    }
    
    /**
     * Start the image division
     */
    public int start(){
    	
    	// Load the OpenCV library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
                
        // Load the image
        Mat image = Imgcodecs.imread(getFilepath());
        
        // Abandon the images that are too small and return
        if (image.width() < 300 || image.height() < 300){
        	
        	System.err.println("The image is too small.");
        	return 1;
        	
        }

        Mat binaryMat = ImageOperations.preProcess(image, 2);
        
        // Cut the space border off the image before division
        Rect cutROI = ImageOperations.cutBorder(binaryMat);
        binaryMat = binaryMat.submat(cutROI);
        
        try {
        	divideImage(binaryMat, image);
        }
        catch (Exception e){
        	return 1;	
        }
        
        return 0;
    	
    }
	
	/**
	 * @return the filepath
	 */
	public static String getFilepath() {
		return filepath;
	}

	/**
	 * @param binaryMat
	 * @param originalMat
	 * 
	 * Divide the image into several regions
	 */
	protected static void divideImage(Mat binaryMat, Mat originalMat){
		
		ArrayList<Integer> rowSpace = new ArrayList<Integer>();
		ArrayList<Integer> columnSpace = new ArrayList<Integer>();
		ArrayList<Integer> tmp = new ArrayList<Integer>();
		ArrayList<Integer> rowDivisionLine = new ArrayList<Integer>();
		ArrayList<Integer> columnDivisionLine = new ArrayList<Integer>();
		
		// Calculate the possible space rows in the image
		for (int y = 0; y < binaryMat.height(); y++){
        	
        	int countPixel = 0;
   	
        	for (int x = 0; x < binaryMat.width(); x++){
        		
        		byte[] data = new byte[1];
        		binaryMat.get(y, x, data);
        		if (data[0] != 0){
        			
        			countPixel++;

        		}
        	}
        	
//        	System.out.println("The countPixel " + countPixel);
        	
        	// Don't allow noisy point in rows
        	if (countPixel == binaryMat.width()){
        		
        		rowSpace.add(y);
        		
        	}
        	
		}
		
		// Find candidate space rows to divide the image
		System.out.println("The picture's height is " + binaryMat.height());
		for (int i = 0; i < rowSpace.size(); i++){
			
			// Remove the candidate space which is
			// too close to the up and bottom edge of the image
			if (rowSpace.get(i) < 0.2 * binaryMat.height() || 
				rowSpace.get(i) > 0.8 * binaryMat.height()){
				
				rowSpace.remove(i);
				i --;
				
			}
			else
				continue;
			
		}
		
		// Remove the candidate space which is too narrow,
		// only keep the space rows which is wide enough,
		// so that the image will not be divided into too many parts
		int m = 0;
		for (int j = 0; j < rowSpace.size(); j++){
			
			if (j < rowSpace.size() - 1 && rowSpace.contains(rowSpace.get(j) + 1)){
				
				tmp.add(rowSpace.get(j));
				m ++;
				
			}
			else {
				
				if (m > 0.01 * binaryMat.height()){
					
					rowDivisionLine.add(tmp.get(tmp.size()/2));
//					System.out.println("The candidate row space " + tmp.get(tmp.size()/2));
					m = 0;
					tmp.clear();

				}
				else {
					
					m = 0;
					tmp.clear();
					continue;
										
				}
				
			}
			
		}
		
		// Remove the candidate space which is too close to the next one
		for (int i = 0; i < rowDivisionLine.size() - 1; i++){
			
			if (binaryMat.height() - rowDivisionLine.get(i) < 100 || 
					rowDivisionLine.get(i + 1) - rowDivisionLine.get(i) < 100){
				
				rowDivisionLine.remove(i);
				if (rowDivisionLine.size() == 1){
					
					break;
					
				}
				i--;
				
			}
			
		}
				
		// Calculate the possible space columns in the image
		for (int x = 0; x < binaryMat.width(); x++){
        	
        	int countPixel = 0;
   	
        	for (int y = 0; y < binaryMat.height(); y++){
        		
        		byte[] data = new byte[1];
        		binaryMat.get(y, x, data);
        		if (data[0] != 0){
        			
        			countPixel++;

        		}
        	}
        	
//        	System.out.println("The countPixel " + countPixel);
        	
        	// Don't allow noisy points in column
        	if (countPixel == binaryMat.height()){
        		
        		columnSpace.add(x);
        		
        	}
        	
		}
		
		// Find candidate space columns to divide the image
		System.out.println("The picture's width is " + binaryMat.width());
		for (int i = 0; i < columnSpace.size(); i++){
			
			// Remove the candidate space which is
			// too close to the left and right edge of the image
			if (columnSpace.get(i) < 0.1 * binaryMat.width() || 
				columnSpace.get(i) > 0.9 * binaryMat.width()){
					
					columnSpace.remove(i);
					i --;
					
				}
				else
					continue;
			
		}
		
		// Remove the candidate space which is too narrow,
		// only keep the space columns which is wide enough,
		// so that the image will not be divided into too many parts
		int n = 0;
		for (int j = 0; j < columnSpace.size(); j++){
			
			if (j < columnSpace.size() - 1 && columnSpace.contains(columnSpace.get(j) + 1)){
				
				tmp.add(columnSpace.get(j));
				n ++;
				
			}
			else {
				
				if (n > 0.015 * binaryMat.width()){
					
					columnDivisionLine.add(tmp.get(tmp.size()/2));
//					System.out.println("The candidate column space " + tmp.get(tmp.size()/2));
					n = 0;
					tmp.clear();
										
				}
				else {
					
					n = 0;
					tmp.clear();
					continue;
										
				}
				
			}
			
		}
		
		// Remove the candidate space which is too close to the next one
		for (int i = 0; i < columnDivisionLine.size() - 1; i++){
			
			if (columnDivisionLine.get(i + 1) - columnDivisionLine.get(i) < 100 || 
					binaryMat.width() - columnDivisionLine.get(i) < 50){
				
				columnDivisionLine.remove(i);
				if (columnDivisionLine.size() == 1){
					
					break;
					
				}
				i--;
				
			}
			
		}
		
		cutImage(originalMat, rowDivisionLine, columnDivisionLine);
		
	}
	
	/**
	 * @param mat
	 * @param row
	 * @param column
	 * 
	 * Cut the image into several sub-images, 
	 * so that it will be easier to recognise and process graphs
	 */
	protected static int cutImage(Mat mat, ArrayList<Integer> row, ArrayList<Integer> column){
		
		// If the image is hard to be divided, just ignore it
		if (row.size() == 0 && column.size() == 0){
				
				furtherCut((double)mat.width(), (double)mat.height(), mat);
				return 0;
			
		}
		
		// Calculate the top left point's coordinate, the width and
		// the height of the ROI (region of interest)
		int x = 0, y = 0, width = 0, height = 0;
		
		for (int i = 0; i <= row.size(); i++){
					
			for (int j = 0; j <= column.size(); j++){
				
				// Abscissa X
				if (j == 0){
					
					x = 0;
					
				}
				else {
					
					x = column.get(j-1);
					
				}
				
				// Ordinate Y
				if (i == 0){
					
					y = 0;
					
				}
				else {
					
					y = row.get(i - 1);
					
				}
				
				// Width
				if (j == 0){
					
					if (column.size() == 0){
						
						width = mat.width();
					
					}
					else {
						
						width = column.get(j);
						
					}
					
				}
				else if (j == column.size()){
					
					width = mat.width() - column.get(j - 1);
					
				}
				else {
					
					width = column.get(j) - column.get(j - 1);
					
				}
				
				// Height
				if (i == 0){
					
					if (row.size() == 0){
						
						height = mat.height();
						
					}
					else {
						
						height = row.get(i);
						
					}
					
				}
				else if (i == row.size()){
					
					height = mat.height() - row.get(i - 1);
					
				}
				else {
					
					height = row.get(i) - row.get(i - 1);
					
				}
				
				Rect roi = new Rect(x, y, width, height);
				Mat roiMat = mat.submat(roi);
				furtherCut((double)width, (double)height, roiMat);
				
			}
			
		}
		
		return 0;
		
	}
	
	/**
	 * @param width
	 * @param height
	 * @param mat
	 * 
	 * Try to cut the image which is still too wide or high
	 */
	public static void furtherCut(double width, double height, Mat mat){
		
		// If the image is too high
		if ((double)height/(double)width >= 1.25){
			
			Rect roiFirst = new Rect(0, 0, mat.width(), mat.height()/2);
			Rect roiSecond = new Rect(0, mat.height()/2, mat.width(), mat.height()/2);
			Mat firstMat = mat.submat(roiFirst);
			Mat secondMat = mat.submat(roiSecond);
			furtherCut((double)firstMat.width(), firstMat.height(), firstMat);
			furtherCut((double)secondMat.width(), secondMat.height(), secondMat);
			
		}
		// If the image is too wide
		else if ((double)width/(double)height >= 2.2){
			
			Rect roiFirst = new Rect(0, 0, mat.width()/2, mat.height());
			Rect roiSecond = new Rect(mat.width()/2, 0, mat.width()/2, mat.height());
			Mat firstMat = mat.submat(roiFirst);
			Mat secondMat = mat.submat(roiSecond);
			furtherCut((double)firstMat.width(), firstMat.height(), firstMat);
			furtherCut((double)secondMat.width(), secondMat.height(), secondMat);
			
		}
		else {
			
			Imgcodecs.imwrite("divImages/" + imageName + "-" + count + ".jpg", mat);
			count ++;
    	
		}
		
	}
	
}
