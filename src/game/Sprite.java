package game;


import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Sprite {
    protected ImageView sprite;
    protected Image image;
    protected double size = 40;
    protected double speed = 120/size;
    protected double x;
    protected double y;
    protected boolean eaten = false;
    protected boolean foodAhead;
    protected int currentImageIndex = 0;
    protected int scale = 1;
    protected boolean leftKeyPressed = false;
    protected boolean rightKeyPressed = false;
    protected boolean upKeyPressed = false;
    protected boolean downKeyPressed = false;
    protected Timeline animation;

    
    public Sprite(Image image, double x, double y) { // constructor
        this.sprite = new ImageView(image);
        this.x = x;
        this.y = y;
        this.sprite.setFitWidth(size);
        this.sprite.setFitHeight(size);
        this.sprite.setX(x);
        this.sprite.setY(y);
    }

    public void grow(double size) { // method for growing of the sprite if it eats something (food/enemy/player)
        this.sprite.setFitWidth(this.sprite.getFitWidth() + size);
        this.sprite.setFitHeight(this.sprite.getFitHeight() + size);
        this.sprite.setX(this.sprite.getX()-size/2);
        this.sprite.setY(this.sprite.getY()-size/2);
        this.setSize(size);
        this.setSpeed(120/getSize());
    }

    public void move(){}; // a method in which all its subclasses inherits

    public void updateImage() { // method that updates the image of the sprite
        this.sprite.setImage(image);
    }

    public void setFoodAhead(boolean b) { // setter for foodAhead boolean value
        this.foodAhead = b;
    }

    public void setAnimation(Timeline animation) { // setter for animation
        this.animation = animation;
    }

    public void setSize(double size) { // setter for size
        this.size += size;
    }

    public void setSpeed(double speed) { // setter for speed
        this.speed = speed;
    }

    public void setX(double x) { // setter for x coordinate
        this.x = x;
        this.sprite.setX(x);
    }

    public void setY(double y) { // setter for y coordinate
        this.y = y;
        this.sprite.setY(y);
    }

    public void setEaten(boolean eaten) { // setter for eaten boolean value
        this.eaten = eaten;
    }

    public ImageView getSprite() { // getter for sprite
        return this.sprite;
    }
    
    public double getSpeed() { // getter for speed
        return this.speed;
    }
    
    public double getSize() { // getter for size
        return this.size;
    }
    
    public double getX() { // getter for x coordinate
        return this.x;
    }

    public double getHeight() { // getter for height
        return this.sprite.getFitHeight();
    }

    public double getWidth() { // getter for width
        return this.sprite.getFitWidth();
    }
    
    public double getY() { // getter for y coordinate
        return this.y;
    }

    public boolean isEaten() { // getter for isEaten boolean value
        return this.eaten;
    }

    public boolean getFoodAhead() { // getter for foodAhead boolean value
        return this.foodAhead;
    }

    public Timeline getAnimation() { // getter for animation
        return this.animation;
    }

}