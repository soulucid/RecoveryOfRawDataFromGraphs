/***
 * 
 * @author Yingyi Zhang
 * @username yxz559
 * @studentID 1544458
 * 
 */

package processGraph;

import java.util.ArrayList;

import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;

import model.Graph;
import model.GraphPoint;

/**
 * 
 * Detect graph data plagiarism
 * 
 */
public class PlagiarismDetect {

	public void detect(ArrayList<Graph> graphList, int type) {
		
		switch(type){
		
			case 1:
				brokenLineChart(graphList);
				break;
			case 2:
				curveChart(graphList);
				break;
				
		}
		
	}
	
	/**
	 * @param graphList
	 * 
	 * Detect data plagiarism in broken line graphs
	 */
	public void brokenLineChart(ArrayList<Graph> graphList){
		
		ArrayList<ArrayList<Double>> allX = new ArrayList<ArrayList<Double>>();
		ArrayList<double[]> allY = new ArrayList<double[]>();
		
		// Get all the graphs' Xs and Ys
		for (int i = 0; i < graphList.size(); i++){
			
			ArrayList<GraphPoint> gp = graphList.get(i).getData();
			ArrayList<Double> x = new ArrayList<Double>();
			double[] y = new double[graphList.size()];
			
			for (int j = 0; j < gp.size(); j++){
				
				x.add(gp.get(j).getX());
				y[j] = gp.get(j).getY();
				
			}
			
			allX.add(x);
			allY.add(y);
			
		}
		
		// Count how many graphs may have the suspicion of data plagiarism
		int count = 0;
		
		for (int i = 0; i < allX.size(); i++){
			
			for (int j = i + 1; j < allX.size() - 1; j++){
				
				// If the number of X axis scale numbers are the same, compute the PPCs
				if (allX.get(i).size() == allX.get(j).size()){
					
					PPCsCalculator calc = new PPCsCalculator();
					double r = calc.calculate(allY.get(i), allY.get(j));
					if (r > 0.99){
						
						count ++;
						
					}
					
				}
				else {
					
					int loop = Math.min(allX.get(i).size(), allX.get(j).size());
					int similar = 0;
					
					for (int n = 0; n < loop; n++){
						
						if (allX.get(i).contains(allX.get(j).get(n))){
							
							if (Math.abs(allY.get(i)[n] - allY.get(j)[n]) < 0.5){
								
								similar ++;
								
							}
							
						}
						
					}
					
					if (similar > 0.75 * loop){
						
						count ++;
						
					}
					
				}
				
			}
			
		}
		
		System.out.println(count);
		
	}
	
	/**
	 * @param graphList
	 * 
	 * Detect data plagiarism in curve graphs
	 */
	public void curveChart(ArrayList<Graph> graphList){
		
		// An ArrayList for all polynomials' coefficients
		ArrayList<double[]> coEffs = new ArrayList<double[]>();
		
		//  For every graph, find a 5-degree polynomial to fit it
		for (Graph g : graphList){
			
			WeightedObservedPoints wobs = new WeightedObservedPoints();
			ArrayList<GraphPoint> GPs = g.getData();
			for (GraphPoint gp : GPs){
				
				wobs.add(gp.getX(), gp.getY());
				
			}
			
			PolynomialCurveFitter polyFitter = PolynomialCurveFitter.create(5);
			
			double[] coeff = polyFitter.fit(wobs.toList()); 
			coEffs.add(coeff);
			for (double c : coeff){
				
				System.out.println(c);
				
			}
			
		}
		
		ArrayList<double[]> Ys = new ArrayList<double[]>();
		
		// Compute the y value when x=1, 2, 3, 4, 5 respectively
		for (int i = 0; i < coEffs.size(); i++){
			
			double y1 = Math.pow(1.0, 5)*coEffs.get(i)[0] 
					 + Math.pow(1.0, 4)*coEffs.get(i)[1]
					 + Math.pow(1.0, 3)*coEffs.get(i)[2]
					 + Math.pow(1.0, 2)*coEffs.get(i)[3]
					 + 1.0*coEffs.get(i)[4];
			
			double y2 = Math.pow(2.0, 5)*coEffs.get(i)[0] 
					 + Math.pow(2.0, 4)*coEffs.get(i)[1]
					 + Math.pow(2.0, 3)*coEffs.get(i)[2]
					 + Math.pow(2.0, 2)*coEffs.get(i)[3]
					 + 2.0*coEffs.get(i)[4];
			
			double y3 = Math.pow(3.0, 5)*coEffs.get(i)[0] 
					 + Math.pow(3.0, 4)*coEffs.get(i)[1]
					 + Math.pow(3.0, 3)*coEffs.get(i)[2]
					 + Math.pow(3.0, 2)*coEffs.get(i)[3]
					 + 3.0*coEffs.get(i)[4];
			
			double y4 = Math.pow(4.0, 5)*coEffs.get(i)[0] 
					 + Math.pow(4.0, 4)*coEffs.get(i)[1]
					 + Math.pow(4.0, 3)*coEffs.get(i)[2]
					 + Math.pow(4.0, 2)*coEffs.get(i)[3]
					 + 4.0*coEffs.get(i)[4];
			
			double y5 = Math.pow(5.0, 5)*coEffs.get(i)[0] 
					 + Math.pow(5.0, 4)*coEffs.get(i)[1]
					 + Math.pow(5.0, 3)*coEffs.get(i)[2]
					 + Math.pow(5.0, 2)*coEffs.get(i)[3]
					 + 5.0*coEffs.get(i)[4];
			
			double[] ys = {y1, y2, y3, y4, y5};
			Ys.add(ys);
			
		}
		
		// Count how many graphs may have the suspicion of data plagiarism
		int count = 0;
		
		// Calculate the Pearson coefficient of Ys
		for (int i = 0; i < Ys.size(); i++){
			
			PPCsCalculator calc = new PPCsCalculator();
			for (int j = i + 1; j < Ys.size() - 1; j++){
				double r = calc.calculate(Ys.get(i), Ys.get(j));
				if (r > 0.99){
					
					count ++;
					
				}
			}
			
		}
		
		System.out.println(count);
		
	}

}
