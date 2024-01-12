package asteroids;

import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;
import java.util.Random;

public class Ship extends Character {

    private boolean alive;

    public Ship(int x, int y) {
        super(new Polygon(-5, -5, 10, 0, -5, 5), x, y);
        Random random = new Random();
        for (int i = 0; i < random.nextInt(70) + 20; i++) {
            super.accelerate();
        }

        this.alive = true;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

}
