/***
 * 
 * @author Yingyi Zhang
 * @username yxz559
 * @studentID 1544458
 * 
 */

package processImage;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 
 * Thread pool for image division
 * 
 */
public class ImageDivisionThreadPool {
	
	public static class DivisionCallable implements Callable {

		private String imagePath;
		
		public DivisionCallable(String imagePath){
			
			this.imagePath = imagePath;
			
		}
		
		@Override
		public Integer call() throws Exception {
			// TODO Auto-generated method stub
			
			int success = 0;
			
			ImageDivision imgDiv = new ImageDivision(imagePath);
			
			try {
				success = imgDiv.start();
			}
			catch (Exception e){
				e.printStackTrace();
			}
			
			if (success == 1){
				return 1;
			}
			else {
				return 0;
			}
		}
		
	}
	
	/**
	 * @param imageList
	 * @throws Exception
	 * 
	 * Call the callable
	 */
	public void divide(ArrayList<String> imageList) throws Exception{
		
		ExecutorService threadPool = Executors.newFixedThreadPool(imageList.size()/2 + 1);
		
		for (String image: imageList){
			
			@SuppressWarnings("unchecked")
			Callable<Integer> callable = new DivisionCallable(image);
			Future<Integer> future = threadPool.submit(callable);
			if (future.get() == 1){
				
				System.err.println("The division of this image failed.");
				
			}
			
		}
		
		threadPool.shutdown();
		
	}

}
