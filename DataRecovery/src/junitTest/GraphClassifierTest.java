/***
 * 
 * @author Yingyi Zhang
 * @username yxz559
 * @studentID 1544458
 * 
 */

package junitTest;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import processGraph.GraphClassifier;

public class GraphClassifierTest {

	/**
	 * Test method for {@link processGraph.GraphClassifier#calssify()}.
	 */
	@Test
	public void testCalssify() {
		String imagePath1 = "testData/1.png";
		String imagePath2 = "testData/2.jpg";
		String imagePath3 = "testData/3.jpg";
		String imagePath4 = "testData/4.jpg";
		String imagePath5 = "testData/5.jpg";
		String imagePath6 = "testData/6.png";
		
		GraphClassifier gc1 = new GraphClassifier(imagePath1);
		GraphClassifier gc2 = new GraphClassifier(imagePath2);
		GraphClassifier gc3 = new GraphClassifier(imagePath3);
		GraphClassifier gc4 = new GraphClassifier(imagePath4);
		GraphClassifier gc5 = new GraphClassifier(imagePath5);
		GraphClassifier gc6 = new GraphClassifier(imagePath6);
		
		assertTrue(gc1.calssify() == 0);
		assertTrue(gc2.calssify() == 0);
		assertTrue(gc3.calssify() == 0);
		assertTrue(gc4.calssify() == 0);
		assertTrue(gc5.calssify() == 0);
		assertTrue(gc6.calssify() == 0);
	}

}
