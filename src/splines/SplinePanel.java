package splines;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JPanel;
import javax.vecmath.Point2f;

public class SplinePanel extends JPanel implements MouseListener, MouseMotionListener {

	private ArrayList<Point> points;
	private ArrayList<Point2f> tangents;
	private int selectedPoint;
	private static final int CLICK_ABSTAND = 5;
	
	public SplinePanel() {
		points = new ArrayList<Point>();
		tangents = new ArrayList<Point2f>();
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawPoints(points, g);
		drawLines(points, g);
		if(points.size()>4)
			drawSpline(points, g);
	}
	
	private void drawPoints(ArrayList<Point> liste, Graphics g) {
		g.setColor(Color.RED);
		for (int i = 0; i < liste.size(); i++) {
			if(i == selectedPoint) {
				drawSelectedPoint(liste.get(i), g);
			} else {
				g.setColor(Color.RED);
				Point p1 = points.get(i);
				g.drawLine(p1.x-2, p1.y-2, p1.x+2, p1.y+2);
				g.drawLine(p1.x-2, p1.y+2, p1.x+2, p1.y-2);
			}
			
		}
	}
	
	private void drawPoints2f(ArrayList<Point2f> liste, Graphics g) {
		g.setColor(Color.RED);
		for (int i = 0; i < liste.size(); i++) {
			g.setColor(Color.RED);
			Point p1 = points.get(i);
			g.drawLine(p1.x-2, p1.y-2, p1.x+2, p1.y+2);
			g.drawLine(p1.x-2, p1.y+2, p1.x+2, p1.y-2);
		}
	}
	
	private void drawSpline(ArrayList<Point> liste, Graphics g) {
		if (liste.size() >= 2) {
			for(int i = 0; i < liste.size(); i+=2) {
				Point p1 = liste.get(i);
				Point p2 = liste.get(i+1);
				Point2f t1 = tangents.get(i);
				Point2f t2 = tangents.get(i+1);
				drawPoints2f(interpolateLine(p1, p2, t1, t2, 10), g);
			}
		}
	}
	
	private ArrayList<Point2f> interpolateLine(Point p1, Point p2, Point2f t1, Point2f t2, int num_steps) {
		ArrayList<Point2f> erg = new ArrayList<Point2f>();
		float stepsize = 1.0f/num_steps;
		for(float u = 0.0f; u <= 1.0f;u += stepsize){
			erg.add(c(u, p1, p2, t1, t2));
		}
		return erg;
	}
	
	private void drawSelectedPoint(Point p, Graphics g) {
		g.setColor(Color.RED);
		g.drawOval(p.x - 5, p.y - 5, 10, 10);
		g.setColor(Color.GREEN);
		g.drawLine(p.x-2, p.y-2, p.x+2, p.y+2);
		g.drawLine(p.x-2, p.y+2, p.x+2, p.y-2);
	}
	
	private void drawLines(ArrayList<Point> liste, Graphics g) {
		g.setColor(Color.BLACK);
		for (int i = 0; i < points.size()-1; i++) {
			Point p1 = points.get(i);
			Point p2 =points.get(i+1);
			g.drawLine(p1.x, p1.y, p2.x, p2.y);
		}
	}
	
	private int isNearPoint(Point p1) {
		for (Point p2 : points) {
//			System.out.println(p1 + " p2: " + p2);
			if (p1.x - CLICK_ABSTAND < p2.x && p2.x < p1.x + CLICK_ABSTAND
				&& p1.y - CLICK_ABSTAND < p2.y && p2.y < p1.y + CLICK_ABSTAND) {
				return points.indexOf(p2);
			}
		}
		return -1;
	}
	
	private void recalculateTangents() {
		tangents = new ArrayList<Point2f>();
		double[][] A = createMatrixA(points.size());
		double[][] Ainverse = Gauss.invertiereMatrix(A);
		double[][] B = createMatrixB(points.size());
		double[][] Px = createMatrixPx();
		double[][] Py = createMatrixPy();
		Gauss.print("A",A);
		Gauss.print("A inverse", Ainverse);
		Gauss.print("B", B);
		Gauss.print("Px", Px);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		double[][] blub = (B, Px);
//		double[][] p_x = Matrix.MultiplyMatrix(Ainverse, blub);
//		double[][] p_y = Matrix.MultiplyMatrix(Ainverse, Matrix.MultiplyMatrix(B, Py));
//		for(int i = 0; i < points.size(); i++) {
//			tangents.add(new Point2f(p_x[i][0], p_y[i][0]));
//		}
	}


	public void mouseClicked(MouseEvent arg0) {
	}


	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	public void mousePressed(MouseEvent arg0) {
		int p = isNearPoint(arg0.getPoint());
		if (p == -1) {
			points.add(arg0.getPoint());
			if(points.size()>4)
				recalculateTangents();
			selectedPoint = -1;
		} else {
			if(arg0.getButton() == MouseEvent.BUTTON3) {
				points.remove(p);
			} else {
				selectedPoint = p;
			}
		}
			
		repaint();
	}


	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	public void mouseDragged(MouseEvent arg0) {
		if (selectedPoint != -1) {
			points.get(selectedPoint).move(arg0.getX(), arg0.getY());
			repaint();
		}
	}
	public void mouseMoved(MouseEvent arg0) {
		int p = isNearPoint(arg0.getPoint());
		if (p != selectedPoint) {
			selectedPoint = p;
			repaint();
		}
	}
	
	private double[][] createMatrixA(int size) {
		double[][] erg = new double[size][size];
		erg[0][0] = 1.0f;
		erg[size-1][size-1] = 1.0f;
		for(int i = 1; i < size-1; i++) {
			erg[i][i-1] = 1.0f;
			erg[i][i] = 4.0f;
			erg[i][i+1] = 1.0f;
		}
		return erg;
	}
	
	private double[][] createMatrixB(int size) {
		double[][] erg = new double[size][size+2];
		erg[0][0] = 1.0f;
		erg[size-1][size+1] = 1.0f;
		for(int i = 1; i < size-1; i++) {
			erg[i][i] = -3.0f;
			erg[i][i+2] = 3.0f;
		}
		return erg;
	}
	
	private double[][] createMatrixPx() {
		double[][] erg = new double[points.size()][1];
		for(int i = 0; i < points.size(); i++) {
			erg[i][0] = points.get(i).x;
		}
		return erg;
	}
	
	private double[][] createMatrixPy() {
		double[][] erg = new double[points.size()][1];
		for(int i = 0; i < points.size(); i++) {
			erg[i][0] = points.get(i).y;
		}
		return erg;
	}
	/*
	 * h1(s) =  2s^3 - 3s^2 + 1
	 * h2(s) = -2s^3 + 3s^2
	 * h3(s) =   s^3 - 2s^2 + s
	 * h4(s) =   s^3 -  s^2
	 */
	
	private float h1(float s) {
		return 2*s*s*s - 3*s*s +1;
	}
	private float h2(float s) {
		return -2*s*s*s + 3*s*s;
	}
	private float h3(float s) {
		return s*s*s - 2*s*s + s;
	}
	private float h4(float s) {
		return s*s*s - s*s;
	}
	
	private Point2f c(float u, Point P0, Point P1, Point2f T0, Point2f T1) {
		Point2f erg = new Point2f();
		erg.x = h1(u)*P0.x + h2(u)*P1.x +h3(u)* T0.x + h4(u)*T1.x;
		erg.y = h1(u)*P0.y + h2(u)*P1.y +h3(u)* T0.y + h4(u)*T1.y;
		return erg;
	}
}
