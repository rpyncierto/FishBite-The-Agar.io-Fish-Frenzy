package game;

import javafx.animation.TranslateTransition;
import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.util.Duration;

public class GameSubscene extends SubScene{
    private final static String BACKGROUND_IMAGE = "assets/panel.png";
    private final static int WIDTH = 500;
    private final static int HEIGHT = 450;

    private boolean isHidden;

    public GameSubscene() { // constructor
        super(new AnchorPane(), WIDTH, HEIGHT);
        prefWidth(WIDTH);
        prefHeight(HEIGHT);
        BackgroundImage image = new BackgroundImage(new Image(BACKGROUND_IMAGE, WIDTH, HEIGHT, false, true), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, null);
        AnchorPane root2 = (AnchorPane) this.getRoot();
        root2.setBackground(new Background(image));

        isHidden = true;

        // manipulate this if panel is visible in the menu
        setLayoutX(-600);
        setLayoutY(250);
    }

    public void moveSubscene() { // method for moving subscene for animation purposes
        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.seconds(0.3));
        transition.setNode(this); // this refers to the subscene to be move
        if(isHidden) {
            transition.setToX(630);
            isHidden = false;
        } else {
            transition.setToX(-500);
            isHidden = true;
        }
        transition.play();
    }

    public AnchorPane getPane() { // getter for root
        return (AnchorPane) this.getRoot();
    }
}
