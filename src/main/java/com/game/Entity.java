package com.game;

import java.util.Timer;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public abstract class Entity {

    Image image;
    ImageView imageView;

    Pane layer;

    double x;
    double y;
    double r;

    double dx;
    double dy;
    double dr;

    double health;
    double damage;

    boolean removable = false;

    double w;
    double h;

    boolean canMove = true;

    private Game game; // added since last commit

    /*
     * Attribute for powerUp
     */
    private boolean clockActive = false;
    private Timer clockTimer;

    private boolean frostWalkerActive = false;
    private Timer frostWalkerTimer; // power up last 20sc

    private boolean jetpackActive = false;

    private boolean shieldActive = false;

    /**
     * Constructor for the Entity class.
     * 
     * @param layer  The pane layer to add the entity to.
     * @param image  The image of the entity.
     * @param x      The X position of the entity.
     * @param y      The Y position of the entity.
     * @param r      The rotation of the entity.
     * @param dx     The X velocity of the entity.
     * @param dy     The Y velocity of the entity.
     * @param dr     The rotation velocity of the entity.
     * @param health The health of the entity.
     * @param damage The damage of the entity.
     */
    public Entity(Pane layer, Image image, double x, double y, double r, double dx, double dy, double dr, double health,
            double damage) {

        this.layer = layer;
        this.image = image;
        this.x = x;
        this.y = y;
        this.r = r;
        this.dx = dx;
        this.dy = dy;
        this.dr = dr;

        this.health = health;
        this.damage = damage;

        this.imageView = new ImageView(image);
        this.imageView.relocate(x, y);
        this.imageView.setRotate(r);

        this.w = image.getWidth(); // imageView.getBoundsInParent().getWidth();
        this.h = image.getHeight(); // imageView.getBoundsInParent().getHeight();

        addToLayer();

    }

    /**
     * Adds the entity to the layer.
     */
    public void addToLayer() {
        this.layer.getChildren().add(this.imageView);
    }

    /**
     * Removes the entity from the layer.
     */
    public void removeFromLayer() {
        this.layer.getChildren().remove(this.imageView);
    }

    /**
     * Gets the layer of the entity.
     * 
     * @return The layer of the entity.
     */
    public Pane getLayer() {
        return layer;
    }

    /**
     * Sets the layer of the entity.
     * 
     * @param layer The layer to set.
     */
    public void setLayer(Pane layer) {
        this.layer = layer;
    }

    /**
     * Gets the X position of the entity.
     * 
     * @return The X position of the entity.
     */
    public double getX() {
        return x;
    }

    /**
     * Sets the X position of the entity.
     * 
     * @param x The X position to set.
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Gets the Y position of the entity.
     * 
     * @return The Y position of the entity.
     */
    public double getY() {
        return y;
    }

    /**
     * Sets the Y position of the entity.
     * 
     * @param y The Y position to set.
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Gets the rotation of the entity.
     * 
     * @return The rotation of the entity.
     */
    public double getR() {
        return r;
    }

    /**
     * Sets the rotation of the entity.
     * 
     * @param r The rotation to set.
     */
    public void setR(double r) {
        this.r = r;
    }

    /**
     * Gets the X velocity of the entity.
     * 
     * @return The X velocity of the entity.
     */
    public double getDx() {
        return dx;
    }

    /**
     * Sets the X velocity of the entity.
     * 
     * @param dx The X velocity to set.
     */
    public void setDx(double dx) {
        this.dx = dx;
    }

    /**
     * Gets the Y velocity of the entity.
     * 
     * @return The Y velocity of the entity.
     */
    public double getDy() {
        return dy;
    }

    /**
     * Sets the Y velocity of the entity.
     * 
     * @param dy The Y velocity to set.
     */
    public void setDy(double dy) {
        this.dy = dy;
    }

    /**
     * Gets the rotation velocity of the entity.
     * 
     * @return The rotation velocity of the entity.
     */
    public double getDr() {
        return dr;
    }

    /**
     * Sets the rotation velocity of the entity.
     * 
     * @param dr The rotation velocity to set.
     */
    public void setDr(double dr) {
        this.dr = dr;
    }

    /**
     * Gets the health of the entity.
     * 
     * @return The health of the entity.
     */
    public double getHealth() {
        return health;
    }

    /**
     * Gets the damage of the entity.
     * 
     * @return The damage of the entity.
     */
    public double getDamage() {
        return damage;
    }

    /**
     * Sets the damage of the entity.
     * 
     * @param damage The damage to set.
     */
    public void setDamage(double damage) {
        this.damage = damage;
    }

    /**
     * Sets the health of the entity.
     * 
     * @param health The health to set.
     */
    public void setHealth(double health) {
        this.health = health;
    }

    /**
     * Gets the removable flag of the entity.
     * 
     * @return The removable flag of the entity.
     */
    public boolean isRemovable() {
        return removable;
    }

    /**
     * Sets the removable flag of the entity.
     * 
     * @param removable The removable flag to set.
     */
    public void setRemovable(boolean removable) {
        this.removable = removable;
    }

    /**
     * Moves the entity.
     */
    public void move() {

        if (!canMove)
            return;

        x += dx;
        y += dy;
        r += dr;

        updateUI();

    }

    /**
     * Checks if the entity is alive.
     * 
     * @return True if the entity is alive, false otherwise.
     */
    public boolean isAlive() {
        return Double.compare(health, 0) > 0;
    }

    /**
     * Gets the image view of the entity.
     * 
     * @return The image view of the entity.
     */
    public ImageView getView() {
        return imageView;
    }

    /**
     * Updates the UI of the entity.
     */
    public void updateUI() {

        imageView.relocate(x, y);
        imageView.setRotate(r);

    }

    /**
     * Gets the width of the entity.
     * 
     * @return The width of the entity.
     */
    public double getWidth() {
        return w;
    }

    /**
     * Gets the height of the entity.
     * 
     * @return The height of the entity.
     */
    public double getHeight() {
        return h;
    }

    /**
     * Gets the center X position of the entity.
     * 
     * @return The center X position of the entity.
     */
    public double getCenterX() {
        return x + w * 0.5;
    }

    /**
     * Gets the center Y position of the entity.
     * 
     * @return The center Y position of the entity.
     */
    public double getCenterY() {
        return y + h * 0.5;
    }

    /**
     * Checks if the entity collides with a power up.
     * 
     * @param powerUps The power up to check collision with.
     * @return True if the entities collide, false otherwise.
     */
    public boolean collidesWithPowerUps(Entity powerUps) {
        return (powerUps.x + powerUps.w >= x && powerUps.y + powerUps.h >= y
                && powerUps.x <= x + w && powerUps.y <= y + h);
    }

    // TODO: per-pixel-collision
    /**
     * Checks if the entity collides with another entity.
     * 
     * @param otherObstacle The other entity to check collision with.
     * @return True if the entities collide, false otherwise.
     */
    public boolean collidesWith(Entity otherObstacle) {

        return (otherObstacle.x + otherObstacle.w >= x && otherObstacle.y + otherObstacle.h >= y
                && otherObstacle.x <= x + w && otherObstacle.y <= y + h);

    }

    /**
     * Reduces the health of the entity by the amount of damage that the given
     * obstacle can inflict.
     * 
     * @param obstacle The obstacle to inflict damage on the entity.
     */
    public void getDamagedBy(Entity obstacle) {
        // shield here ??
        kill();
    }

    /**
     * Sets the health of the entity to 0.
     */
    public void kill() {
        setHealth(0);
        // stop the game and show the score with setText score
    }

    /**
     * Sets the flag that the sprite can be removed from the UI.
     */
    public void remove() {
        setRemovable(true);
    }

    /**
     * Sets the flag that the sprite can't move anymore.
     */
    public void stopMovement() {
        this.canMove = false;
    }

    /**
     * Checks if the entity is removable.
     */
    public abstract void checkRemovability();

    /**
     * Applies world scroll to the entity.
     *
     * @param scrollAmount The amount to scroll.
     */
    public void applyScroll(double scrollAmount) {
        y += scrollAmount;
        updateUI();
    }

}