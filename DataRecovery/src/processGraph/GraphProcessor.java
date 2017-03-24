/***
 * 
 * @author Yingyi Zhang
 * @username yxz559
 * @studentID 1544458
 * 
 */

package processGraph;

import java.io.File;
import java.util.ArrayList;

import org.opencv.core.Core;

/**
 * 
 * The entry for graph process and data recovery
 * 
 */
public class GraphProcessor {
	
	protected static ArrayList<String> imageList = new ArrayList<String>(); 

	public int start() {
		
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		getAllImages();
		for (String image : imageList){
			
			GraphClassifier gc = new GraphClassifier(image);
			try {
				gc.calssify();
			}
			catch (Exception e){
				return 1;
			}
			
		}
		
		return 0;
			
	}
	
	/**
	 * Get all the images' name in the divImages/ directory
	 */
	public static void getAllImages(){
		
		String imageDirectory = "divImages/";
		File parentDirectory = new File(imageDirectory);
        File[] fileEntries = parentDirectory.listFiles();
        for (File entry : fileEntries){
        	
        	String imageName = imageDirectory + entry.getName();
        	imageList.add(imageName);
        	
        }
		
	}

}
