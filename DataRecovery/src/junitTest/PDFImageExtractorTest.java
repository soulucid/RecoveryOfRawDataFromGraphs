/***
 * 
 * @author Yingyi Zhang
 * @username yxz559
 * @studentID 1544458
 * 
 */

package junitTest;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import processPDF.PDFImageExtractor;

public class PDFImageExtractorTest {

	@Test
	public void testExtract() {
		PDFImageExtractor imgext = new PDFImageExtractor();
		try {
			assertTrue(imgext.extract("testData/testcase1.pdf") == 0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			assertTrue(imgext.extract("testData/testcase2.pdf") == 0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			assertTrue(imgext.extract("testData/testcase3.pdf") == 0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
