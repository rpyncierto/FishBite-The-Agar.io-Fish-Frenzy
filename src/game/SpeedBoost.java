package game;

import javafx.scene.image.Image;

public class SpeedBoost extends Consumable {
    private static final Image SPEED_BOOST_IMAGE = new Image("assets/speedPotion.png");

    public SpeedBoost() { // constructor
        super(SPEED_BOOST_IMAGE);
    }
}
