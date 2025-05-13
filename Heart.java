package game;

import java.awt.Color;
import java.awt.Graphics;

interface HeartSpawner {
    void spawnHearts(Graphics g, int count);
}

public class Heart {
    public Point position; // Position of the heart
    
    public Heart(Point position) {
        this.position = position;
    }

	// Method to draw the heart at its position
    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillOval((int)position.x, (int)position.y, 20, 20); // Adjust size as needed
    }
}