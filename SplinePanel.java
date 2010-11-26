package splines;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JPanel;

public class SplinePanel extends JPanel implements MouseListener, MouseMotionListener {

	private ArrayList<Point> points;
	private int selectedPoint;
	private static final int CLICK_ABSTAND = 5;
	
	public SplinePanel() {
		points = new ArrayList<Point>();
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawPoints(points, g);
		drawSpline(points, g);
	}
	
	private void drawPoints(ArrayList<Point> liste, Graphics g) {
		g.setColor(Color.RED);
		for (int i = 0; i < points.size(); i++) {
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
	
	private void drawSelectedPoint(Point p, Graphics g) {
		g.setColor(Color.RED);
		g.drawOval(p.x - 5, p.y - 5, 10, 10);
		g.setColor(Color.GREEN);
		g.drawLine(p.x-2, p.y-2, p.x+2, p.y+2);
		g.drawLine(p.x-2, p.y+2, p.x+2, p.y-2);
	}
	
	private void drawSpline(ArrayList<Point> liste, Graphics g) {
		g.setColor(Color.BLACK);
		for (int i = 0; i < points.size()-1; i++) {
			Point p1 = points.get(i);
			Point p2 =points.get(i+1);
			g.drawLine(p1.x, p1.y, p2.x, p2.y);
		}
	}
	
	private int isNearPoint(Point p1) {
		for (Point p2 : points) {
			System.out.println(p1 + " p2: " + p2);
			if (p1.x - CLICK_ABSTAND < p2.x && p2.x < p1.x + CLICK_ABSTAND
				&& p1.y - CLICK_ABSTAND < p2.y && p2.y < p1.y + CLICK_ABSTAND) {
				return points.indexOf(p2);
			}
		}
		return -1;
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
}
