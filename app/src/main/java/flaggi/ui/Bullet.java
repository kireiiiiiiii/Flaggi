/*
 * Author: Matěj Šťastný
 * Date created: 12/6/2024
 * Github link: https://github.com/kireiiiiiiii/Flaggi
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package flaggi.ui;

import java.awt.Container;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;

import flaggi.common.GPanel.Renderable;
import flaggi.common.Sprite;
import flaggi.constants.WidgetTags;
import flaggi.constants.ZIndex;

/**
 * Bullet object with direction and velocity.
 */
public class Bullet implements Renderable, Runnable {

    /////////////////
    // Variables
    ////////////////
    private boolean visible;
    private double[] position; // Current position [x, y] as doubles for precise movement
    private double[] direction; // Normalized direction vector [dx, dy]
    private Sprite sprite;
    private int velocity;
    private int decayTime; // Time in milliseconds
    private boolean running;

    private Thread thread;

    /////////////////
    // Constructor
    ////////////////

    /**
     * Bullet constructor.
     *
     * @param initialPosition - Initial position of the bullet [x, y].
     * @param targetPosition  - Target position the bullet heads to [x, y].
     * @param velocity        - Velocity in points per second.
     * @param decayTime       - Time (in ms) after which the bullet disappears.
     */
    public Bullet(int[] initialPosition, int[] targetPosition, int velocity, int decayTime) {
        this.position = new double[] { initialPosition[0], initialPosition[1] };
        this.velocity = velocity;
        this.decayTime = decayTime;
        this.sprite = new Sprite();
        this.sprite.addAnimation(Arrays.asList("bullet"), "bullet");
        this.sprite.setAnimation("bullet");
        this.visible = true;
        this.running = true;

        // Calculate normalized direction vector
        double dx = targetPosition[0] - initialPosition[0];
        double dy = targetPosition[1] - initialPosition[1];
        double magnitude = Math.sqrt(dx * dx + dy * dy);
        this.direction = new double[] { dx / magnitude, dy / magnitude };

        // Start the movement thread
        this.thread = new Thread(this);
        this.thread.start();
    }

    /////////////////
    // Rendering
    ////////////////

    @Override
    public void render(Graphics2D g, int[] size, int[] origin, Container focusCycleRootAncestor) {
        this.sprite.render(g, (int) this.position[0] + origin[0], (int) this.position[1] + origin[1], focusCycleRootAncestor);
    }

    @Override
    public int getZIndex() {
        return ZIndex.ENVIRONMENT_TOP;
    }

    @Override
    public boolean isVisible() {
        return this.visible;
    }

    @Override
    public void hide() {
        this.visible = false;
    }

    @Override
    public void show() {
        this.visible = true;
    }

    @Override
    public ArrayList<String> getTags() {
        ArrayList<String> tags = new ArrayList<>();
        tags.add(WidgetTags.GAME_ELEMENTS);
        tags.add(WidgetTags.PROJECTILES);
        return tags;
    }

    /////////////////
    // Movement Logic
    ////////////////

    /**
     * Thread for updating the bullet's position and handling its lifecycle.
     */
    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        long lastUpdate = System.currentTimeMillis();

        while (running) {
            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - lastUpdate;

            // Update position based on velocity and elapsed time
            if (elapsedTime > 0) {
                double delta = (elapsedTime / 1000.0) * this.velocity; // Distance to move in this time slice
                this.position[0] += this.direction[0] * delta;
                this.position[1] += this.direction[1] * delta;
                lastUpdate = currentTime;
            }

            // Check for decay
            if (currentTime - startTime >= this.decayTime) {
                this.running = false;
                this.visible = false;
            }

            try {
                Thread.sleep(16); // Roughly 60 updates per second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Stops the bullet thread.
     */
    public void stop() {
        this.running = false;
    }

}
