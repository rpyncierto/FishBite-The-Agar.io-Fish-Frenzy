package game;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

public class Player extends Sprite {

    private Node node;
    public static final int INITIAL_SIZE = 40;
    private static final Image[] PLAYER_IMAGES = {new Image("assets/playerA.png"), new Image("assets/playerB.png"), new Image("assets/playerEat.png")};
    private static final double PLAYER_INITIALX = (GameTimer.MAP_WIDTH / 2) - (INITIAL_SIZE / 2);
    private static final double PLAYER_INITIALY = (GameTimer.MAP_WIDTH / 2) - (INITIAL_SIZE / 2);
    private boolean immune = false;
    private boolean boosted = false;
    private int foodEaten = 0;
    private int blobsEaten = 0;

    public Player() { // constructor
        super(PLAYER_IMAGES[0], PLAYER_INITIALX, PLAYER_INITIALY);
    }

    public void update(KeyCode keyCode) { // method for the listener
        switch(keyCode) {
            case W:
                this.upKeyPressed = true;
                break;
            case S:
                this.downKeyPressed = true;
                break;
            case A:
                this.leftKeyPressed = true;
                break;
            case D:
                this.rightKeyPressed = true;
                break;
            default:
                break;
        }
    }

    @Override
    public void move () { // method for moving of the player
        if (this.leftKeyPressed) {
            if(this.scale != -1) {
                this.scale *= -1;
                this.getSprite().setScaleX(this.scale);
            }
            if(this.getX() >= 0) {
                if(this.boosted) {
                    this.setX(this.getX() - this.speed * 2);
                } else {
                    this.setX(this.getX() - this.speed);
                }
            }
        }
        if (this.rightKeyPressed) {
            if(this.scale != 1) {
                this.scale *= -1;
                this.getSprite().setScaleX(this.scale);
            }
            if(this.getX() + this.getWidth() <= 2400) {
                if(this.boosted) {
                    this.setX(this.getX() + this.speed * 2);
                } else {
                    this.setX(this.getX() + this.speed);
                }
            }
        }
        if (this.upKeyPressed) {
            if(this.getY() >= 0) {
                if (this.boosted) {
                    this.setY(this.getY() - this.speed * 2);
                } else {
                    this.setY(this.getY() - this.speed);
                }
            }
        }
        if (this.downKeyPressed) {
            if(this.getY() + this.getHeight() <= 2400) {
                if (this.boosted) {
                    this.setY(this.getY() + this.speed * 2);
                } else {
                    this.setY(this.getY() + this.speed);
                }
            }
        }
    }

    public void requestFocus() { // method for requesting focus for the player sprite
        this.node.requestFocus();
    }

    public void stop(KeyCode keyCode) { // another method for the listener
        switch(keyCode) {
            case W:
                this.upKeyPressed = false;
                break;
            case S:
                this.downKeyPressed = false;
                break;
            case A:
                this.leftKeyPressed = false;
                break;
            case D:
                this.rightKeyPressed = false;
                break;
            default:
                break;
        }
    }

    @Override
    public void updateImage() { // method for updating the image of the player for animation purposes
        if(this.foodAhead) {
            this.image = PLAYER_IMAGES[PLAYER_IMAGES.length-1];
            this.foodAhead = false;
        } else {
            this.currentImageIndex = (currentImageIndex + 1) % (PLAYER_IMAGES.length - 1);
            this.image = PLAYER_IMAGES[currentImageIndex];
        }
        super.updateImage();
    }

    public void setImmune(boolean b) { // setter for immune
        this.immune = b;
    }

    public void setBoosted(boolean b) { // setter for boosted
        this.boosted = b;
    }

    public void increaseBlobsEaten() { // setter for blobs taken
        this.blobsEaten++;
    }
    
    public void increaseFoodEaten() { // setter for food taken
        this.foodEaten++;
    }

    public boolean isImmune() { // getter for immune
        return this.immune;
    }

    public int getFoodEaten() { // getter for food taken
        return this.foodEaten;
    }

    public int getBlobsEaten() { // getter for blobs taken
        return this.blobsEaten;
    }
}
