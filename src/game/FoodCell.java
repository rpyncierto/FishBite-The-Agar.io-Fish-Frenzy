package game;

import java.util.Random;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class FoodCell extends Circle {
    // since sa gitna ung x and y unlike sprites na upper left
    private static final int RADIUS = 10;
    private static final int MIN_X = 0 + RADIUS;
    private static final int MAX_X = GameTimer.MAP_WIDTH - RADIUS;
    private static final int MIN_Y = 0 + RADIUS;
    private static final int MAX_Y = GameTimer.MAP_HEIGHT - RADIUS;
    private static final Random RANDOM = new Random();
    private boolean eaten = false;
    private double positionX;
    private double positionY;

    public FoodCell() { // constructor
        super(RADIUS);
        this.positionX = MIN_X + (Math.random() * (MAX_X - MIN_X));
        this.positionY = MIN_Y + (Math.random() * (MAX_Y - MIN_Y));
        setCenterX(positionX);
        setCenterY(positionY);
        setFill(Color.color(RANDOM.nextDouble(), RANDOM.nextDouble(), RANDOM.nextDouble()));
        setStroke(Color.BLACK);
        setStrokeWidth(3);
    }

    public void setEaten(boolean b) { // setter for isEaten
        this.eaten = b;
    }

    public int getSize() { // getter for size
        return RADIUS;
    }

    public boolean isEaten() { // getter for isEaten boolean value
        return this.eaten;
    }
}