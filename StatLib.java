package test;


public class StatLib {

	// simple average
	public static float avg(float[] x){
		float sum = 0;
		for(float f : x){
			sum += f;
		}
		return sum / x.length;
	}

	// returns the variance of X and Y
	public static float var(float[] x){
		float u = avg(x);
		float sigma = 0;
		for(float f : x)
			sigma += f*f;// f^2
		sigma /= x.length; // *(1/N)
		// is x.len - 1 ? https://stackoverflow.com/questions/7988486/how-do-you-calculate-the-variance-median-and-standard-deviation-in-c-or-java
		return sigma - (u*u);
	}

	// returns the covariance of X and Y
	public static float cov(float[] x, float[] y){
		int len = x.length;
		//if(x.length != y.length) ?!?!?!?!?!
		float[] covarr = new float[len];
		float sum = 0;
		for (int i = 0; i < len; i++){
			covarr[i] = (x[i] - avg(x))*(y[i] - avg(y));
		}
		return avg(covarr);
	}


	// returns the Pearson correlation coefficient of X and Y
	public static float pearson(float[] x, float[] y){
		return 0;
	}

	// performs a linear regression and returns the line equation
	public static Line linear_reg(Point[] points){
		return null;
	}

	// returns the deviation between point p and the line equation of the points
	public static float dev(Point p,Point[] points){
		return 0;
	}

	// returns the deviation between point p and the line
	public static float dev(Point p,Line l){
		return 0;
	}
	
}
