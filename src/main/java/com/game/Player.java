package com.game;

import java.util.ArrayList;
import java.util.List;

import com.game.powerUp.DoublePointPowerUp;
import com.game.powerUp.PowerUp;

import javafx.scene.control.skin.TextInputControlSkin.Direction;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class Player extends Entity {
    private boolean isDead = false;
    private boolean isJumping = false;
    private long lastJumpTime = 0;
    private static final long JUMP_COOLDOWN = 200;
    private static final long JUMP_DURATION = 150;
    private double jumpStartX;
    private double jumpStartY;
    private Input input;
    private long jumpStartTime;
    private Game game;
    private static final double MAX_IDLE_TIME = 3.0; // 3 seconds max idle time
    private double idleTime = 0;
    private double lastY = 0;
    private int score;
    private List<PowerUp> activePowerUps = new ArrayList<>();
    private boolean doublePoints = false;
    private boolean clock = false; // TODO
    private boolean hasShield = false;
    private double posX, posY;
    private boolean moving = false;
    private Direction direction = Direction.DOWN;

    /**
     * Checks if the player is dead.
     * 
     * @return True if the player is dead, false otherwise.
     */
    public boolean isDead() {
        return isDead;
    }

    /**
     * Checks if the player collides with an enemy.
     * 
     * @param enemy The enemy to check collision with.
     */
    public void checkCollisions(Entity enemy) {
        if (collidesWith(enemy)) {
            if (hasShield) {
                hasShield = false;
            } else {
                die();
            }
        }
    }

    /**
     * Kills the player.
     */
    void die() {
        isDead = true;
        game.gameOver();
        // Optional: You could add death animation or visual feedback here
        // For example: change player sprite to death animation
    }

    /**
     * Constructor for the Player class.
     * 
     * @param layer  The pane layer to add the player to.
     * @param image  The image of the player.
     * @param x      The X position of the player.
     * @param y      The Y position of the player.
     * @param dx     The X velocity of the player.
     * @param dy     The Y velocity of the player.
     * @param dr     The rotation velocity of the player.
     * @param health The health of the player.
     * @param damage The damage of the player.
     * @param speed  The speed of the player.
     * @param input  The input of the player.
     * @param game   The game of the player.
     */
    public Player(Pane layer, Image image, double x, double y, double dx, double dy, double dr, double health,
            double damage, double speed, Input input, Game game) {
        super(layer, image, x, y, dx, dy, dr, health, damage, speed);
        this.input = input;
        this.game = game;
    }

    /**
     * Processes the input for the player.
     */
    public void processInput() {
        if (isDead) {
            return;
        }

        if (!isJumping) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastJumpTime >= JUMP_COOLDOWN) {
                if (input.isMoveUp() && y > 0) {
                    startJump(0, -1);
                    game.updateScore(1);
                    idleTime = 0; // Reset idle time when moving

                } else if (input.isMoveDown() && y < Settings.SCENE_HEIGHT - Map.GRID_SIZE) {
                    startJump(0, 1);
                    game.updateScore(-1);
                    idleTime = 0; // Reset idle time when moving
                } else if (input.isMoveLeft() && x > 0) {
                    startJump(-1, 0);
                    idleTime = 0; // Reset idle time when moving
                } else if (input.isMoveRight() && x < Settings.SCENE_WIDTH - Map.GRID_SIZE) {
                    startJump(1, 0);
                    idleTime = 0; // Reset idle time when moving
                }
            }
        }
    }

    /**
     * Starts the jump for the player.
     * 
     * @param deltaX The X delta of the jump.
     * @param deltaY The Y delta of the jump.
     */
    private void startJump(double deltaX, double deltaY) {
        isJumping = true;
        jumpStartTime = System.currentTimeMillis();
        jumpStartX = x;
        jumpStartY = y;
        dx = deltaX * Map.GRID_SIZE;
        dy = deltaY * Map.GRID_SIZE;
        lastJumpTime = jumpStartTime;
    }

    /**
     * Moves the player.
     */
    @Override
    public void move() {
        if (isDead) {
            return; // Don't move if dead
        }
        if (isJumping) {
            long currentTime = System.currentTimeMillis();
            long jumpTime = currentTime - jumpStartTime;

            if (jumpTime >= JUMP_DURATION) {
                // End jump
                isJumping = false;
                // Calculate final position (one full grid space in the jump direction)
                x = jumpStartX + dx;
                y = jumpStartY + dy;
                dx = 0;
                dy = 0;
                updateUI();
            } else {
                // Animate the jump
                double jumpProgress = jumpTime / (double) JUMP_DURATION;
                double targetX = jumpStartX + dx;
                double targetY = jumpStartY + dy;
                x = jumpStartX + (targetX - jumpStartX) * jumpProgress;
                y = jumpStartY + (targetY - jumpStartY) * jumpProgress;
                double heightOffset = Math.sin(jumpProgress * Math.PI) * (Map.GRID_SIZE / 3);
                y -= heightOffset;
                updateUI();
            }
        }
    }

    /**
     * Activates the power up for the player.
     * 
     * @param powerUp The power up to activate.
     */
    public void activatePowerUp(PowerUp powerUp) {
        if (powerUp instanceof DoublePointPowerUp) {
            this.doublePoints = true;
            activePowerUps.add(powerUp);
            game.updateScore(2);
        }
    }

    /**
     * Deactivates the power up for the player.
     * 
     * @param powerUp The power up to deactivate.
     */
    public void deactivatePowerUp(PowerUp powerUp) {
        if (powerUp instanceof DoublePointPowerUp) {
            this.doublePoints = false;
            activePowerUps.remove(powerUp);
        }
    }

    /**
     * Adds points to the player's score.
     * 
     * @param points The points to add.
     */
    public void addPoints(int points) {
        if (doublePoints) {
            points *= 2;
        }
        score += points;
        game.updateScore(score);
    }

    /**
     * Sets the double points for the player.
     * 
     * @param doublePoints The double points to set.
     */
    public void setDoublePoints(boolean doublePoints) {
        this.doublePoints = doublePoints;
    }

    /**
     * Checks if the player has double points.
     * 
     * @return True if the player has double points, false otherwise.
     */
    public boolean isDoublePoints() {
        return doublePoints;
    }

    /**
     * Updates the score for the player.
     * 
     * @param points The points to update the score by.
     */
    public void updateScore(int points) {
        if (isDoublePoints()) {
            game.updateScore(2);
        }
        game.updateScore(points);
    }

    /**
     * Sets the shield for the player.
     * 
     * @param shield The shield to set.
     */
    public void setShield(boolean shield) {
        this.hasShield = shield;
    }

    /**
     * Gets the game for the player.
     * 
     * @return The game for the player.
     */
    public Game getGame() {
        return this.game;
    }

    /**
     * Checks if the player is removable.
     */
    @Override
    public void checkRemovability() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'checkRemovability'");
    }

    /**
     * Updates the idle time for the player.
     * 
     * @param deltaTime The delta time since the last update.
     */
    public void updateIdleTime(double deltaTime) {
        if (y == lastY) {
            idleTime += deltaTime;
            if (idleTime >= MAX_IDLE_TIME) {
                die();
            }
        } else {
            idleTime = 0;
        }
        lastY = y;
    }

    /**
     * Applies scroll to the player.
     *
     * @param scrollAmount The amount to scroll.
     */
    @Override
    public void applyScroll(double scrollAmount) {
        y += scrollAmount;
    }

}