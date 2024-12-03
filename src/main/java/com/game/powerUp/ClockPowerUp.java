package com.game.powerUp;

import com.game.Player;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class ClockPowerUp extends PowerUp {

    public ClockPowerUp(Pane layer, Image image, double x, double y) {
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
        // TODO we have to make the method to set (speed/2)
        // player.getGame().scheduleTask(() -> player.setClock(false), 10000);

    }
}
