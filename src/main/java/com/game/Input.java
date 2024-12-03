package com.game;

import java.util.BitSet;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class Input {

    /**
     * Bitset which registers if any {@link KeyCode} keeps being pressed or if it is
     * released.
     */
    private BitSet keyboardBitSet = new BitSet();

    // -------------------------------------------------
    // default key codes
    // will vary when you let the user customize the key codes or when you add
    // support for a 2nd player
    // -------------------------------------------------

    private KeyCode upKey = KeyCode.UP;
    private KeyCode downKey = KeyCode.DOWN;
    private KeyCode leftKey = KeyCode.LEFT;
    private KeyCode rightKey = KeyCode.RIGHT;
    private KeyCode primaryWeaponKey = KeyCode.SPACE;
    private KeyCode secondaryWeaponKey = KeyCode.CONTROL;

    Scene scene;

    /**
     * Constructor for the Input class.
     * 
     * @param scene The scene to add the input to.
     */
    public Input(Scene scene) {
        this.scene = scene;
    }

    /**
     * Adds the listeners.
     */
    public void addListeners() {

        scene.addEventFilter(KeyEvent.KEY_PRESSED, keyPressedEventHandler);
        scene.addEventFilter(KeyEvent.KEY_RELEASED, keyReleasedEventHandler);

    }

    /**
     * Removes the listeners.
     */
    public void removeListeners() {

        scene.removeEventFilter(KeyEvent.KEY_PRESSED, keyPressedEventHandler);
        scene.removeEventFilter(KeyEvent.KEY_RELEASED, keyReleasedEventHandler);

    }

    /**
    * Key pressed event handler.
     */
    private EventHandler<KeyEvent> keyPressedEventHandler = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {

            // register key down
            keyboardBitSet.set(event.getCode().ordinal(), true);

        }
    };

    /**
     * Key released event handler.
     */
    private EventHandler<KeyEvent> keyReleasedEventHandler = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {

            // register key up
            keyboardBitSet.set(event.getCode().ordinal(), false);

        }
    };

    // -------------------------------------------------
    // Evaluate bitset of pressed keys and return the player input.
    // If direction and its opposite direction are pressed simultaneously, then the
    // direction isn't handled.
    // -------------------------------------------------

    /**
     * Checks if the player is moving up.
     * 
     * @return True if the player is moving up, false otherwise.
     */
    public boolean isMoveUp() {
        return keyboardBitSet.get(upKey.ordinal())
                || keyboardBitSet.get(KeyCode.Z.ordinal()) && !keyboardBitSet.get(downKey.ordinal());
    }

    /**
     * Checks if the player is moving down.
     * 
     * @return True if the player is moving down, false otherwise.
     */
    public boolean isMoveDown() {
        return keyboardBitSet.get(downKey.ordinal())
                || keyboardBitSet.get(KeyCode.S.ordinal()) && !keyboardBitSet.get(upKey.ordinal());
    }

    /**
     * Checks if the player is moving left.
     * 
     * @return True if the player is moving left, false otherwise.
     */
    public boolean isMoveLeft() {
        return keyboardBitSet.get(leftKey.ordinal())
                || keyboardBitSet.get(KeyCode.Q.ordinal()) && !keyboardBitSet.get(rightKey.ordinal());
    }

    /**
     * Checks if the player is moving right.
     * 
     * @return True if the player is moving right, false otherwise.
     */
    public boolean isMoveRight() {
        return keyboardBitSet.get(rightKey.ordinal())
                || keyboardBitSet.get(KeyCode.D.ordinal()) && !keyboardBitSet.get(leftKey.ordinal());

    }

    /**
     * Checks if the player is firing the primary weapon.
     * 
     * @return True if the player is firing the primary weapon, false otherwise.
     */
    public boolean isFirePrimaryWeapon() {
        return keyboardBitSet.get(primaryWeaponKey.ordinal());
    }

    /**
     * Checks if the player is firing the secondary weapon.
     * 
     * @return True if the player is firing the secondary weapon, false otherwise.
     */
    public boolean isFireSecondaryWeapon() {
        return keyboardBitSet.get(secondaryWeaponKey.ordinal());
    }

}
