package com.game;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class Obstacle extends Entity {

    /**
     * Constructor for the Obstacle class.
     * @param layer The pane layer to add the obstacle to.
     * @param image The image of the obstacle.
     * @param x The X position of the obstacle.
     * @param y The Y position of the obstacle.
     * @param r The rotation of the obstacle.
     * @param dx The X velocity of the obstacle.
     * @param dy The Y velocity of the obstacle.
     * @param dr The rotation velocity of the obstacle.
     * @param health The health of the obstacle.
     * @param damage The damage of the obstacle.
     */
    public Obstacle(Pane layer, Image image, double x, double y, double r, double dx, double dy, double dr, double health, double damage) {
        super(layer, image, x, y, r, dx, dy, dr, health, damage);
    }

    /**
     * Checks if the obstacle is removable.
     */
    @Override
    public void checkRemovability() {

        if( Double.compare( getY(), Settings.SCENE_HEIGHT) > 0) {
            setRemovable(true);
        }


    }
}