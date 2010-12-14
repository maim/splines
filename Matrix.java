package splines;

public class Matrix {

	private static int iDF = 0;
	
	public static float[][] Inverse(float[][] a) {
		// Formula used to Calculate Inverse:
		// inv(A) = 1/det(A) * adj(A)
		int tms = a.length;

		float m[][] = new float[tms][tms];
		float mm[][] = Adjoint(a);

		float det = Determinant(a);
		float dd = 0;

		if (det == 0) {
			System.err.println("Inverse nicht bildbar");

		} else {
			dd = 1 / det;
		}

		for (int i = 0; i < tms; i++)
			for (int j = 0; j < tms; j++) {
				m[i][j] = dd * mm[i][j];
			}

		return m;
	}
	
	public static float Determinant(float[][] matrix) {
		int tms = matrix.length;

		float det = 1;

		matrix = UpperTriangle(matrix);

		for (int i = 0; i < tms; i++) {
			det = det * matrix[i][i];
		} // multiply down diagonal

		det = det * iDF; // adjust w/ determinant factor

		return det;
	}
	
	public static float[][] Adjoint(float[][] a) {
		int tms = a.length;

		float m[][] = new float[tms][tms];

		int ii, jj, ia, ja;
		float det;

		for (int i = 0; i < tms; i++)
			for (int j = 0; j < tms; j++) {
				ia = ja = 0;

				float ap[][] = new float[tms - 1][tms - 1];

				for (ii = 0; ii < tms; ii++) {
					for (jj = 0; jj < tms; jj++) {

						if ((ii != i) && (jj != j)) {
							ap[ia][ja] = a[ii][jj];
							ja++;
						}

					}
					if ((ii != i) && (jj != j)) {
						ia++;
					}
					ja = 0;
				}

				det = Determinant(ap);
				m[i][j] = (float) Math.pow(-1, i + j) * det;
			}

		m = Transpose(m);

		return m;
	}
	
	public static float[][] Transpose(float[][] a) {
		
		float m[][] = new float[a[0].length][a.length];

		for (int i = 0; i < a.length; i++)
			for (int j = 0; j < a[i].length; j++)
				m[j][i] = a[i][j];
		return m;
	}
	
	public static float[][] UpperTriangle(float[][] m) {

		float f1 = 0;
		float temp = 0;
		int tms = m.length; // get This Matrix Size (could be smaller than
							// global)
		int v = 1;

		iDF = 1;

		for (int col = 0; col < tms - 1; col++) {
			for (int row = col + 1; row < tms; row++) {
				v = 1;

				outahere: while (m[col][col] == 0) // check if 0 in diagonal
				{ // if so switch until not
					if (col + v >= tms) // check if switched all rows
					{
						iDF = 0;
						break outahere;
					} else {
						for (int c = 0; c < tms; c++) {
							temp = m[col][c];
							m[col][c] = m[col + v][c]; // switch rows
							m[col + v][c] = temp;
						}
						v++; // count row switchs
						iDF = iDF * -1; // each switch changes determinant
										// factor
					}
				}

				if (m[col][col] != 0) {

					try {
						f1 = (-1) * m[row][col] / m[col][col];
						for (int i = col; i < tms; i++) {
							m[row][i] = f1 * m[col][i] + m[row][i];
						}
					} catch (Exception e) {
						System.out.println("Still Here!!!");
					}

				}

			}
		}

		return m;
	}
	
	public static float[][] MultiplyMatrix(float[][] a, float[][] b) {
		
		if(a[0].length != b.length)
			throw new RuntimeException("Matrices incompatible for multiplication");
		float matrix[][] = new float[a.length][b[0].length];

		for (int i = 0; i < a.length; i++)
			for (int j = 0; j < b[i].length; j++) {
				System.out.println("i:" + i + "\nj:" + j + "\n");
				matrix[i][j] = 0;
			}
		//cycle through answer matrix
		for(int i = 0; i < matrix.length; i++){
			for(int j = 0; j < matrix[i].length; j++){
				matrix[i][j] = calculateRowColumnProduct(a,i,b,j);
			}
		}
		return matrix;
	}
	
	public static float calculateRowColumnProduct(float[][] A, int row, float[][] B, int col){
		float product = 0;
		for(int i = 0; i < A[row].length; i++)
			product +=A[row][i]*B[i][col];
		return product;
	}
}

