/***
 * 
 * @author Yingyi Zhang
 * @username yxz559
 * @studentID 1544458
 * 
 */

package junitTest;

import static org.junit.Assert.*;

import org.junit.Test;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import processGraph.AxisDetector;

public class AxisDetectorTest {

	/**
	 * Test method for {@link processGraph.AxisDetector#axisDetect(org.opencv.core.Mat, char, org.opencv.core.Mat)}.
	 */
	@Test
	public void testAxisDetect() {
		String imagePath1 = "testData/BNOK7.png";
		String imagePath2 = "testData/IC378572.png";
		String imagePath3 = "testData/testcase1-IMG-6-0.jpg";
		String imagePath4 = "testData/testcase2-IMG-7-0.jpg";
		String imagePath5 = "testData/testcase2-IMG-8-0.jpg";
		String imagePath6 = "testData/Windows_ControlPanel_WebService_Setup_Running.PNG";
		
		Mat mat1 = Imgcodecs.imread(imagePath1);
		Mat mat2 = Imgcodecs.imread(imagePath2);
		Mat mat3 = Imgcodecs.imread(imagePath3);
		Mat mat4 = Imgcodecs.imread(imagePath4);
		Mat mat5 = Imgcodecs.imread(imagePath5);
		Mat mat6 = Imgcodecs.imread(imagePath6);
		
		assertTrue(AxisDetector.axisDetect(mat1, 'x') == 0);
		assertTrue(AxisDetector.axisDetect(mat1, 'y') == 0);
		
		assertTrue(AxisDetector.axisDetect(mat2, 'x') == 0);
		assertTrue(AxisDetector.axisDetect(mat2, 'y') == 0);
		
		assertTrue(AxisDetector.axisDetect(mat3, 'x') != 0);
		assertTrue(AxisDetector.axisDetect(mat3, 'y') != 0);
		
		assertTrue(AxisDetector.axisDetect(mat4, 'x') != 0);
		assertTrue(AxisDetector.axisDetect(mat4, 'y') != 0);
		
		assertTrue(AxisDetector.axisDetect(mat5, 'x') != 0);
		assertTrue(AxisDetector.axisDetect(mat5, 'y') != 0);
		
		assertTrue(AxisDetector.axisDetect(mat6, 'x') == 0);
		assertTrue(AxisDetector.axisDetect(mat6, 'y') == 0);
		
	}

}
