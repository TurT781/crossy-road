package com.game.powerUp;

import com.game.Game;
import com.game.Player;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

public abstract class PowerUp extends Game {
    protected double x, y;
    protected boolean collected;
    protected Node vanish;

    public PowerUp(double x, double y) {
        this.x = x;
        this.y = y;
        this.collected = false;
    }

    public void setVanish(Node vanish) {
        this.vanish = vanish;
    }

    public void vanish() {
        if (!collected) {
            collected = true;
            removeFromLayer();
        }
    }

    public void removeFromLayer() {
        if (vanish != null) {
            ((Pane) vanish.getParent()).getChildren().remove(vanish);
        }
    }

    public abstract void activate(Player player);

    public void deactivate() {
        this.collected = false;
    }

    public boolean collected() {
        return collected;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
