package splines;

import java.util.Arrays;


public class Matrixtest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		float[][] m = { {1.0f, 0.0f, 0.0f,0.0f,0.0f},
						{1.0f, 4.0f, 1.0f, 0.0f, 0.0f},
						{0.0f, 1.0f, 4.0f, 1.0f, 0.0f},
						{0.0f, 0.0f, 1.0f, 4.0f, 1.0f},
						{0.0f, 0.0f, 0.0f, 0.0f, 1.0f}};
		float[][] erg = Matrix.Inverse(m);
		
		System.out.println(Arrays.deepToString(erg));
		float[][] a = {{1,2 ,3},{1,2,3}};
		float[][] b = {{1},{1},{1}};
		/*[[1.0, -0.0, 0.0, -0.0, -0.0], 
		 [-0.26785716, 0.26785716, -0.071428575, 0.017857144, -0.017857144], 
		 [0.071428575, -0.071428575, 0.2857143, -0.071428575, 0.071428575], 
		 [-0.017857144, 0.017857144, -0.071428575, 0.26785716, -0.26785716], 
		 [0.0, -0.0, 0.0, -0.0, 1.0]]
		 
		[[1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0],
		 [0.0, -3.0, 0.0, 3.0, 0.0, 0.0, 0.0],
		 [0.0, 0.0, -3.0, 0.0, 3.0, 0.0, 0.0],
		 [0.0, 0.0, 0.0, -3.0, 0.0, 3.0, 0.0],
		 [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0]]
		 
		[ [101.0],
		  [332.0],
		  [520.0],
		  [540.0],
		  [428.0]]*/
		System.out.println(Arrays.deepToString(Matrix.MultiplyMatrix(a,b)));
//		System.out.println(Arrays.deepToString(SplinePanel.createMatrixA(5)));
//		System.out.println(Arrays.deepToString(SplinePanel.createMatrixB(5)));
	}

}
