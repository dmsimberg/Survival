package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import game.Enemy.Laser;
import game.Enemy.Mine;

public class RunX extends Polygon implements java.awt.event.KeyListener {
	private boolean forwardKey = false;
	private boolean leftKey = false;
	private boolean rightKey = false;

	public RunX(Point[] inShape, Point inPosition) {
		super(inShape, inPosition, 0);
	}
	
	public void paint(Graphics brush) {
		int[] x = new int[getPoints().length];
		int[] y = new int[getPoints().length];
		Point[] points = getPoints();
		for (int i = 0; i < points.length; i++) {
			x[i] = (int)points[i].getX();
			y[i] = (int)points[i].getY();
		}
		brush.setColor(Color.blue);
		brush.fillPolygon(x, y, points.length);
	}
	
	public void move() {
		int stepSize = 10;
		
		if (rightKey) {
			this.rotation++;
		}
		if (leftKey) {
			this.rotation--;
		}
		if (forwardKey) {
			double currentX = this.position.getX();
			double currentY = this.position.getY();
			double newX = currentX + (stepSize * Math.cos(Math.toRadians(rotation)));
			double newY = currentY + (stepSize * Math.sin(Math.toRadians(rotation)));
			this.position.setX(newX);
			this.position.setY(newY);
		}
	}
	
	public boolean collides(Polygon other) {
		boolean collides = false;
		if (other instanceof Laser) {
			Laser laser = (Laser) other;
			Point enemyCenter = new Point(laser.getX(), laser.getY());
			if (contains(enemyCenter)) {
				collides = true;
			}
		}
		if (other instanceof Mine) {
			Mine mine = (Mine) other;
			Point enemyCenter = new Point(mine.getX(), mine.getY());
			if (contains(enemyCenter)) {
				collides = true;
			}
		}
		return collides;
	}
	
	public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP) {
				forwardKey = true;
			}
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				leftKey = true;
			}
			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				rightKey = true;
			}
	}
	
	public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP) {
				forwardKey = false;
			}
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				leftKey = false;
			}
			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				rightKey = false;
			}
	}
	
	public void keyTyped(KeyEvent e) {
		
	}
}
