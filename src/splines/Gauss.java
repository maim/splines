package splines;
/**
 * Methoden zum Loesen von linearen Gls
 * nach Gauss
 * 
 * @author Rudolph copyright (c) 2008
 * 
 */
public class Gauss {

	/**
	 * Führt die Rueckwaertssubstitution des Gausverfahrens durch.
	 * Es ist eine rechte Seite erlaubt. 
	 * 
	 * @param m n * (n+1) Matrix obere Dreiecksmatrix
	 * @return Loesungsvektor x
	 */
	public static double[] rueckwaertsSubstitution(double[][] m) {
		int n = m.length;
		double[] x = new double[n];
		x[n - 1] = m[n - 1][n] / m[n - 1][n - 1];
		for (int i = n - 2; i > -1; i--) {
			double tmp = m[i][n];
			for (int j = i + 1; j < n; j++) {
				tmp -= m[i][j] * x[j];
			}
			x[i] = tmp / m[i][i];
		}
		return x;
	}

	/**
	 * Führt die Rueckwaertssubstitution des Gaussverfahrens durch.
	 * Es sind k rechte Seiten erlaubt.
	 * 
	 * @param m n * (n+k) Matrix obere Dreiecksmatrix
	 * @return Loesungsvektor x
	 */
	public static double[][] rueckwaertsSubstitutionMehrereGLS(double[][] m) {
		int n = m.length;
		int nGls = m[0].length - n;
		double[][] x = new double[n][nGls];
		for (int k = 0; k < nGls; k++) {
			x[n - 1][k] = m[n - 1][n + k] / m[n - 1][n - 1];
			for (int i = n - 2; i > -1; i--) {
				double tmp = m[i][n + k];
				for (int j = i + 1; j < n; j++) {
					tmp -= m[i][j] * x[j][k];
				}
				x[i][k] = tmp / m[i][i];
			}
		}
		return x;
	}

	/**
	 * Führt eine Dreieckszerlegung nach Gauss durch.
	 * 
	 * @param m n*(n+k) Matrix Koeffizienten und rechte Seite(n). 
	 * @return Matrix, die Elemente der unteren Dreiecksmatrix haben den Wert 0
	 */
	public static double[][] dreiecksElimination(double[][] m) {
		double[][] r = cloneMatrix(m);
		int n = r.length;
		// --- fuer alle zu eliminierenden Spalten
		for (int j = 0; j < n - 1; j++) {
			double max = Math.abs(r[j][j]);
			int pivotZeile = j;
			// ---- Pivotsuche
			for (int i = j + 1; i < n; i++) {
				if (Math.abs(r[i][j]) > max) {
					max = Math.abs(r[i][j]);
					pivotZeile = i;
				}
			}
			// ---- Zeilentausch
			if (pivotZeile != j) {
				for (int k = j; k < r[j].length; k++) {
					double tmp = r[j][k];
					r[j][k] = r[pivotZeile][k];
					r[pivotZeile][k] = tmp;
				}
			}
			// ---- Elimination
			for (int i = j + 1; i < n; i++) {
				double faktor = r[i][j] / r[j][j];
				for (int k = j; k < r[j].length; k++) {
					r[i][k] -= faktor * r[j][k];
				}
			}
		}
		return r;
	}

	/**
	 * Berechnet die Determinante einer quadratischen Matrix.
	 * Es wird zunaechst eine Dreieckselimination durchgefuehrt. 
	 * Die Determinante berechnet sich als Produkt der 
	 * Hauptdiagonalelemente.
	 * 
	 * @param m n*n Matrix
	 * @return Wert der Determinante
	 */
	public static double determinante(double[][] m) {
		double[][] tmp = dreiecksElimination(m);
		// --- Produkt der Hauptdiagonalelemente
		double r = tmp[0][0];
		for (int i = 1; i < tmp.length; i++) {
			r *= tmp[i][i];
		}
		return r;
	}

	/**
	 * Berechnet das Hadamardsche Konditionsmass Kh < 0.01 schlechte Kondition
	 * Kh > 0.1 gute Kondition
	 * 
	 * @param m quadratische Matrix
	 * @return
	 */
	public static double hadamardKondition(double[][] m) {
		double r = Math.abs(determinante(m));
		int n = m.length;
		for (int i = 0; i < n; i++) {
			double alpha = 0d;
			for (int j = 0; j < n; j++) {
				alpha += m[i][j] * m[i][j];
			}
			r /= Math.sqrt(alpha);
		}
		return r;
	}

	/**
	 * Drucke Matrix
	 * 
	 * @param c Name der Matrix
	 * @param m Vektor
	 */
	public static void print(String c, double[][] m) {
		for (int i = 0; i < m.length; i++) {
			System.out.print(c + "[ " + i + "][*]: ");
			for (int j = 0; j < m[i].length; j++) {
				System.out.print(m[i][j] + ", ");
			}
			System.out.println(" ");
		}
	}

	/**
	 * Drucke Vektor
	 * 
	 * @param c Name des Vektors
	 * @param m Vektor
	 */
	public static void print(String c, double[] m) {
		for (int i = 0; i < m.length; i++) {
			System.out.println(c + "[ " + i + "]: " + m[i] + ", ");
		}
	}

	/**
	 * Legt die Kopie einer Matrix an
	 * 
	 * @param matrix
	 * @return
	 */
	public static double[][] cloneMatrix(double[][] matrix) {
		int m = matrix.length;
		int n = matrix[0].length;
		double[][] clone = new double[m][n];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				clone[i][j] = matrix[i][j];
			}
		}
		return clone;
	}

	/**
	 * Invertiere die quadratische Matrix m
	 * 
	 * @param m zu invertierende Matrix
	 * @return inverse Matrix
	 */
	public static double[][] invertiereMatrix(double[][] m) {
		int n = m.length;
		double[][] tmp = new double[n][2 * n];
		//print("Parameter", m);
		// --- Kopie der Matrix mit Einheitsmatrix rechts
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				tmp[i][j] = m[i][j];
			}
			tmp[i][i + n] = 1d;
		}
		// --- Dreieckselimination
		double[][] tmp1 = dreiecksElimination(tmp);
		// --- Rueckwaertssubstitution
		//print("ausgang", tmp);
		//print("dreieck", tmp1);
		double[][] r = rueckwaertsSubstitutionMehrereGLS(tmp1);
		//print("inverse", r);

		return r;
	}
	
	/**
	 * Matrizen multiplizieren
	 * @param a
	 * @param b
	 * @return
	 */
	public static double[][] multipliziereMatizen(double[][] a, double[][] b) {
		
		if(a[0].length != b.length)
			throw new RuntimeException("Matrices incompatible for multiplication");
		
		double matrix[][] = new double[a.length][b[0].length];

		//cycle through answer matrix
		for(int i = 0; i < matrix.length; i++){
			for(int j = 0; j < matrix[i].length; j++){
				matrix[i][j] = 0;
				for(int k =0; k<a[0].length; k++)
				{
					matrix[i][j] += a[i][k]*b[k][j]; 
				}
			}
		}
		return matrix;
	}

	/**
	 * Beispielprogramm
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		double[][] m1 = { { 1d, 2d, 3d }, { 2d, 3d, 5d } };
		print("m1", m1);
		double[][] m2 = dreiecksElimination(m1);
		print("m2", m2);
		double[] x2 = rueckwaertsSubstitution(m2);
		print("x2", x2);
		double[][] m3 = { { 1d, 2d, 1d, 2d }, { 2d, 3d, 1d, 4d }, { 1d, 1d, 1d, 1d } };
		print("m3", m3);
		double[][] m4 = dreiecksElimination(m3);
		print("m4", m4);
		double[] x4 = rueckwaertsSubstitution(m4);
		print("x4", x4);
		double[][] m5 = { { 1d, 2d, 1d, 1d, 0d, 0d }, { 2d, 3d, 1d, 0d, 1d, 0d }, { 1d, 1d, 1d, 0d, 0d, 1d } };
		double[][] m6 = dreiecksElimination(m5);
		print("m5", m5);
		print("m6", m6);
		double[][] x5 = rueckwaertsSubstitutionMehrereGLS(m6);
		print("x5", x5);

		double[][] m5a = { { 1d, 3d, 1d },{ 2d, 6d, 1d }, { 0d, 2d, 3d }  };
		double[][] m7 = invertiereMatrix(m5a);
		print("m5a", m5a);
		print("m7", m7);

	}

}
