/*
 * Author: Matěj Šťastný
 * Date created: 12/1/2024
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

package flaggi.common;

import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import flaggi.App;
import flaggi.util.ImageUtil;

/**
 * A Sprite class to handle rendering and animations of image textures.
 *
 */
public class Sprite {

    /////////////////
    // Constants
    ////////////////

    public static final int SPRITE_SCALING = 5;
    public static final String SPRITE_RESOURCE_DIR_PATH = "sprites" + App.FILE_JAR_SEPARATOR;

    /////////////////
    // Variables
    ////////////////

    private final Map<String, List<Image>> animations;
    private String currentAnimation;
    private int currentFrame, fps;
    private FrameUpdater frameUpdater;

    /////////////////
    // Constructor
    ////////////////

    /**
     * Default constructor.
     * 
     */
    public Sprite() {
        this.animations = new HashMap<>();
        this.fps = 0;
    }

    /////////////////
    // Animations
    ////////////////

    /**
     * Adds a new animation with the given frames and name.
     * Frames are loaded from the specified file paths.
     *
     * @param frameName - List of file paths to the animation frames.
     * @param name      - The name of the animation.
     */
    public void addAnimation(List<String> frameName, String name) {
        List<Image> frames = new ArrayList<>();
        for (String framePath : frameName) {
            framePath = SPRITE_RESOURCE_DIR_PATH + framePath + ".png";
            Image image = ImageUtil.getImageFromFile(framePath);
            if (image != null) {
                image = ImageUtil.scaleImage(image,
                        image.getWidth(null) * SPRITE_SCALING,
                        image.getHeight(null) * SPRITE_SCALING,
                        false);
                frames.add(image);
            } else {
                Logger.addLog("Failed to load texture: '" + framePath + "'");
            }
        }
        animations.put(name, frames);
    }

    /**
     * Sets the current animation to be played. Resets the frame back to 0.
     * 
     * @param name - The name of the animation.
     */
    public void setAnimation(String nameString) {
        setAnimationNoReset(nameString);
        this.currentFrame = 0;
    }

    /**
     * Sets the current animation to be played. Continues at the frame of the last
     * animation.
     * 
     * @param name - The name of the animation.
     */
    public void setAnimationNoReset(String name) {
        if (!animations.containsKey(name)) {
            for (String animationName : animations.keySet()) {
                System.out.println(animationName);
            }
            System.out.println("end");
            throw new IllegalArgumentException("Animation not found: '" + name + "'");
        }
        currentAnimation = name;
        // currentFrame = 0;
    }

    /**
     * Starts playing the current animation.
     * 
     */
    public void play() {
        if (currentAnimation == null) {
            throw new IllegalStateException("No animation selected. Use setAnimation() first.");
        }
        if (frameUpdater == null || !frameUpdater.isRunning()) {
            frameUpdater = new FrameUpdater();
            frameUpdater.start();
        }
    }

    /**
     * Stops the animation playback.
     * 
     */
    public void stop() {
        if (frameUpdater != null) {
            frameUpdater.stop();
        }
    }

    /**
     * Sets the frames per second (FPS) for the animation playback.
     * 
     * @param fps - Target frames per second.
     */
    public void setFps(int fps) {
        if (fps <= 0) {
            throw new IllegalArgumentException("FPS must be greater than 0");
        }
        this.fps = fps;
    }

    /////////////////
    // Rendering
    ////////////////

    /**
     * Renders the current frame of the animation on a Graphics2D object.
     *
     * @param g    - The Graphics2D object.
     * @param x    - X coordinate.
     * @param y    - Y coordinate.
     * @param root - The container for rendering context.
     */
    public void render(Graphics2D g, int x, int y, Container root) {
        if (currentAnimation == null || !animations.containsKey(currentAnimation)) {
            return; // Nothing to render
        }
        List<Image> frames = animations.get(currentAnimation);
        if (frames.isEmpty() || currentFrame >= frames.size()) {
            return; // Invalid state
        }
        g.drawImage(frames.get(currentFrame), x, y, root);
    }

    /**
     * Renders a specific animation's current frame on a Graphics2D object.
     * If the provided animation name is invalid or the animation has no frames,
     * nothing will be rendered.
     *
     * @param g             - The Graphics2D object to render on.
     * @param x             - The x-coordinate for rendering.
     * @param y             - The y-coordinate for rendering.
     * @param root          - The container for rendering context.
     * @param animationName - The name of the animation set to render.
     */
    public void render(Graphics2D g, int x, int y, Container root, String animationName, int frame) {
        if (animationName == null || !animations.containsKey(animationName)) {
            System.err.println("Invalid animation name: " + animationName);
            return;
        }

        List<Image> frames = animations.get(animationName);
        if (frames == null || frames.isEmpty()) {
            return;
        }

        frame = frame > frames.size() - 1 || frame < 0 ? frames.size() - 1 : frame;
        g.drawImage(frames.get(frame), x, y, root);
    }

    /////////////////
    // Accesors
    ////////////////

    /**
     * Getter method for the width of the current set frame.
     * 
     * @return {@code int} of the texture width.
     */
    public int getWidth() {
        if (currentAnimation == null || !animations.containsKey(currentAnimation)) {
            if (currentFrame >= 0 && currentFrame < animations.get(currentAnimation).size()) {
                return this.animations.get(this.currentAnimation).get(this.currentFrame).getWidth(null);
            }
        }
        return 0;
    }

    /**
     * Getter method for the height of the current set frame.
     * 
     * @return {@code int} of the texture height.
     */
    public int getHeight() {
        if (currentAnimation == null || !animations.containsKey(currentAnimation)) {
            if (currentFrame >= 0 && currentFrame < animations.get(currentAnimation).size()) {
                return this.animations.get(this.currentAnimation).get(this.currentFrame).getHeight(null);
            }
        }
        return 0;
    }

    /**
     * Gets the current animation, and current frame.
     * 
     * @return - {@code String} in the form of
     *         "current-animation-name:current-frame".
     */
    public String getAnimationFrame() {
        return this.currentAnimation + ":" + currentFrame;
    }

    /////////////////
    // FrameUpdater class
    ////////////////

    /**
     * A utility class to handle frame updates based on FPS.
     * 
     */
    private class FrameUpdater {
        private boolean running = true;

        /**
         * Starts the updater.
         * 
         */
        public void start() {
            Thread updaterThread = new Thread(() -> {
                int intervalMillis = fps == 0 ? 0 : 1000 / fps;
                while (running) {
                    try {
                        Thread.sleep(intervalMillis);
                        updateFrame();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.out.println("Frame updater interrupted.");
                    }
                }
            });
            updaterThread.setDaemon(true);
            updaterThread.start();
        }

        /**
         * Stops the updater.
         */
        public void stop() {
            running = false;
        }

        /**
         * Checks if the updater is running.
         * 
         * @return True if running, otherwise false.
         */
        public boolean isRunning() {
            return running;
        }

        /**
         * Updates the current frame of the animation, ensuring valid frame operations.
         * 
         */
        private void updateFrame() {
            List<Image> frames = animations.get(currentAnimation);

            if (frames == null || frames.isEmpty()) {
                System.err.println("Cannot update frame: animation '" + currentAnimation + "' has no frames.");
                return;
            }

            currentFrame = (currentFrame + 1) % frames.size();
        }
    }

}
