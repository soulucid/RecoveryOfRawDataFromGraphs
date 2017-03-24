/***
 * 
 * @author Yingyi Zhang
 * @username yxz559
 * @studentID 1544458
 * 
 */

package processImage;

import java.io.File;
import java.util.ArrayList;

/**
 * 
 * The entry for image division
 * 
 */
public class ImageProcessor {

	protected static ArrayList<String> imageList = new ArrayList<String>();
	
	/**
	 * @return
	 * 
	 * Get all the images in the directory and start to divide them
	 */
	public int start(){
		
		try {
			getAllImages();
		}
		catch (Exception e){
			return 1;
		}
		
		ImageDivisionThreadPool idtp = new ImageDivisionThreadPool();
		
		try {
			idtp.divide(imageList);
		}
		catch (Exception e){
			return 1;
		}
		
		return 0;
		
	}
	
	/**
	 * @throws Exception
	 * 
	 * Get all the images' name in the images/ directory
	 */
	public static void getAllImages() throws Exception{
		
		String imageDirectory = "images/";
		File parentDirectory = new File(imageDirectory);
        File[] fileEntries = parentDirectory.listFiles();
        for (File entry : fileEntries){
        	
        	String imageName = imageDirectory + entry.getName();
        	imageList.add(imageName);
        	
        }
		
	}
	
}
