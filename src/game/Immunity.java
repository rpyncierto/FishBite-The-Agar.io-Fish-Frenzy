package game;

import javafx.scene.image.Image;

public class Immunity extends Consumable {

    private static final Image IMMUNITY_IMAGE = new Image("assets/immunityPotion.png");

    public Immunity() { // constructor
        super(IMMUNITY_IMAGE);
    } 
}
