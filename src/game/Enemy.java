package game;

import java.util.HashMap;
import java.util.Map;
import javafx.scene.image.Image;

public class Enemy extends Sprite{
    
    private static final int INITIAL_SIZE = 40;
    Map<Enemy, Image[]> enemyImagesMap = new HashMap<>();
    private static final Image[] ENEMY1_IMAGES = {new Image("assets/enemy1A.png"), new Image("assets/enemy1B.png"), new Image("assets/enemy1Eat.png")};
    private static final Image[] ENEMY2_IMAGES = {new Image("assets/enemy2A.png"), new Image("assets/enemy2B.png"), new Image("assets/enemy2Eat.png")};
    private static final Image[] ENEMY3_IMAGES = {new Image("assets/enemy3A.png"), new Image("assets/enemy3B.png"), new Image("assets/enemy3Eat.png")};
    private static final Image[] ENEMY4_IMAGES = {new Image("assets/enemy4A.png"), new Image("assets/enemy4B.png"), new Image("assets/enemy4Eat.png")};
    private static final Image[][] enemyImages = {ENEMY1_IMAGES, ENEMY2_IMAGES, ENEMY3_IMAGES, ENEMY4_IMAGES};

    public Enemy(int index) { // constructor
        super(enemyImages[index][0], Math.random() * (GameTimer.MAP_WIDTH - INITIAL_SIZE), Math.random() * (GameTimer.MAP_HEIGHT - INITIAL_SIZE));
        enemyImagesMap.put(this, enemyImages[index]);
    }

    public static int getImagesLength() { // get the length of the enemy images
        return enemyImages.length;
    }

    @Override
    public void move () { // moves the enemies
        if (this.leftKeyPressed) {
            if(this.scale != -1) {
                this.scale *= -1;
                this.getSprite().setScaleX(this.scale);
            }
            if(this.getX() > 0) {
                    this.setX(this.getX() - this.speed);
            }
        }
        if (this.rightKeyPressed) {
            if(this.scale != 1) {
                this.scale *= -1;
                this.getSprite().setScaleX(this.scale);
            }
            if(this.getX() + this.getWidth() < 2400) {
                this.setX(this.getX() + this.speed);
                
            }
        }
        if (this.upKeyPressed) {
            if(this.getY() > 0) {
                this.setY(this.getY() - this.speed);
            }
        }
        if (this.downKeyPressed) {
            if(this.getY() + this.getHeight() < 2400) {
                this.setY(this.getY() + this.speed);
            }
        }
    }
    
    public void moveRandomly(int direction) { // randomization of the enemy movement
        switch (direction) {
            case 0: // move up
                this.upKeyPressed = true;
                this.downKeyPressed = false;
                this.leftKeyPressed = false;
                this.rightKeyPressed = false;
                break;
            case 1: // move down
                this.downKeyPressed = true;
                this.upKeyPressed = false;
                this.leftKeyPressed = false;
                this.rightKeyPressed = false;
                break;
            case 2: // move left
                this.leftKeyPressed = true;
                this.rightKeyPressed = false;
                this.upKeyPressed = false;
                this.downKeyPressed = false;
                break;
            case 3: // move right
                this.rightKeyPressed = true;
                this.leftKeyPressed = false;
                this.upKeyPressed = false;
                this.downKeyPressed = false;
                break;
            case 4: // move up diagonal inclined to right
                this.upKeyPressed = true;
                this.rightKeyPressed = true;
                this.downKeyPressed = false;
                this.leftKeyPressed = false;
                break;
            case 5: // move up diagonal inclined to left
                this.upKeyPressed = true;
                this.leftKeyPressed = true;
                this.downKeyPressed = false;
                this.rightKeyPressed = false;
                break;
            case 6: // move down diagonal inclined to right
                this.downKeyPressed = true;
                this.rightKeyPressed = true;
                this.upKeyPressed = false;
                this.leftKeyPressed = false;
                break;
            case 7: // move down diagonal inclined to left
                this.downKeyPressed = true;
                this.leftKeyPressed = true;
                this.upKeyPressed = false;
                this.rightKeyPressed = false;
                break;
        }
        this.move();
    }

    @Override
    public void updateImage() { // method for updating the image of the enemy for animation purposes
        if (this.foodAhead) {
            this.image = enemyImagesMap.get(this)[2];
            this.foodAhead = false;
        } else {
            currentImageIndex = (currentImageIndex + 1) % (2);
            this.image = enemyImagesMap.get(this)[currentImageIndex];
        }
        super.updateImage();
    }
}
