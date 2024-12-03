package com.game;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.game.powerUp.DoublePointPowerUp;
import com.game.powerUp.PowerUp;
import com.game.powerUp.ShieldPowerUp;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Game extends Application {

    Random rnd = new Random();

    Pane playfieldLayer;
    Pane scoreLayer;

    Image playerImage;
    Image obstacleImage;
    Image doublePointPowerUpImage;

    Image carRImage;
    Image carLImage;
    Image trainImage;
    Image logImage;
    Image shieldPowerUpImage;

    private Map gameMap;

    private boolean isGameOver = false;
    private VBox gameOverMenu;

    private Text scoreText = new Text();
    private int point = 0;
    private int updateScore;

    List<Player> players = new ArrayList<>();
    List<Obstacle> obstacles = new ArrayList<>();
    List<PowerUp> powerUps = new ArrayList<>();

    boolean collision = false;

    Scene scene;

    /**
     * Gets the power ups.
     * 
     * @return The power ups.
     */
    public List<PowerUp> getPowerUps() {
        return powerUps;
    }

    /**
     * Starts the game.
     * 
     * @param primaryStage The primary stage.
     * @throws IOException If an error occurs.
     */
    @Override
    public void start(Stage primaryStage) throws IOException {

        Group root = new Group();

        // create layers
        playfieldLayer = new Pane();
        scoreLayer = new Pane();

        root.getChildren().add(playfieldLayer);
        root.getChildren().add(scoreLayer);

        scene = new Scene(root, Settings.SCENE_WIDTH, Settings.SCENE_HEIGHT);

        primaryStage.setScene(scene);
        primaryStage.show();

        gameMap = new Map(playfieldLayer);

        loadGame();

        createScoreLayer();
        createPlayers();

        AnimationTimer gameLoop = new AnimationTimer() {

            /**
             * Handles the game loop.
             * 
             * @param now The current time.
             */
            @Override
            public void handle(long now) {
                if (!isGameOver) {
                    // player input
                    players.forEach(player -> player.processInput());

                    // add random enemies
                    spawnCars(true);
                    spawnTrain(true);
                    // spawnLog(true);

                    // movement
                    players.forEach(player -> player.move());
                    obstacles.forEach(obstacle -> obstacle.move());

                    // // Update camera position based on player position
                    // if (!players.isEmpty()) {
                    // Player player = players.get(0);
                    // gameMap.updateViewport(player.getY());
                    // }

                    // check collisions
                    checkCollisions();

                    // update obstacles in scene
                    players.forEach(obstacle -> obstacle.updateUI());
                    obstacles.forEach(obstacle -> obstacle.updateUI());

                    // check if obstacle can be removed
                    obstacles.forEach(obstacle -> obstacle.checkRemovability());

                    // remove removables from list, layer, etc
                    removeObstacles(obstacles);

                    // check collisons with power ups

                    // spawn powerUps
                    spawnPowerUps();

                    // update point
                    updatePoint();

                }

            }
        };
        gameLoop.start();
    }

    /**
     * Handles the game over.
     */
    public void gameOver() {
        isGameOver = true;

        // Create game over menu
        gameOverMenu = new VBox(20); // 20 is the spacing between elements
        gameOverMenu.setAlignment(Pos.CENTER);

        Text gameOverText = new Text("Game Over");
        gameOverText.setFont(Font.font(null, FontWeight.BOLD, 72));
        gameOverText.setFill(Color.RED);
        gameOverText.setStroke(Color.BLACK);

        Text finalScore = new Text("Final Score: " + point);
        finalScore.setFont(Font.font(null, FontWeight.BOLD, 36));
        finalScore.setFill(Color.WHITE);
        finalScore.setStroke(Color.BLACK);

        Button restartButton = new Button("Restart Game");
        restartButton.setFont(Font.font(20));
        restartButton.setOnAction(e -> restartGame());

        gameOverMenu.getChildren().addAll(gameOverText, finalScore, restartButton);

        // Center the menu
        gameOverMenu.setLayoutX((Settings.SCENE_WIDTH - 300) / 2);
        gameOverMenu.setLayoutY((Settings.SCENE_HEIGHT - 200) / 2);

        scoreLayer.getChildren().add(gameOverMenu);
    }

    /**
     * Restarts the game.
     */
    private void restartGame() {
        // Reset game state
        isGameOver = false;
        point = 0;
        collision = false;
        // gameMap.resetViewport();
        // gameMap.resetCamera();

        // Clear all entities
        obstacles.forEach(obstacle -> obstacle.removeFromLayer());
        obstacles.clear();
        players.forEach(player -> player.removeFromLayer());
        players.clear();

        // Remove game over menu
        scoreLayer.getChildren().remove(gameOverMenu);

        // Reset score display
        updatePoint();

        // Create new player
        createPlayers();
    }

    /**
     * Updates the point.
     */
    public void updatePoint() {
        scoreText.setText("Score: " + point);
    }

    /**
     * Updates the score.
     * 
     * @param points The points to update the score with.
     */
    public void updateScore(int points) {
        point += points;
        updateScoreDisplay();
    }

    /**
     * Updates the score display.
     */
    private void updateScoreDisplay() {
        scoreText.setText("Score: " + point);
    }

    /**
     * Gets the update score.
     * 
     * @return The update score.
     */
    public int getUpdateScore() {
        return this.updateScore;
    }

    /**
     * Loads the game.
     */
    private void loadGame() {
        URL playerUrl = getClass().getResource("/for_char1.png");
        URL obstacleUrl = getClass().getResource("sprites/enemy.png");
        URL carRUrl = getClass().getResource("/car_right3.png");
        URL carLUrl = getClass().getResource("/car_left2.png");
        URL trainUrl = getClass().getResource("/train.png");
        URL logUrl = getClass().getResource("/log.png");

        // Image double Point
        URL doublePointPowerUpUrl = getClass().getResource("/doublePoint.png");
        if (doublePointPowerUpUrl == null) {
            throw new RuntimeException("doublePoint.png not found");
        }

        // Image shield
        URL shieldPowerUpUrl = getClass().getResource("/shield.png");
        if (shieldPowerUpUrl == null) {
            throw new RuntimeException("shield.png not found");
        }

        if (playerUrl == null) {
            throw new RuntimeException("player.png not found");
        }
        if (obstacleUrl == null) {
            throw new RuntimeException("enemy.png not found");
        }

        // Load images and scale them to fit grid cells slightly smaller than grid
        // size
        double spriteSize = Map.GRID_SIZE * 0.9; // 90% of grid size
        // double FspriteSize = Map.GRID_WIDTH * 4;
        // double FfspriteSize = Map.GRID_HEIGHT * 4;
        playerImage = new Image(playerUrl.toExternalForm(), spriteSize, spriteSize, true, true);
        obstacleImage = new Image(obstacleUrl.toExternalForm(), spriteSize, spriteSize, true, true);
        carRImage = new Image(carRUrl.toExternalForm(), spriteSize, spriteSize, true, true);
        carLImage = new Image(carLUrl.toExternalForm(), spriteSize, spriteSize, true, true);
        trainImage = new Image(trainUrl.toExternalForm(), 12 * spriteSize, spriteSize, false, true);
        logImage = new Image(logUrl.toExternalForm(), 3 * spriteSize, spriteSize, false, true);
        doublePointPowerUpImage = new Image(doublePointPowerUpUrl.toExternalForm(), spriteSize, spriteSize, true, true);
        shieldPowerUpImage = new Image(shieldPowerUpUrl.toExternalForm(), spriteSize, spriteSize, true, true);
    }

    /**
     * Creates the score layer.
     */
    private void createScoreLayer() {

        // Display the score in the game
        HBox scorePlayer = new HBox(); // New obj Hbox() to use it bellow
        scorePlayer.setAlignment(Pos.TOP_RIGHT); // Set a the text top right (whatever the scene size)
        scorePlayer.setPrefWidth(Settings.SCENE_WIDTH);
        scorePlayer.getChildren().add(scoreText);
        scorePlayer.setPadding(new Insets(25, 25, 25, 25));
        scoreLayer.getChildren().add(scorePlayer);
        scoreText.setFont(Font.font("ARIAL", FontWeight.BOLD, 30));
        scoreText.setText("Score: " + point); // Set the text by "text: " and the point given to the player's position
    }

    /**
     * Creates the players.
     */
    private void createPlayers() {
        Input input = new Input(scene);
        input.addListeners();

        // Center the sprite in the grid cell
        int gridX = Map.GRID_WIDTH / 2;
        int gridY = (int) (Map.GRID_HEIGHT * 0.7);
        double x = Map.gridToPixel(gridX) + (Map.GRID_SIZE - playerImage.getWidth()) / 2;
        double y = Map.gridToPixel(gridY) + (Map.GRID_SIZE - playerImage.getHeight()) / 2;

        Player player = new Player(playfieldLayer, playerImage, x, y, 0, 0, 0, Settings.PLAYER_SHIP_HEALTH, 0,
                Settings.PLAYER_SHIP_SPEED, input, this);

        players.add(player);
    }

    /**
     * Spawns enemies.
     * 
     * @param random Whether to spawn enemies randomly.
     */
    private void spawnCars(boolean random) {
        if (random && rnd.nextInt(Settings.ENEMY_SPAWN_RANDOMNESS = 100) != 0) {
            return;
        }

        // Randomly choose left or right spawn
        boolean spawnRight = rnd.nextBoolean();

        int gridX = rnd.nextInt(Map.GRID_HEIGHT);
        double x, y, speed;

        if (spawnRight) {
            x = Settings.SCENE_WIDTH;
            y = Map.gridToPixel(gridX) + (Map.GRID_SIZE - carLImage.getHeight()) / 2;
            speed = -(rnd.nextDouble() * 1.0 + 8.0);

            Obstacle obstacle = new Obstacle(playfieldLayer, carLImage, x, y, 0, speed, 0, 0, 1, 1);
            obstacles.add(obstacle);
        } else {
            x = -carRImage.getWidth();
            y = Map.gridToPixel(gridX) + (Map.GRID_SIZE - carRImage.getHeight()) / 2;
            speed = rnd.nextDouble() * 1.0 + 8.0;

            Obstacle obstacle = new Obstacle(playfieldLayer, carRImage, x, y, 0, speed, 0, 0, 1, 1);
            obstacles.add(obstacle);
        }
    }

    /**
     * Spawns a train.
     * 
     * @param random Whether to spawn a train randomly.
     */
    private void spawnTrain(boolean random) {
        if (random && rnd.nextInt(Settings.ENEMY_SPAWN_RANDOMNESS = 300) != 0) {
            return;
        }

        // Randomly choose left or right spawn
        boolean spawnRight = rnd.nextBoolean();

        int gridX = rnd.nextInt(Map.GRID_HEIGHT);
        double x, y, speed;

        if (spawnRight) {
            x = Settings.SCENE_WIDTH;
            y = Map.gridToPixel(gridX) + (Map.GRID_SIZE - trainImage.getHeight()) / 2;
            speed = -(rnd.nextDouble() * 1.0 + 50.0);

            Obstacle obstacle = new Obstacle(playfieldLayer, trainImage, x, y, 0, speed, 0, 0, 1, 1);
            obstacles.add(obstacle);
        } else {
            x = -trainImage.getWidth();
            y = Map.gridToPixel(gridX) + (Map.GRID_SIZE - trainImage.getHeight()) / 2;
            speed = rnd.nextDouble() * 1.0 + 50.0;

            Obstacle obstacle = new Obstacle(playfieldLayer, trainImage, x, y, 0, speed, 0, 0, 1, 1);
            obstacles.add(obstacle);
        }
    }

    /**
     * Spawns power ups.
     */
    private void spawnPowerUps() {
        if (rnd.nextInt(Settings.POWERUP_SPAWN_RANDOMNESS) != 0) {
            return;
        }

        // Choisir un type de power-up aléatoire
        boolean isShield = rnd.nextBoolean();

        int gridX = rnd.nextInt(Map.GRID_WIDTH);
        int gridY = rnd.nextInt(Map.GRID_HEIGHT);

        double x = Map.gridToPixel(gridX) + (Map.GRID_SIZE - doublePointPowerUpImage.getWidth()) / 2;
        double y = Map.gridToPixel(gridY) + (Map.GRID_SIZE - doublePointPowerUpImage.getHeight()) / 2;

        // Dont generate a power up on the player
        for (Player player : players) {
            if (player.getX() == x && player.getY() == y) {
                return;
            }
        }

        PowerUp powerUp;
        if (isShield) {
            powerUp = new ShieldPowerUp(playfieldLayer, shieldPowerUpImage, x, y);
        } else {
            powerUp = new DoublePointPowerUp(playfieldLayer, doublePointPowerUpImage, x, y);
        }

        powerUps.add(powerUp);
    }

    /**
     * Removes obstacles from the list.
     * 
     * @param obstacleList The list of obstacles to remove.
     */
    private void removeObstacles(List<? extends Entity> obstacleList) {
        Iterator<? extends Entity> iter = obstacleList.iterator();
        while (iter.hasNext()) {
            Entity obstacle = iter.next();

            if (obstacle.isRemovable()) {

                // remove from layer
                obstacle.removeFromLayer();

                // remove from list
                iter.remove();
            }
        }
    }

    /**
     * Checks for collisions between players and obstacles.
     */
    private void checkCollisions() {
        collision = false;
        for (Player player : players) {
            for (Obstacle obstacle : obstacles) {
                if (player.collidesWith(obstacle)) {
                    collision = true;
                    if (!isGameOver) {
                        gameOver();
                    }
                }

            }
            for (PowerUp powerUp : powerUps) {
                if (player.collidesWithPowerUps(player)) {
                    powerUp.activate(player);
                    powerUp.removeFromLayer();
                    powerUps.remove(powerUp);
                    break;
                }
            }

        }
    }

    /**
     * Schedules a task.
     * 
     * @param task The task to schedule.
     * @param delay The delay.
     */
    public void scheduleTask(Runnable task, long delay) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                task.run();
            }
        }, delay);
    }

    /**
     * Launches the game.
     * 
     * @param args The arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }
}