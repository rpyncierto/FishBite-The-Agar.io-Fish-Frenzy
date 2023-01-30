package game;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.animation.TranslateTransition;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class GameButton extends Button {
    
    private final String FONT = "src/assets/MachineGunk.ttf";
    private final String BUTTON_PRESSED_STYLE = "-fx-background-color: transparent; -fx-background-image: url('assets/pressedButton.png');";
    private final String DEFAULT_PRESSED_STYLE = "-fx-background-color: transparent; -fx-background-image: url('assets/defaultButton.png');";

    public GameButton(String text) { // constructor
        setText(text);
        setButtonFont();
        setPrefWidth(190);
        setPrefHeight(49);
        setStyle(DEFAULT_PRESSED_STYLE);
        initializeButtonListeners();
    }

    private void setButtonFont() { // method to set the font of the button
        try {
            setFont(Font.loadFont(new FileInputStream(FONT), 25));
        } catch (FileNotFoundException e) { // if the font is not found, use the verdana font
            setFont(Font.font("Verdana", 20));
        }
    }

    private void setButtonPressedStyle() { // method to set the animation of the button
        setStyle(BUTTON_PRESSED_STYLE);
        setPrefHeight(49);
        setLayoutY(getLayoutY() + 4); // move the button down 4 pixels to give the illusion of a pressed button
    }

    private void setButtonReleasedStyle() { // method to set the animation of the button
        setStyle(DEFAULT_PRESSED_STYLE);
        setPrefHeight(45);
        setLayoutY(getLayoutY() - 4);
    }

    private void initializeButtonListeners() { // create listeners
        setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getButton().equals(MouseButton.PRIMARY)) {
                    setButtonPressedStyle();
                }
            }
        });

        setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getButton().equals(MouseButton.PRIMARY)) {
                    setButtonReleasedStyle();
                }
            }
        });

        setOnMouseEntered(new EventHandler<MouseEvent>() { // when the mouse enters the button, create a drop shadow effect
            @Override
            public void handle(MouseEvent event) {
                setEffect(new DropShadow());
            }
        });

        setOnMouseExited(new EventHandler<MouseEvent>() { // when the mouse exits the button, remove the drop shadow effect
            @Override
            public void handle(MouseEvent event) {
                setEffect(null);
            }
        });
    }

    public void moveButton() { // method to move the button for animation purposes
        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.seconds(0.3));
        transition.setNode(this); // this refers to the button to be move
        transition.setToX(260);
        transition.play();
    }
}
