/***
 * 
 * @author Yingyi Zhang
 * @username yxz559
 * @studentID 1544458
 * 
 */

package processPDF;

import java.io.File;
import java.util.ArrayList;

/**
 * 
 * The entry of PDF process
 * 
 */
public class PDFProcessor
{

    protected static ArrayList<String> pdfFileList = new ArrayList<String>();
    private String sourcePath;
    
    /**
     * Constructor function
     * @param sourcePath
     */
    public PDFProcessor(String sourcePath) {
    	
        this.sourcePath = sourcePath;

    }
    
    /**
     * Get all the PDF files in a directory
     * and start to process them
     */
    public int start() {
    	
    	int flag = 0;
    	
    	// If there is any exception, end the program
    	try {
    		getAllPDFs(sourcePath);
    	}
    	catch (Exception e){
    		flag = 2;
    		return flag;
    	}
    	
    	// If the directory doesn't contain any PDF file, end the program
    	if (pdfFileList.isEmpty()){
    		flag = 1;
    		return flag;
    	}
    	else {
    		
    		// If there is any exception, end the program
    		PDFProcessorThreadPool pdftp = new PDFProcessorThreadPool();
    		try{
    			pdftp.process(pdfFileList);
    		}
    		catch (Exception e){
    			flag = 2;
    			return flag;
    		}
        
    	}
    	
    	return flag;
    	
    }
    
    /**
     * Navigate through the directory and get all the PDF
     * files under that directory
     */
    public static void getAllPDFs(String filepath) throws Exception{
        
        File parentDirectory = new File(filepath);
        File[] fileEntries = parentDirectory.listFiles();
        for (File entry : fileEntries){
            
            if (entry.isDirectory()){
                
                getAllPDFs(entry.getAbsolutePath());
                
            }
            else {
                
                String filename = entry.getName();
                if (filename.endsWith(".pdf")){
                    
                    pdfFileList.add(entry.getAbsolutePath());
//                  System.out.println("FILENAME: " + entry.getName() + "\n");
                    
                }
                
            }
            
        }
        
    }

}
