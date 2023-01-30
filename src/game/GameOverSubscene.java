package game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;
import main.App;

public class GameOverSubscene extends SubScene {
    static final int WIDTH = 600;
    static final int HEIGHT = 400;
    private Label foodEatenLabel, blobsEatenLabel, currentSizeLabel, timeAliveLabel;
    private Text foodEatenValue, blobsEatenValue, currentSizeValue, timeAliveValue;
    private HBox foodEatenLayout, blobsEatenLayout, currentSizeLayout, timeAliveLayout;
    private final int FONT_SIZE = 25;
    private Group gameOverRoot;

    public GameOverSubscene() { // constructor
        super(new Group(), WIDTH, HEIGHT);
    }

    public Group displaySubScene() { // method for displaying the stats of the game

        foodEatenLabel = new Label("Food Eaten: \t");
        foodEatenLabel.setTextFill(Color.WHITE);
        foodEatenLabel.setFont(Font.font("Courier New", FontWeight.BOLD, FONT_SIZE));
        
        foodEatenValue = new Text(Integer.toString(GameTimer.player.getFoodEaten()));
        foodEatenValue.setStyle("-fx-font-weight: bold");
        foodEatenValue.setFont(new Font("Bookman Old Style", FONT_SIZE));
        foodEatenValue.setFill(Color.YELLOW);

        foodEatenLayout = new HBox();
        foodEatenLayout.getChildren().addAll(foodEatenLabel, foodEatenValue);

        blobsEatenLabel = new Label("Blobs Eaten: \t");
        blobsEatenLabel.setTextFill(Color.WHITE);
        blobsEatenLabel.setFont(Font.font("Courier New", FontWeight.BOLD, FONT_SIZE));

        blobsEatenValue = new Text(Integer.toString(GameTimer.player.getBlobsEaten()));
        blobsEatenValue.setStyle("-fx-font-weight: bold");
        blobsEatenValue.setFont(new Font("Bookman Old Style", FONT_SIZE));
        blobsEatenValue.setFill(Color.YELLOW);

        blobsEatenLayout = new HBox();
        blobsEatenLayout.getChildren().addAll(blobsEatenLabel, blobsEatenValue);

        currentSizeLabel = new Label("Current Size: \t");
        currentSizeLabel.setFont(Font.font("Courier New", FontWeight.BOLD, FONT_SIZE));
        currentSizeLabel.setTextFill(Color.WHITE);
        currentSizeValue = new Text(Integer.toString((int) GameTimer.player.getSize()));
        currentSizeValue.setStyle("-fx-font-weight: bold");
        currentSizeValue.setFont(new Font("Bookman Old Style", FONT_SIZE));
        currentSizeValue.setFill(Color.YELLOW);

        currentSizeLayout = new HBox();
        currentSizeLayout.getChildren().addAll(currentSizeLabel, currentSizeValue);

        timeAliveLabel = new Label("Time Alive: \t");
        timeAliveLabel.setFont(Font.font("Courier New", FontWeight.BOLD, FONT_SIZE));
        timeAliveLabel.setTextFill(Color.WHITE);

        timeAliveValue = new Text(String.format("%02d:%02d:%02d", GameTimer.elapsedMinutes, GameTimer.seconds, GameTimer.milliseconds));
        timeAliveValue.setStyle("-fx-font-weight: bold");
        timeAliveValue.setFont(new Font("Bookman Old Style", FONT_SIZE));
        timeAliveValue.setFill(Color.YELLOW);

        timeAliveLayout = new HBox();
        timeAliveLayout.getChildren().addAll(timeAliveLabel, timeAliveValue);

        Text gameOverText = new Text(" Game Over");
        gameOverText.setFont(Font.font("Courier New", FontWeight.BOLD, 50));
        gameOverText.setFill(Color.RED);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(1);
        dropShadow.setOffsetY(1);
        dropShadow.setColor(Color.BLACK);
        gameOverText.setEffect(dropShadow);  

        // Add the stats layouts to a VBox layout
        VBox layout = new VBox();
        layout.getChildren().addAll(gameOverText, foodEatenLayout, blobsEatenLayout, currentSizeLayout, timeAliveLayout);
        layout.setSpacing(10);
        layout.setPadding(new Insets(0, 0, 100, 0));
        
        // Add the VBox layout to the scene root
        gameOverRoot = (Group) getRoot();
        gameOverRoot.getChildren().add(layout);

        layout.setOpacity(0);

        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(2), layout);

        // Set the from and to values for the opacity
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);

        // Play the animation
        fadeTransition.play();

        GameTimer.reset = true;

        return gameOverRoot;
    }

    public HBox createButton(Stage stage, Group root, ArrayList<Consumable> consumables, ObservableList<Enemy> enemies, Player player, Map<Enemy, Pair<AnimationTimer, Timeline>> enemyTimers, HashMap<Consumable, AnimationTimer> consumableTimers) { // creates the menu button
        
        // Create an HBox layout to hold the button
        HBox buttonLayout = new HBox(); 
        buttonLayout.setAlignment(Pos.CENTER); // Center the buttons
        buttonLayout.setPadding(new Insets(240, 0, 0, 0));

        GameButton menu = new GameButton("Menu"); // create the button
        menu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                stage.close(); // close the current stage

                // clear all the timers, children of roots and so on
                for (Map.Entry<Consumable, AnimationTimer> entry : consumableTimers.entrySet()) {
                    AnimationTimer timer = entry.getValue();
                    timer.stop();
                } 
                
                player.getAnimation().stop();
                for (Map.Entry<Enemy, Pair<AnimationTimer, Timeline>> entry : enemyTimers.entrySet()) {
                    entry.getValue().getKey().stop();
                    entry.getValue().getValue().stop();
                }

                for(Enemy enemy : enemies) {
                    enemy.getAnimation().stop();
                }

                root.getChildren().clear();
                consumables.clear();
                Stage newStage = new Stage();
                root.getChildren().clear();
                enemies.clear();

                GameTimer.createPlayer(); // create player
                GameTimer.reset = true; // to update the game results subscene

                App app = new App(); // create instance of the app
                app.start(newStage);
            }
        });

        // Add the buttons to the HBox layout
        buttonLayout.getChildren().add(menu);
        buttonLayout.setOpacity(0);

        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(2), buttonLayout);

        // Set the from and to values for the opacity
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        // Play the animation
        fadeTransition.play();

        return buttonLayout;
    }
}




