package com.game;

public class Camera {
    private double y = 0;
    private static final double PLAYER_VERTICAL_OFFSET = Settings.SCENE_HEIGHT * 0.7;
    private static final double SCROLL_SPEED = 2.0;
    private static final double CATCH_UP_SPEED = 4.0;
    
    private double targetY = 0;
    private boolean isMoving = false;
    private double lastPlayerY;

    public Camera() {
        this.lastPlayerY = 0;
    }

    public void update(double playerY) {
        targetY = playerY - PLAYER_VERTICAL_OFFSET;

        if (!isMoving && playerY < lastPlayerY) {
            isMoving = true;
            System.out.println("Camera started following player");
        }

        if (isMoving) {
            y -= SCROLL_SPEED;

            if (y > targetY) {
                y -= CATCH_UP_SPEED;
            }

            y = Math.min(y, targetY);
        }

        lastPlayerY = playerY;
        System.out.println("Camera Y: " + y + " | Player Y: " + playerY + " | Target Y: " + targetY);
    }

    public double getY() {
        return y;
    }

    public double worldToScreen(double worldY) {
        return worldY - y;
    }

    public double screenToWorld(double screenY) {
        return screenY + y;
    }

    public void reset() {
        y = 0;
        targetY = 0;
        isMoving = false;
    }
}
