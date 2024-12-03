package com.game.powerUp;

import com.game.Player;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class DoublePointPowerUp extends PowerUp {

    public DoublePointPowerUp(Pane layer, Image image, double x, double y) {
        super(x, y);
        ImageView view = new ImageView(image);
        view.setX(x);
        view.setY(y);
        setVanish(view);
        layer.getChildren().add(view);
    }

    @Override
    public void activate(Player player) {
        player.activatePowerUp(this);
        player.getGame().scheduleTask(() -> player.setDoublePoints(false), 10000);

    }
}
