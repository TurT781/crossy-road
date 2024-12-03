package com.game;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class Map {
    public static final int GRID_SIZE = 80; // Size of each grid cell
    public static final int GRID_WIDTH = (int) (Settings.SCENE_WIDTH / GRID_SIZE);
    public static final int GRID_HEIGHT = (int) (Settings.SCENE_HEIGHT / GRID_SIZE);

    private Canvas gridCanvas;
    private double viewportY; // Track the viewport's Y position
    private static final double VIEWPORT_OFFSET = Settings.SCENE_HEIGHT * 0.7; // Keep player at 70% of screen height

    private static final double SCROLL_SPEED = 2.0; // Constant scroll speed
    private double scrollOffset = 0;

    private static Map instance;

    private Image backgroundSprite;
    private static final String BACKGROUND_PATH = "sprites/grass.png"; // Adjust path as needed
    private Canvas backgroundCanvas; // Separate canvas for background

    private double cameraY = 0; // Track camera position
    private double cameraX = 0; // Add this field for horizontal tracking

    private Camera camera;

    public static Map getInstance() {
        return instance;
    }

    /**
     * Constructor for the Map class.
     * 
     * @param layer The pane layer to add the grid canvas to.
     */
    public Map(Pane layer) {
        camera = new Camera();
        // Load background sprite
        backgroundSprite = new Image(getClass().getResourceAsStream(BACKGROUND_PATH));

        // Create background canvas and add it first
        backgroundCanvas = new Canvas(Settings.SCENE_WIDTH, Settings.SCENE_HEIGHT);
        layer.getChildren().add(0, backgroundCanvas);

        // Add grid canvas above background
        gridCanvas = new Canvas(Settings.SCENE_WIDTH, Settings.SCENE_HEIGHT);
        layer.getChildren().add(1, gridCanvas);

        viewportY = 0;
        drawBackground();
        // drawGrid();
        instance = this; // Store instance
    }

    /**
     * Draws the grid on the grid canvas.
     */
    private void drawGrid() {
        GraphicsContext gc = gridCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, Settings.SCENE_WIDTH, Settings.SCENE_HEIGHT);
        gc.setStroke(Color.LIGHTGRAY);
        gc.setLineWidth(1);

        // Draw vertical lines, adjusted for viewport
        for (int x = 0; x <= Settings.SCENE_WIDTH; x += GRID_SIZE) {
            gc.strokeLine(x, -viewportY % GRID_SIZE, x, Settings.SCENE_HEIGHT);
        }

        // Draw horizontal lines, adjusted for viewport
        double firstY = -viewportY % GRID_SIZE;
        for (double y = firstY; y <= Settings.SCENE_HEIGHT; y += GRID_SIZE) {
            gc.strokeLine(0, y, Settings.SCENE_WIDTH, y);
        }
    }

    // Convert grid coordinates to pixel coordinates
    public static double gridToPixel(int gridPosition) {
        return gridPosition * GRID_SIZE;
    }

    /**
     * Converts pixel coordinates to grid coordinates.
     * 
     * @param pixelPosition The pixel position.
     * @return The grid position.
     */
    public static int pixelToGrid(double pixelPosition) {
        return (int) (pixelPosition / GRID_SIZE);
    }

    /**
     * Snaps a position to the nearest grid position.
     * 
     * @param position The position to snap.
     * @return The snapped position.
     */
    public static double snapToGrid(double position) {
        return Math.round(position / GRID_SIZE) * GRID_SIZE;
    }

    /**
     * Gets the scroll offset.
     * 
     * @return The scroll offset.
     */
    public double getScrollOffset() {
        return scrollOffset;
    }

    /**
     * Updates the viewport and checks if the player is out of bounds.
     * 
     * @param playerY The Y position of the player.
     * @return True if the player is within bounds, false otherwise.
     */
    public boolean updateViewport(double playerY) {
        camera.update(playerY);
        cameraY = camera.getY();

        // Redraw with new camera position
        drawBackground();
        drawGrid();

        // Player dies if they fall too far behind
        double screenY = camera.worldToScreen(playerY);
        return screenY < Settings.SCENE_HEIGHT;
    }

    /**
     * Converts screen coordinates to world coordinates.
     * 
     * @param screenX The X position on the screen.
     * @return The X position in the world.
     */
    public static double getScreenX(double worldX) {
        return worldX - getInstance().cameraX;
    }

    /**
     * Converts world coordinates to screen coordinates.
     * 
     * @param worldX The X position in the world.
     * @return The X position on the screen.
     */
    public static double getWorldX(double screenX) {
        return screenX + getInstance().cameraX;
    }

    private void drawBackground() {
        GraphicsContext gc = backgroundCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, Settings.SCENE_WIDTH, Settings.SCENE_HEIGHT);

        double offsetY = cameraY % backgroundSprite.getHeight();
        for (int x = 0; x < Settings.SCENE_WIDTH; x += backgroundSprite.getWidth()) {
            for (double y = -offsetY; y < Settings.SCENE_HEIGHT; y += backgroundSprite.getHeight()) {
                gc.drawImage(backgroundSprite, x, y);
            }
        }
    }

    /**
     * Resets the viewport.
     */
    public void resetViewport() {
        viewportY = 0;
        updateViewport(0);
    }

    /**
     * Resets the camera.
     */
    public void resetCamera() {
        cameraY = 0;
        cameraX = 0;
        camera.reset();
    }
}
