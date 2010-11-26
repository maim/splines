package splines;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class Main extends JFrame {
	public Main() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		add(new SplinePanel());
		setSize(800, 600);
		setVisible(true);
		setLocationRelativeTo(null);
	}
	
	public static void main(String[] args) {
		new Main();
	}
}
