package game;

import java.awt.*;
import java.util.Random;

public class Enemy extends Polygon{ 
	public Enemy(Point[] inShape, Point inPosition, double inRotation) {
		super(inShape, inPosition, inRotation);
	}

    // Inner class for Laser
	public static class Laser extends Enemy {
	    public int width; // Dynamic based on random k
	    public int height = 10; // Fixed height
	    private int x, y;
	    public static final Color COLOR = Color.ORANGE;

	    public Laser(Point[] inShape, Point inPosition, double inRotation) {
			super(inShape, inPosition, inRotation);
	        Random rand = new Random();
	        int k = 1 + rand.nextInt(6); // k is between 1 and 6
	        this.width = 10 * k;
	        this.x = 800; // Assuming game width is 800, spawn from right
	        this.y = rand.nextInt(600 - height); // Assuming game height is 600
	    }

	    public void draw(Graphics g) {
	        g.setColor(COLOR);
	        g.fillRect(x, y, width, height);
	        x -= 10;
	    }
	    
	    public int getX() {
	    	return x;
	    }
	    
	    public int getY() {
	    	return y;
	    }
	}

    // Inner class for Mine
	public static class Mine extends Enemy {
	    public static final int SIZE = 10; // Mines are 10x10 pixels
	    private int x, y;
	    public final boolean isPositive; // True for green (adds heart), false for yellow (removes heart)
	    public final Color color;
	    public long spawnTime; // Timestamp when the mine was spawned

	    public Mine(Point[] inShape, Point inPosition, double inRotation) {
			super(inShape, inPosition, inRotation);
	        Random rand = new Random();
	        this.isPositive = rand.nextBoolean();
	        this.color = isPositive ? Color.GREEN : Color.RED;
	        this.x = rand.nextInt(800 - SIZE); // Random x position within the screen width
	        this.y = rand.nextInt(600 - SIZE); // Random y position within the screen height
	        this.spawnTime = System.currentTimeMillis(); // Set the spawn time to current time
	    }

	    // Add a method to check if the mine should expire
	    public boolean shouldExpire() {
	        long elapsedTime = System.currentTimeMillis() - this.spawnTime;
	        return elapsedTime > 10000; // 10000 milliseconds equals 10 seconds
	    }
	    
	    public void draw(Graphics g) {
	        g.setColor(color);
	        g.fillRect(x, y, SIZE, SIZE); // Draw the mine as a filled square
	    }
	    
	    public int getX() {
	    	return x;
	    }
	    
	    public int getY() {
	    	return y;
	    }
	}
}