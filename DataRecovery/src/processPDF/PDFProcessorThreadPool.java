/***
 * 
 * @author Yingyi Zhang
 * @username yxz559
 * @studentID 1544458
 * 
 */

package processPDF;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 
 * The thread pool for processing PDFs,
 * so that several PDFs could be processed
 * simultaneously
 * 
 */
public class PDFProcessorThreadPool
{

    @SuppressWarnings("rawtypes")
    public static class ExtractorCallable implements Callable {
        
        private String filepath;
        
        public ExtractorCallable(String filepath) {
            
            this.filepath = filepath;
            
        }
        
        @Override
		public Integer call() {
            
            int success = 0;
            
//          System.out.println("File " + filepath + "is being processed...\n");
//          System.out.println("The working thread is THREAD " + Thread.currentThread().getName() + ".\n");
            
            PDFImageExtractor imgExt = new PDFImageExtractor();
            
            try {
                success = imgExt.extract(filepath);
            }
            catch (IOException e) {
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
    
    @SuppressWarnings("unchecked")
    public void process(ArrayList<String> pdfFileList) throws Exception{
        
//      System.out.println("There are " + pdfFileList.size() + " PDF files.\n");
        
    	// Set the number of working threads
        ExecutorService threadPool = Executors.newFixedThreadPool(pdfFileList.size()/2 + 1);
        
        // process the PDFs
        for(String job : pdfFileList){
            
            Callable<Integer> callable = new ExtractorCallable(job);
            Future<Integer> future = threadPool.submit(callable);
            if (future.get() == 1){
            	System.err.println("The images in PDF " + job + " are not extracted correctly.\n");
            }
            
        }
        
//      System.out.println("All PDFs are processed.\n");
        threadPool.shutdown();
        
    }
    
}
