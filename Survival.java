package game;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.swing.JOptionPane;
import javax.swing.Timer;

import game.Enemy.Mine;

public class Survival extends Game {
    static int counter = 0;

    Point[] xpoints = { new Point(-20, -20), new Point(20, -20), new Point(20, 20), new Point(-20, 20) };
    RunX mainShape = new RunX(xpoints, new Point(250, 250));

    private ArrayList<Enemy.Laser> lasers = new ArrayList<>();
    private ArrayList<Enemy.Mine> mines = new ArrayList<>();
    private final int MAX_MINES = 6;
    private ArrayList<Heart> hearts = new ArrayList<>(); // For managing heart objects

    private HeartSpawner heartSpawner = (Graphics g, int numberOfHearts) -> {
        int x = 10;
        int y = 10;
        for (int i = 0; i < numberOfHearts; i++) {
            hearts.add(new Heart(new Point(10 + (hearts.size() * 30), 10)));
        }
    };

    private Timer updateTimer; // Timer for game updates

    public Survival() {
        super("YourGameName!", 800, 600);
        this.setFocusable(true);
        this.requestFocus();
        this.addKeyListener(mainShape);

        // Initialize hearts and mines initially
        heartSpawner.spawnHearts(null, 3); // Spawning 3 hearts initially
        for (int i = 0; i < MAX_MINES; i++) {
            mines.add(new Enemy.Mine(xpoints, new Point(0,0), 0));
        }

        // Set up the game update timer
        updateTimer = new Timer(1000 / 60, e -> gameUpdate()); // Update game 60 times per second
        updateTimer.start();

        // Set up a timer for spawning lasers every second
        new Timer(1000, e -> spawnLaser()).start(); // Spawn a laser every 1 second
    }

    private void gameUpdate() {
        // Handle mine expiration and maintain mine count
        long currentTime = System.currentTimeMillis();
        Iterator<Enemy.Mine> mineIterator = mines.iterator();
        while (mineIterator.hasNext()) {
            Enemy.Mine mine = mineIterator.next();
            if (currentTime - mine.spawnTime > 10000) { // If the mine has existed for more than 10 seconds
                mineIterator.remove(); // Remove expired mines
            }
        }

        // Ensure there are always MAX_MINES mines
        Random rand = new Random(); // Use a single Random instance for efficiency
        while (mines.size() < MAX_MINES) { // Keep mines at maximum
            // Generate the shape for the mine according to your game design
            Point newMinePosition = new Point(rand.nextInt(800 - Enemy.Mine.SIZE), rand.nextInt(600 - Enemy.Mine.SIZE));
            Point[] mineShape = new Point[]{new Point(0, 0), new Point(0, Enemy.Mine.SIZE), 
                                            new Point(Enemy.Mine.SIZE, Enemy.Mine.SIZE), new Point(Enemy.Mine.SIZE, 0)};
            mines.add(new Enemy.Mine(mineShape, newMinePosition, 0)); // Add new mines with no rotation
        }

        // Check for collisions
        checkCollisions();

        repaint(); // Repaint at the end of update
    }

    private void checkCollisions() {
        // Check for collisions between mainShape and mines
        Iterator<Enemy.Mine> mineIterator = mines.iterator();
        while (mineIterator.hasNext()) {
            Enemy.Mine mine = mineIterator.next();
            if (mainShape.collides(mine)) { // Assumes a method exists in RunX for collision checking
                if (mine.isPositive) {
                	hearts.add(new Heart(new Point(10 + (hearts.size() * 30), 10))); // Or use your existing logic for positioning
                } else {
                    int heartsToRemove = Math.min(2, hearts.size());
                    for (int i = 0; i < heartsToRemove; i++) {
                        if (!hearts.isEmpty()) {
                            hearts.remove(hearts.size() - 1); // Remove the last heart
                        }
                    }
                }
                mineIterator.remove(); // Remove collided mine
                if (hearts.isEmpty()) {
                    gameOver();
                    return;
                }
            }
        }
     // Check for collisions between mainShape and lasers
        Iterator<Enemy.Laser> laserIterator = lasers.iterator();
        while (laserIterator.hasNext()) {
            Enemy.Laser laser = laserIterator.next();
            if (mainShape.collides(laser)) { // Assumes a method exists in RunX for collision checking
               int heartsToRemove = Math.min(2, hearts.size());
               for (int i = 0; i < heartsToRemove; i++) {
            	   if (!hearts.isEmpty()) {
                      hearts.remove(hearts.size() - 1); // Remove the last heart
                   }
                }
                mineIterator.remove(); // Remove collided mine
                if (hearts.isEmpty()) {
                    gameOver();
                    return;
                }
            }
        }
    }


    private void gameOver() {
        updateTimer.stop(); // Stop the game updates
        JOptionPane.showMessageDialog(null, "Game Over! Your score is: " + counter + "\nPress OK to restart.");
        restartGame(); // Call a method to restart the game
    }

    private void restartGame() {
        // Reset all game elements to their initial states
        hearts.clear();
        lasers.clear();
        mines.clear();
        counter = 0;
        // Repopulate initial game elements like hearts and mines
        heartSpawner.spawnHearts(null, 3);
        for (int i = 0; i < MAX_MINES; i++) {
            mines.add(new Enemy.Mine(xpoints, null, 0));
        }
        updateTimer.start(); // Restart game updates
    }


    private void spawnLaser() {
    	lasers.add(new Enemy.Laser(xpoints, new Point(0,0), 0));
    }

    public void paint(Graphics brush) {
        brush.setColor(Color.black);
        brush.fillRect(0, 0, width, height);
        mainShape.move();
        mainShape.paint(brush);
        // Draw hearts
        for (Heart heart : hearts) {
            heart.draw(brush);
        }

        // Draw lasers
        for (Enemy.Laser laser : lasers) {
            laser.draw(brush);
        }

        // Draw mines
        for (Enemy.Mine mine : mines) {
            mine.draw(brush);
        }
        checkCollisions();
        // Existing debugging and counter code
        counter++;
        brush.setColor(Color.white);
        brush.drawString("Counter is " + counter, 10, 10);
    }

    public void drawHeart(Graphics g, int x, int y) {
        g.setColor(Color.RED); // Set color to red
        g.fillOval(x, y, 10, 10); // Draw left half of the heart
        g.fillOval(x + 10, y, 10, 10); // Draw right half of the heart
        g.fillPolygon(new int[] { x, x + 10, x + 20 }, new int[] { y + 5, y + 15, y + 5 }, 3); // Draw the bottom triangle
    }

    // HeartSpawner interface definition
    @FunctionalInterface
    interface HeartSpawner {
        void spawnHearts(Graphics g, int count);
    }

    // Main method to start the game
    public static void main(String[] args) {
        Survival a = new Survival();
        a.repaint();
    }
}