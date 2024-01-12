package asteroids;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AsteroidsApplication extends Application {

    Map<KeyCode, Boolean> pressedKeys = new HashMap<>();

    List<Projectile> projectiles = new ArrayList<>();

    public static int WIDTH = 800;
    public static int HEIGHT = 600;
    private long lastShotTime = 0;

    @Override
    public void start(Stage stage) throws Exception {
        Pane pane = new Pane();
        pane.setPrefSize(WIDTH, HEIGHT);
        //points~~~~~~~~~~~~~~~~~~~~~~
        Text text = new Text(10, 20, "Points: 0");
        pane.getChildren().add(text);
        AtomicInteger points = new AtomicInteger();
        //creates polygon ship
        Ship ship = new Ship(WIDTH / 2, HEIGHT / 2);

        pane.getChildren().add(ship.getCharacter());

        Scene scene = new Scene(pane);
        //create an astriod
        List<Asteroid> asteroids = new ArrayList<>();
        ///sets the amount of asteroids
        Random rnd = new Random();
        for (int i = 0; i < 20; i++) {
            Asteroid asteroid = new Asteroid(rnd.nextInt(WIDTH), rnd.nextInt(HEIGHT));
            asteroids.add(asteroid);
        }

        asteroids.forEach(asteroid -> pane.getChildren().add(asteroid.getCharacter()));
        //~~~~~~~~~~Event listenr~~~~~~~~~~~~~~~~~~~~~
        scene.setOnKeyPressed(event -> {
            pressedKeys.put(event.getCode(), Boolean.TRUE);
        });

        scene.setOnKeyReleased(event -> {
            pressedKeys.put(event.getCode(), Boolean.FALSE);
        });

        new AnimationTimer() {

            @Override
            public void handle(long now) {
                if (ship.isAlive()) {
                    if (Math.random() < 0.018) {
                        Asteroid asteroid = new Asteroid(rnd.nextInt(WIDTH), rnd.nextInt(HEIGHT));
                        if (!asteroid.collide(ship)) {
                            asteroids.add(asteroid);
                            pane.getChildren().add(asteroid.getCharacter());
                        }
                    }

                    if (pressedKeys.getOrDefault(KeyCode.LEFT, false)) {
                        ship.turnLeft();
                    }

                    if (pressedKeys.getOrDefault(KeyCode.RIGHT, false)) {
                        ship.turnRight();
                    }

                    if (pressedKeys.getOrDefault(KeyCode.UP, false)) {
                        ship.accelerate();
                    }
                    if (pressedKeys.getOrDefault(KeyCode.SPACE, false) && System.currentTimeMillis() - lastShotTime > 600) {
                        Projectile projectile = new Projectile((int) ship.getCharacter().getTranslateX(), (int) ship.getCharacter().getTranslateY());
                        projectile.getCharacter().setRotate(ship.getCharacter().getRotate());
                        projectile.accelerate();
                        projectile.setMovement(projectile.getMovement().normalize().multiply(3));

                        // Set the creation time of the projectile
                        projectile.setCreationTime(System.currentTimeMillis());

                        projectiles.add(projectile);
                        pane.getChildren().add(projectile.getCharacter());

                        // Update the last shot time
                        lastShotTime = System.currentTimeMillis();
                    }

                    ship.move();
                    asteroids.forEach(asteroid -> {
                        if (ship.collide(asteroid)) {
                            ship.setAlive(false);
                        }
                        asteroid.move();
                    });
                    projectiles.forEach(projectile -> projectile.move());

                    projectiles.forEach(projectile -> {
                        // Check if the projectile is older than 3 seconds
                        if (System.currentTimeMillis() - projectile.getCreationTime() > 4000) {
                            projectile.setAlive(false);
                        }

                        // Handle collisions between projectiles and asteroids
                        asteroids.forEach(asteroid -> {
                            if (projectile.collide(asteroid)) {
                                points.addAndGet(100);
                                projectile.setAlive(false);
                                asteroid.setAlive(false);
                            }
                        });

                        // Handles the points
                        if (!projectile.isAlive()) {
                            text.setText("Points: " + points.get());
                        }

                    });

                    removeProjectileAndAsteroids(pane, projectiles, asteroids);

                } else { // end if ship alive
                    pane.getChildren().remove(ship.getCharacter());
                    removeProjectileAndAsteroids(pane, projectiles, asteroids);
                    text.setText("Points: " + points.get() + " GAME OVER");
                }
            }
        }
                .start();
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        stage.setTitle(
                "Asteroids!");
        stage.setScene(scene);

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void removeProjectileAndAsteroids(Pane pane, List<Projectile> projectiles, List<Asteroid> asteroids) {
        projectiles.stream()
                .filter(projectile -> !projectile.isAlive())
                .forEach(projectile -> pane.getChildren().remove(projectile.getCharacter()));
        projectiles.removeAll(projectiles.stream()
                .filter(projectile -> !projectile.isAlive())
                .collect(Collectors.toList()));

        asteroids.stream()
                .filter(asteroid -> !asteroid.isAlive())
                .forEach(asteroid -> pane.getChildren().remove(asteroid.getCharacter()));
        asteroids.removeAll(asteroids.stream()
                .filter(asteroid -> !asteroid.isAlive())
                .collect(Collectors.toList()));
    }

    public static int partsCompleted() {
        // State how many parts you have completed using the return value of this method
        return 4;
    }

}
