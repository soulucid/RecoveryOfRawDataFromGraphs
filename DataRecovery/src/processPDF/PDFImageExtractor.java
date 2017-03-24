/***
 * 
 * @author Yingyi Zhang
 * @username yxz559
 * @studentID 1544458
 * 
 */

package processPDF;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceGray;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;
import org.apache.pdfbox.pdmodel.graphics.image.PDImage;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.contentstream.PDFGraphicsStreamEngine;
import org.apache.pdfbox.pdmodel.graphics.form.PDTransparencyGroup;
import org.apache.pdfbox.pdmodel.graphics.state.PDSoftMask;

/**
 * 
 * This class is used to load a PDF file
 * and extract images from it
 * 
 */
public class PDFImageExtractor {
    
    private int counter = 1;
    private String pdfname;

    // Get the JPEG COSName
    private static final List<String> JPEG = Arrays.asList(
            COSName.DCT_DECODE.getName(),
            COSName.DCT_DECODE_ABBREVIATION.getName());
    
    /**
     * @param sourcePath
     * @return
     * @throws IOException
     * 
     * Initialise some variables and load the PDF document
     */
    public int extract(String sourcePath) throws IOException {
        
        File originPDF = new File(sourcePath);
        pdfname = originPDF.getName().substring(0, originPDF.getName().length() - 4);
        
        if (originPDF.exists()) {
            
            PDDocument document = null;
            
            try {
                
                // Load the PDF file and process pages one by one
                document = PDDocument.load(originPDF);
                for (int i = 0; i < document.getNumberOfPages(); i++) {
                    
                    PDPage page = document.getPage(i);
                    ImageGraphicsEngine extractor = new ImageGraphicsEngine(page);
                    extractor.processPage();
                    
                }
                
            }
            
            finally {
                
                if(document != null) {
                    
                    document.close();
                    
                }
            }
            return 0;
        }
    
        else {
        
//          System.err.println("The PDF file does not exist.");
            return 1;
        
        }
    }
    
    /**
     * Referenced org.apache.pdfbox.tools.ExtractImages
     * @author Ben Litchfield
     */
    private class ImageGraphicsEngine extends PDFGraphicsStreamEngine {
        
        protected ImageGraphicsEngine(PDPage page) throws IOException {
            super(page);
        }

        public void processPage() throws IOException {
            
        	// Get all the resources(objects) in a page
            PDPage page = getPage();
            processPage(page);
            PDResources res = page.getResources();
            for (COSName name : res.getExtGStateNames()) {
                
                PDSoftMask softMask = res.getExtGState(name).getSoftMask();
                
                if (softMask != null) {
                    
                    PDTransparencyGroup group = softMask.getGroup();
                    
                    if (group != null) {
                        processSoftMask(group);
                    }
                }
            }
        }

        @Override
        public void drawImage(PDImage pdImage) throws IOException {
            
            if (pdImage instanceof PDImageXObject) {
                
                // Save the image 
                String name = pdfname + "-IMG-" + counter;
                counter++;

//              System.out.println("Saving image: " + name);
                saveImage(pdImage, name);
            }
            
        }
        
        private boolean hasMasks(PDImage pdImage) throws IOException {
            
            if (pdImage instanceof PDImageXObject) {
                
                PDImageXObject xOImg = (PDImageXObject) pdImage;
                return xOImg.getMask() != null || xOImg.getSoftMask() != null;
                
            }
            return false;
            
        }
        
        private void saveImage(PDImage pdImage, String filename) throws IOException {
            
            String suffix = pdImage.getSuffix();
            
            // If the image doesn't have a suffix, set it to png
            if (suffix == null) {
                suffix = "png";
            }

            FileOutputStream out = null;
            
            try {
            	
                // Set the output position of the images
                out = new FileOutputStream("images/" + filename + "." + suffix);
                BufferedImage image = pdImage.getImage();
                if (image != null) {
                    
                    if ("jpg".equals(suffix)) {
                        
                        String colorSpaceName = pdImage.getColorSpace().getName();
                        
                        if (!hasMasks(pdImage) && 
                                (PDDeviceGray.INSTANCE.getName().equals(colorSpaceName) ||
                                 PDDeviceRGB.INSTANCE.getName().equals(colorSpaceName))) {
                            
                            // RGB or Gray colorspace: get and write the unmodified JPEG stream
                            InputStream data = pdImage.createInputStream(JPEG);
                            IOUtils.copy(data, out);
                            IOUtils.closeQuietly(data);
                            
                        }
                        else {
                            
                            // for CMYK and other "unusual" colorspaces, the JPEG will be converted
                            ImageIOUtil.writeImage(image, suffix, out);
                            
                        }
                    }
                    else 
                    {
                        ImageIOUtil.writeImage(image, suffix, out);
                    }
                }
                out.flush();
            }
            finally
            {
                if (out != null)
                {
                    out.close();
                }
            }
        }

        @Override
        public void appendRectangle(Point2D arg0, Point2D arg1, Point2D arg2, Point2D arg3)
                throws IOException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void clip(int arg0) throws IOException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void closePath() throws IOException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void curveTo(float arg0, float arg1, float arg2, float arg3, float arg4, float arg5)
                throws IOException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void endPath() throws IOException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void fillAndStrokePath(int arg0) throws IOException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void fillPath(int arg0) throws IOException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public Point2D getCurrentPoint() throws IOException
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void lineTo(float arg0, float arg1) throws IOException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void moveTo(float arg0, float arg1) throws IOException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void shadingFill(COSName arg0) throws IOException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void strokePath() throws IOException
        {
            // TODO Auto-generated method stub
            
        }
    }
}
