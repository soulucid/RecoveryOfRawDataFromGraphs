/***
 * 
 * @author Yingyi Zhang
 * @username yxz559
 * @studentID 1544458
 * 
 */

package processImage;

import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * 
 * All the image operations
 * 
 */
public class ImageOperations {

	/**
	 * Pre-process the images
	 * 1. Transform the colour images into grayscale.
	 * 2. Binaryzation
	 * 3. Remove noisy points
	 * 4. Erosion
	 */
	public static Mat preProcess(Mat originalMat, int typeCode){
		
		// Load the OpenCV library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		// Transform to grayscale
		Mat grayMat = new Mat();
		Imgproc.cvtColor(originalMat, grayMat, Imgproc.COLOR_BGR2GRAY);
		
		// Set an adaptive threshold automatically and do the binaryzation
		Mat binaryMat = new Mat(originalMat.height(), originalMat.width(), CvType.CV_8UC1);
 		Imgproc.threshold(grayMat, binaryMat, 130, 255, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);
 		
 		// Remove noisy points
 		Mat blurMat = new Mat();
 		Imgproc.medianBlur(binaryMat, blurMat, 3);
 		
 		// Erosion, make the outline of objects in the graph much more easy to detect
 		Mat erodeElement = new Mat();
 		switch (typeCode){
 		
 			case 1:
 				erodeElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(1, 1));
 				break;
 			case 2: 
 				erodeElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2, 2));
 				break;
 			case 3:
 				erodeElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3));
 				break;
 		
 		}
 		Imgproc.erode(blurMat, blurMat, erodeElement);
		
		return blurMat;
		
	}
	
	/**
	 * Sharpen the image
	 */
	public static Mat SharpenImage(Mat mat){
		
 		Mat kernel = Mat.zeros(3, 3, CvType.CV_8SC1);
 		Mat temp = kernel.rowRange(1, 2).setTo(new Scalar(-1));
 		temp = kernel.colRange(1, 2).setTo(new Scalar(-1));
 		kernel.put(1, 1, 7);
 		Imgproc.filter2D(mat, mat, mat.depth(), kernel);
		
 		return mat;
 		
	}
	
	/**
	 * Cut the space border off the image
	 */
	public static Rect cutBorder(Mat mat){
		
		int upBorder = 0, downBorder = mat.height(), leftBorder = 0, rightBorder = mat.width();
		
		Mat binaryMat = new Mat(mat.height(), mat.width(), CvType.CV_8UC1);
		
		if (mat.channels() > 1){
			Mat grayMat = new Mat();
			Imgproc.cvtColor(mat, grayMat, Imgproc.COLOR_BGR2GRAY);
			
			Imgproc.threshold(grayMat, binaryMat, 130, 255, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);
		}
		
		else {
			
			binaryMat = mat;
			
		}
		
		// Some images may have a black border, cut it off
		byte[] data = new byte[1];
		binaryMat.get(0, 0, data);
		if (data[0] == 0){
			
			Rect roi = new Rect(10, 10, mat.width() - 20, mat.height() - 20);
			binaryMat = binaryMat.submat(roi);
			
		}
		
		int flag = 0;
		
		// Find the up border
		for (int i = 0; i < binaryMat.height() * 0.5 && flag == 0; i++){
			
			int count = 0;
			
			for (int j = 0; j < binaryMat.width(); j++){
				
				binaryMat.get(i, j, data);
				if (data[0] != 0){
					
					count ++;
					
				}
				
			}
			
			if (count != binaryMat.width()){
				
				if (i == 0){
					
					upBorder = 0;
					flag = 1;
					
				}
				else {
					
					upBorder = i - 1;
					flag = 1;
				
				}
				
			}
			
		}
		
		flag = 0;
		// Find the down border
		for (int i = binaryMat.height() - 1; i >= binaryMat.height() * 0.5 && flag == 0; i--){
			
			int count = 0;
			for (int j = 0; j < binaryMat.width(); j++){
				
				binaryMat.get(i, j, data);
				if (data[0] != 0){
					
					count ++;
					
				}
				
			}
			
			if (count != binaryMat.width()){
				
				downBorder = i + 1;
				flag = 1;
				
			}
			
		}
		
		flag = 0;
		// Find the left border
		for (int j = 0; j < binaryMat.width() * 0.5 && flag == 0; j++){
			
			int count = 0;
			for (int i = 0; i <binaryMat.height(); i++){
				
				binaryMat.get(i, j, data);
				if (data[0] != 0){
					
					count ++;
					
				}
				
			}
			
			if (count != binaryMat.height()){
				
				if(j == 0){
					
					leftBorder = 0;
					flag = 1;
					
				}
				else {
					
					leftBorder = j - 1;
					flag = 1;
					
				}
				
			}
			
		}
		
		flag = 0;
		// Find the right border
		for (int j = binaryMat.width() - 1; j >= binaryMat.width() * 0.5 && flag == 0; j--){
			
			int count = 0;
			for (int i = 0; i <binaryMat.height(); i++){
				
				binaryMat.get(i, j, data);
				if (data[0] != 0){
					
					count ++;
					
				}
				
			}
			
			if (count != binaryMat.height()){
				
				rightBorder = j + 1;
				flag = 1;
				
			}
			
		}
		
		Rect roi = new Rect(leftBorder, upBorder, rightBorder - leftBorder, downBorder - upBorder);
		
		return roi;
		
	}
	
	/** 
     * Transform Mat to BufferedImage
     * A help function for showImage(BuffredImage bfimage)
     */
    public static BufferedImage transformToImg(Mat mat){
    	
    	int bufferSize = mat.channels()*mat.rows()*mat.cols();
        byte[] b = new byte[bufferSize];
        mat.get(0, 0, b);
        BufferedImage bfimage;
        // Check is the image a colour image or a black and white one
        if (mat.channels() == 3){
        	
        	bfimage = new BufferedImage(mat.cols(), mat.rows(), BufferedImage.TYPE_3BYTE_BGR);
        	
        }
        else {
        	
        	bfimage = new BufferedImage(mat.cols(), mat.rows(), BufferedImage.TYPE_BYTE_GRAY);
        	
        }
        final byte[] pixels = ((DataBufferByte) bfimage.getRaster().getDataBuffer()).getData();
        System.arraycopy(b, 0, pixels, 0, b.length);
        return bfimage;
    	
    }
    
    /**
     * Display the image in a JFrame
     * A help function for testing and check the result
     * of image processing
     */
    public static void showImage(BufferedImage bfimage){
        
        ImageIcon icon = new ImageIcon(bfimage);
        JFrame frame = new JFrame();
        frame.setLayout(new FlowLayout());
        frame.setSize(bfimage.getWidth(null)+50, bfimage.getHeight(null)+50);
        JLabel jlb = new JLabel();
        jlb.setIcon(icon);
        frame.add(jlb);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
    }
	
}
