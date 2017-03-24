/***
 * 
 * @author Yingyi Zhang
 * @username yxz559
 * @studentID 1544458
 * 
 */

package processGraph;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

/**
 * 
 * Classify graphs from images
 * 
 */
public class GraphClassifier {
	
	String imagePath;
	
	public GraphClassifier(String imagePath){
		
		this.imagePath = imagePath;
		
	}

	/**
	 * @return 0 for successful
	 *         1 for fail
	 * 
	 * Load the trained classifier and classify graphs
	 * from images and save the images to the 
	 * directory graphs/
	 */
	public int calssify() {
		
		// Load the OpenCV library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        
        // Specify the classifier
		CascadeClassifier graphClassifier = new CascadeClassifier();
		String cascadeName = "trainedClassifier/classifier3-650-10stages/cascade.xml";
		
		// Load the image
        Mat image = Imgcodecs.imread(imagePath);
        
        // If the image is too small, do not classify it
        if (image.width() < 400 || image.height() < 300 || image.width()/image.height() >= 2){
        	
        	System.err.println("The image's size is not qualified.");
        	return 1;
        	
        }
        	
        // Transform the image into gray scale
        Mat grayMat = new Mat();
        Imgproc.cvtColor(image, grayMat, Imgproc.COLOR_BGR2GRAY);
        
        // Resize the image, let its width:height = 4:3
        double ratio = (grayMat.height()*(4d/3d))/grayMat.width();
        Mat resizeMat = new Mat();
        Imgproc.resize(grayMat, resizeMat, new Size(), ratio, 1, Imgproc.INTER_LINEAR);
        
        // Load the classifier
		graphClassifier.load(cascadeName);
		
		// Get the detected objects
		MatOfRect objects = new MatOfRect();
		graphClassifier.detectMultiScale(grayMat, objects, 1.1, 1, 0, new Size(340, 255), new Size(900, 675));
		
		System.out.println("The object's size: " + objects.size());
		System.out.println(objects.dump());
		
		// Remove the rectangle whose position is less likely to be as a part of a graph
		Rect[] lstOfRect = objects.toArray();
		int rectNumber = lstOfRect.length;
		
		if (lstOfRect.length > 0){
		
			for (int i = 0; i < lstOfRect.length; i++){
				
				System.out.println("The rect: " + lstOfRect[i]);
			
				if ((lstOfRect[i].x >= 200 && lstOfRect[i].y >= 200) || 
						(Math.abs(lstOfRect[i].x - lstOfRect[i].y) > 200) || 
						(lstOfRect[i].x >= 500 || lstOfRect[i].y >= 500)){
				
					rectNumber --;
				
				}
			
			}
			
		}
		else {
			
			System.out.println("This is not a graph.");
			return 0;
			
		}
		
		// If there still are some rectangle containers left, 
		// then this image may be a graph
		if (rectNumber > 0){
			
			// If there are x and y axes in the image, then this image has
			// a high possibility to be a graph
			Imgproc.Canny(grayMat, grayMat, 50, 200);
			int x = AxisDetector.axisDetect(grayMat, 'x');
			int y = AxisDetector.axisDetect(grayMat, 'y');
			
			System.out.println("The x axis: " + x);
			System.out.println("The y axis: " + y);
			
			if (x > grayMat.height() * 0.75 && y < grayMat.width() * 0.2 && y > grayMat.width() * 0.1){
				Imgcodecs.imwrite("graphs/" + imagePath.substring(10), image);
				System.out.println("This is a graph.");
				return 0;
			}
			else {
				
				System.out.println("This is not a graph.");
				return 0;
				
			}
			
		}
		// Else the images may not be a graph
		else {
			
			System.out.println("This is not a graph.");
			return 0;
			
		}

	}

}
