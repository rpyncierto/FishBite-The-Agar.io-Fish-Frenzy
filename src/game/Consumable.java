package game;

import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Consumable {
    protected ImageView sprite;
    protected final static double SIZE = 30;
    protected double minX = GameTimer.player.getX() + GameTimer.player.getWidth()/2 - 400;
    protected double maxX = GameTimer.player.getX() + GameTimer.player.getWidth()/2 + 400;
    protected double minY = GameTimer.player.getY() + GameTimer.player.getHeight()/2 - 400;
    protected double maxY = GameTimer.player.getY() + GameTimer.player.getHeight()/2 + 400;
    protected double x, y;
    protected boolean consumed;
    protected double coordinate;
    protected AnimationTimer animationTimer;


    public Consumable(Image image) { // constructor
        this.sprite = new ImageView(image);
        this.consumed = false;
        this.sprite.setFitWidth(SIZE);
        this.sprite.setFitHeight(SIZE+10);
        this.x = generateCoordinate(minX, maxX - this.sprite.getFitWidth());
        this.y = generateCoordinate(minY, maxY - this.sprite.getFitHeight());
        this.sprite.setX(this.x);
        this.sprite.setY(this.y);
    }

    public double generateCoordinate(double min, double max){ // method to generate the coordinate of the consumables
        this.coordinate = Math.random() * (max - min) + min;
        if(this.coordinate < 0 || this.coordinate > GameTimer.MAP_WIDTH) {
            // System.out.println("Invalid coordinate");
            generateCoordinate(min, max);
        }
        return this.coordinate;
    }

    public ImageView getSprite() { // getter for sprite
        return this.sprite;
    }
    public boolean isConsumed() { // getter for isConsumed boolean value
        return this.consumed;
    }

    public void setConsumed(boolean consumed) { // setter for is consumed
        this.consumed = consumed;
    }
    
    public double getX() { // getter for x coordinate
        return this.x;
    }

    public double getY() { // getter for y coordinate
        return this.y;
    }
}
