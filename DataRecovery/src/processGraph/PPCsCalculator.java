/***
 * 
 * @author Yingyi Zhang
 * @username yxz559
 * @studentID 1544458
 * 
 */

package processGraph;

/**
 * 
 * Calculate the Pearson product-moment correlation coefficient
 * 
 */
public class PPCsCalculator {

	public double calculate(double[] coef1, double[] coef2) {
		
		double sum_xi = 0;           //sum(Xi)
		double sum_yi = 0;           //sum(Yi)
		double sum_xiXyi = 0;        //sum(Xi*Yi)
		double sum_xiXsum_yi = 0;    //sum(Xi)*sum(Yi)
		double sum_xiSquare = 0;     //sum(Xi^2)
		double square_sum_xi = 0;    //sum(Xi)^2
		double sum_yiSquare = 0;     //sum(Yi^2)
		double square_sum_yi = 0;    //sum(Yi)^2
		
		int n = coef1.length;
		
		for (int i = 0; i < n; i++){
			
			sum_xiXyi += coef1[i] * coef2[i];
			sum_xi += coef1[i];
			sum_yi += coef2[i];
			sum_xiSquare += Math.pow(coef1[i], 2);
			sum_yiSquare += Math.pow(coef2[i], 2);
			
		}
		
		sum_xiXsum_yi = sum_xi * sum_yi;
		square_sum_xi = Math.pow(sum_xi, 2);
		square_sum_yi = Math.pow(sum_yi, 2);
		
		// n*sum(Xi*Yi) - sum(Xi)*sum(Yi)
		double numerator = (n * sum_xiXyi) - (sum_xiXsum_yi);
		// sqrt(n*sum(Xi^2) - sum(Xi)^2) * sqrt(n*sum(Yi^2) - sum(Yi)^2)
		double denominator = Math.sqrt(n * sum_xiSquare - square_sum_xi) * 
										Math.sqrt(n * sum_yiSquare - square_sum_yi);
		
		double r = numerator/denominator;
		
		System.out.println("r = " + r);
		
		return r;
		
	}

}
