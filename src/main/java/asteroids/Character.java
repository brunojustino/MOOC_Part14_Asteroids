package asteroids;

import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

public abstract class Character {

    private Polygon character;
    private Point2D movement;

    public Character(Polygon polygon, int x, int y) {
        this.character = polygon;
        this.character.setTranslateX(x);
        this.character.setTranslateY(y);

        this.movement = new Point2D(0, 0);
    }

    public Polygon getCharacter() {
        return character;
    }

    public void turnLeft() {
        this.character.setRotate(this.character.getRotate() - 3);
    }

    public void turnRight() {
        this.character.setRotate(this.character.getRotate() + 3);
    }

    public void move() {
        // Update the character's position based on its movement vector
        double newX = this.character.getTranslateX() + this.movement.getX();
        double newY = this.character.getTranslateY() + this.movement.getY();

        // Reverse the movement direction if the character hits the left or right boundary
        if (newX < 0 || newX > AsteroidsApplication.WIDTH) {
            this.movement = new Point2D(-this.movement.getX(), this.movement.getY());
        }

        // Reverse the movement direction if the character hits the top or bottom boundary
        if (newY < 0 || newY > AsteroidsApplication.HEIGHT) {
            this.movement = new Point2D(this.movement.getX(), -this.movement.getY());
        }

        // Set the new position
        this.character.setTranslateX(newX);
        this.character.setTranslateY(newY);
    }

    public Point2D getMovement() {
        return movement;
    }

    public void setMovement(Point2D movement) {
        this.movement = movement;
    }

    public void accelerate() {
        double changeX = Math.cos(Math.toRadians(this.character.getRotate()));
        double changeY = Math.sin(Math.toRadians(this.character.getRotate()));

        changeX *= 0.01;
        changeY *= 0.01;

        this.movement = this.movement.add(changeX, changeY);
    }

    public boolean collide(Character other) {
        Shape collisionArea = Shape.intersect(this.character, other.getCharacter());
        return collisionArea.getBoundsInLocal().getWidth() != -1;
    }
}
