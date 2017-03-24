/***
 * 
 * @author Yingyi Zhang
 * @username yxz559
 * @studentID 1544458
 * 
 */

package processGraph;

import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

/**
 * 
 * A naive version of coordinate system detection of the graph
 * TODO: 
 * 1. Need to be able to throw exceptions when an image which 
 *    is difficult to deal with occurs
 * 2. Process several graphs simultaneously
 * 
 */
public class AxisDetector{

    public int detect(Mat image, char axis)
    {
    	
    	// Load the OpenCV library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        
        // Transform the image into gray scale and detect edges using Canny operator
        Mat grayMat = new Mat();
        Imgproc.cvtColor(image, grayMat, Imgproc.COLOR_BGR2GRAY);
		Imgproc.Canny(grayMat, grayMat, 50, 200);
        
        // Allocate the position of the axis
		int axisPosition = axisDetect(grayMat, axis);
		
		return axisPosition;
        
    }
    
    public static int axisDetect(Mat mat, char axis){
    	
    	Mat lines = new Mat();
    	int threshold = 0;
    	int axisPosition = 0;

    	// Detect straight lines that are long enough to be axes
    	switch(axis){
    		
    		case 'x':
    			threshold = (mat.width() / 100) * 75;
    			Imgproc.HoughLines(mat, lines, 1, Math.PI/180, threshold);
    			break;
    		case 'y':
    			threshold = (mat.height() / 100) * 75;
    			Imgproc.HoughLines(mat, lines, 1, Math.PI, threshold);
    			break;
    			
    	}

    	// Save the information(position and angle) of each straight line
    	ArrayList<double[]> line = new ArrayList<double[]>();	
    		
    	for (int i = 0; i < lines.rows(); i++){

    		line.add(lines.get(i, 0));
    			
    	}
    	
    	for (int m = 0; m < line.size(); m++){
    			
    		double rho = line.get(m)[0];
    		double theta = line.get(m)[1];
    		
    		switch(axis){
    		
    			// X axis must be horizontal
    			case 'x':
    				if (Math.abs(Math.PI/2 - theta) < 0.01 && rho > axisPosition && rho < mat.height() * 0.95){
    					
    					axisPosition = (int)rho;
    					
    				}
    				break;
    			// Y axis must be vertical
    			case 'y':
    				if (theta == 0 && axisPosition == 0 && rho > mat.width() * 0.05){
    					
    					axisPosition = (int)rho;
    					
    				}
    				else if (theta == 0 && rho < axisPosition && rho > mat.width() * 0.05){
    					
    					axisPosition = (int)rho;
    					
    				}
    		
    		}

    	}

    	return axisPosition;
    	
    }

}
