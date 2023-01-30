package game;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ViewManager {
    private AnchorPane mainPane;
    private Scene mainScene;
    private Stage mainStage;
    private static final int WIDTH = 800;
    private static final int HEIGHT = 800;
    private final static int BUTTON_START_X = (WIDTH/2) - (190/2); // 190 is the width of the button
    private final static int BUTTON_START_Y = 250;
    private final static int LOGO_SIZE = 200;
    private final static int LOGO_X = (WIDTH/2) - (LOGO_SIZE/2);
    private final static int LOGO_Y = 10;
    private static final int FONT_SIZE = 25;
    private GameSubscene aboutSubscene;
    private GameSubscene instructionsSubscene;
    private GameSubscene resultsSubscene;
    private GameSubscene sceneToHide;
    List<GameButton> menuButtons;
    GameButton startButton;
    private Label foodEatenLabel, blobsEatenLabel, currentSizeLabel, timeAliveLabel;
    private Text foodEatenValue, blobsEatenValue, currentSizeValue, timeAliveValue;
    private HBox foodEatenLayout, blobsEatenLayout, currentSizeLayout, timeAliveLayout;


    public ViewManager() {
        menuButtons = new ArrayList<>(); // store the buttons created
        mainPane = new AnchorPane(); // main container that holds the elements in the user interface
        mainScene = new Scene(mainPane, WIDTH, HEIGHT); // scene graph of the application
        mainStage = new Stage(); // top level container where mainScene object as its scene
        mainStage.setScene(mainScene);
        createSubscenes(); // create subscenes for the event of different buttons
        createButton(); // create buttons to add in the user interface
        createBackground(); // setup the background of the application
        createLogo(); // set the logo
    }

    private void showSubscene(GameSubscene subscene) { // method to show and hide the subscenes
        if(sceneToHide != null) {
            sceneToHide.moveSubscene();
        }
        subscene.moveSubscene();
        sceneToHide = subscene;
    }

    private void createSubscenes() { // method for creating subscenes
        createAboutSubscene();
        createInstructionsScene();
        createResultsScene();
    }

    public void highlightText(String text, int start, int end, VBox root) { // method to highlight text, used in the instructions subscene
        Text t1 = new Text(text.substring(0, start));
        t1.setFont(Font.font("Courier New", FontWeight.BOLD, 19));

        Text t2 = new Text(text.substring(start, end));
        t2.setFill(Color.WHITE);
        addDropShadow(t2, 0.5);
        t2.setFont(Font.font("Courier New", FontWeight.BOLD, 19));

        Text t3 = new Text(text.substring(end));
        t3.setFont(Font.font("Courier New", FontWeight.BOLD, 19));

        TextFlow textFlow = new TextFlow(t1, t2, t3);
        Label label = new Label();

        label.setWrapText(true);
        label.setGraphic(textFlow);
        root.getChildren().add(label);
    }

    public void createResultsScene(){ // create the results subscene where summary of game stats are located
        resultsSubscene = new GameSubscene();
        mainPane.getChildren().add(resultsSubscene);

        VBox layout = new VBox();

        foodEatenLabel = new Label("Food Eaten: \t");
        formatLabel(foodEatenLabel, layout, FONT_SIZE);
        
        foodEatenValue = new Text(Integer.toString(GameTimer.food));
        foodEatenValue.setStyle("-fx-font-weight: bold");
        foodEatenValue.setFont(new Font("Bookman Old Style", FONT_SIZE));

        foodEatenLayout = new HBox();
        foodEatenLayout.getChildren().addAll(foodEatenLabel, foodEatenValue);


        blobsEatenLabel = new Label("Blobs Eaten: \t");
        formatLabel(blobsEatenLabel, layout, FONT_SIZE);

        blobsEatenValue = new Text(Integer.toString(GameTimer.blob));
        blobsEatenValue.setStyle("-fx-font-weight: bold");
        blobsEatenValue.setFont(new Font("Bookman Old Style", FONT_SIZE));

        blobsEatenLayout = new HBox();
        blobsEatenLayout.getChildren().addAll(blobsEatenLabel, blobsEatenValue);

        currentSizeLabel = new Label("Size: \t\t");
        formatLabel(currentSizeLabel, layout, FONT_SIZE);

        if(GameTimer.reset) {
            currentSizeValue = new Text(Integer.toString(GameTimer.size));
        } else {
            currentSizeValue = new Text("0");
        }
        currentSizeValue.setStyle("-fx-font-weight: bold");
        currentSizeValue.setFont(new Font("Bookman Old Style", FONT_SIZE));

        currentSizeLayout = new HBox();
        currentSizeLayout.getChildren().addAll(currentSizeLabel, currentSizeValue);

        timeAliveLabel = new Label("Time Alive: \t");
        timeAliveLabel.setFont(Font.font("Courier New", FontWeight.BOLD, FONT_SIZE));
        timeAliveLabel.setTextFill(Color.YELLOWGREEN);
        formatLabel(timeAliveLabel, layout, FONT_SIZE);

        timeAliveValue = new Text(String.format("%02d:%02d:%02d", GameTimer.elapsedMinutes, GameTimer.seconds, GameTimer.milliseconds));
        timeAliveValue.setStyle("-fx-font-weight: bold");
        timeAliveValue.setFont(new Font("Bookman Old Style", FONT_SIZE));
        timeAliveLayout = new HBox();
        timeAliveLayout.getChildren().addAll(timeAliveLabel, timeAliveValue);


        if(GameTimer.reset) {
            
            Text congratulations = new Text("Congratulations!\n");
            congratulations.setFont(Font.font("Bookman Old Style", FontWeight.BOLD, FONT_SIZE + 10));
            congratulations.setFill(Color.YELLOWGREEN);
            congratulations.setTranslateX(-30);

            DropShadow dropShadow = new DropShadow();
            dropShadow.setOffsetX(2);
            dropShadow.setOffsetY(2);
            dropShadow.setColor(Color.BLACK);
            congratulations.setEffect(dropShadow); 
            layout.getChildren().add(congratulations);
        } else {
            Text play = new Text("You have not yet played a game!");
            play.setFont(Font.font("Bookman Old Style", FontWeight.BOLD, FONT_SIZE + 10));
            play.setWrappingWidth(400);
            play.setTextAlignment(TextAlignment.CENTER);
            play.setFill(Color.YELLOWGREEN);
            play.setTranslateX(-30);
            play.setTranslateY(-20);

            DropShadow dropShadow = new DropShadow();
            dropShadow.setOffsetX(2);
            dropShadow.setOffsetY(2);
            dropShadow.setColor(Color.BLACK);
            play.setEffect(dropShadow);  
            layout.getChildren().add(play);
        }
    
        layout.getChildren().addAll(foodEatenLayout, blobsEatenLayout, currentSizeLayout, timeAliveLayout);
        layout.setSpacing(20);
        layout.setAlignment(Pos.CENTER);

        resultsSubscene.getPane();
        AnchorPane.setLeftAnchor(layout, 70.0);
        aboutSubscene.getPane();
        AnchorPane.setRightAnchor(layout, 30.0);
        aboutSubscene.getPane();
        AnchorPane.setTopAnchor(layout, 0.0);
        aboutSubscene.getPane();
        AnchorPane.setBottomAnchor(layout, 0.0);

        resultsSubscene.getPane().getChildren().add(layout);
    }   

    public void createInstructionsScene() { // method to create the instructions subscene where instructions to the game is located
        instructionsSubscene = new GameSubscene();
        mainPane.getChildren().add(instructionsSubscene);

        VBox root = new VBox();
        highlightText("1. Use the keys W, A, S, and D to move\n   your player fish around the play\n   area.", 16, 30, root);
        highlightText("2. Eat pellets and smaller fishes to\n   grow larger.", 7, 34, root);
        highlightText("3. Avoid larger fishes, as they will\n   eat you.", 3, 22, root);
        highlightText("4. Collect power-ups to gain temporary\n   advantages in the game.", 3, 20, root);
        highlightText("       The speed boost potion will\n       double your movement speed\n       temporarily.", 34, 87, root);
        highlightText("       The immune potion will make you\n       temporarily invulnerable to\n       being eaten by bigger fishes.", 30, 70, root);
        highlightText("5. Try to survive as long as\n   possible.", 10, 40, root);
        
        root.setAlignment(Pos.CENTER_LEFT);
        root.setSpacing(5);
        
        aboutSubscene.getPane();
        AnchorPane.setLeftAnchor(root, 30.0);
        aboutSubscene.getPane();
        AnchorPane.setRightAnchor(root, 30.0);
        aboutSubscene.getPane();
        AnchorPane.setTopAnchor(root, 0.0);
        aboutSubscene.getPane();
        AnchorPane.setBottomAnchor(root, 0.0);

        Image speedBoost = new Image("assets/speedPotion.png");
        Image immunity = new Image("assets/immunityPotion.png");

        // render the images
        ImageView speedBoostView = new ImageView(speedBoost);
        ImageView immunityView = new ImageView(immunity);

        speedBoostView.setFitHeight(30);
        speedBoostView.setFitWidth(30);

        immunityView.setFitHeight(30);
        immunityView.setFitWidth(30);

        // set the x and y value
        speedBoostView.setX(60);
        speedBoostView.setY(260);

        immunityView.setX(60);
        immunityView.setY(322);

        instructionsSubscene.getPane().getChildren().addAll(root, speedBoostView, immunityView);
    }

    public void formatLabel(Label label, VBox root, int size){ // method to format the labels for ui purposes
        label.setTextAlignment(TextAlignment.CENTER);
        label.setFont(Font.font("Bookman Old Style", FontWeight.BOLD, 27));
        label.setTextFill(Color.WHITE);
        root.getChildren().add(label);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(3.0);
        dropShadow.setOffsetY(3.0);
        dropShadow.setColor(Color.BLACK);
        label.setEffect(dropShadow);
    }

    public void formatText(Text text, Text textInfo, VBox root){ // method to format texts for ui purposes
        text.setTextAlignment(TextAlignment.CENTER);
        textInfo.setTextAlignment(TextAlignment.CENTER);

        text.setFont(Font.font("Bookman Old Style", FontWeight.BOLD, 21));
        text.setStroke(Color.BLACK);
        text.setStrokeWidth(0.2);
        text.setFill(Color.BLACK);
        textInfo.setFont(Font.font("Courier New",FontPosture.ITALIC, 15));

        root.getChildren().addAll(text, textInfo);
    }

    public void addDropShadow(Text text, double stroke){ // method to add dropshadow in a text for ui purposes
        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(stroke);
        dropShadow.setOffsetY(stroke);
        dropShadow.setColor(Color.BLACK);
        text.setEffect(dropShadow);
    }

    public void createAboutSubscene(){ // method to create the about subscenes where info about the developer as well as the assets used are located
        aboutSubscene = new GameSubscene();
        mainPane.getChildren().add(aboutSubscene);

        VBox root = new VBox();

        Label developerLabel = new Label("Developer");

        formatLabel(developerLabel, root, FONT_SIZE + 2);

        Text developerName = new Text("Reymond P. Yncierto");
        Text developerInfo = new Text("BS Computer Science\nUniversity of the Philippines Los Baños");
        Text credits1 = new Text("takeCert (YT Channel)");
        Text credits1Info = new Text("Main Menu UI Inspiration");
        Text credits2 = new Text("resyntax.itch.io");
        Text credits2Info = new Text("Game Assets");
        Text credits3 = new Text("Hungry Shark");
        Text credits3Info = new Text("Game Inspiration");
        Text credits4 = new Text("CMSC 22 Instructors and Codebase");
        Text credits4Info = new Text("Knowledge and Template");
        Text credits5 = new Text("Stack Overflow");
        Text credits5Info = new Text("Debugging");
        Text message = new Text("If you have any questions, please contact the\ndeveloper at rpyncierto@up.edu.ph.");

        formatText(developerName, developerInfo, root);

        Label creditsLabel = new Label("Credits");
        formatLabel(creditsLabel, root, FONT_SIZE + 2);

        formatText(credits1, credits1Info, root);
        formatText(credits2, credits2Info, root);
        formatText(credits3, credits3Info, root);
        formatText(credits4, credits4Info, root);
        formatText(credits5, credits5Info, root);

        message.setTextAlignment(TextAlignment.CENTER);
        message.setFont(Font.font("Bookman Old Style", FontWeight.BOLD, 15));
        message.setFill(Color.WHITE);
        addDropShadow(message, 2);
        root.getChildren().add(message);

        int move = 5;

        // format the x and y value of the objects in the vbox
        developerLabel.setTranslateY(-move*5);
        developerName.setTranslateY(-move*4);
        developerInfo.setTranslateY(-move*4 - 2);
        creditsLabel.setTranslateY(-move*3);
        credits1.setTranslateY(-move*2);
        credits1Info.setTranslateY(-move*2 - 2);
        credits2.setTranslateY(-move);
        credits2Info.setTranslateY(-move - 2);
        credits3Info.setTranslateY(-2);
        credits4.setTranslateY(move);
        credits4Info.setTranslateY(move - 2);
        credits5.setTranslateY(move*2);
        credits5Info.setTranslateY(move*2 - 2);
        message.setTranslateY(move*4);

        root.setAlignment(Pos.CENTER);

        aboutSubscene.getPane();
        AnchorPane.setLeftAnchor(root, 0.0);
        aboutSubscene.getPane();
        AnchorPane.setRightAnchor(root, 0.0);
        aboutSubscene.getPane();
        AnchorPane.setTopAnchor(root, 0.0);
        aboutSubscene.getPane();
        AnchorPane.setBottomAnchor(root, 0.0);

        aboutSubscene.getPane().getChildren().add(root);
    }

    public Stage getMainStage() { // method to return mainStage to the main app
        return mainStage;
    }

    private void addButton(GameButton button) { // method to add the buttons to the mainPane
        button.setLayoutX(BUTTON_START_X);
        button.setLayoutY(BUTTON_START_Y + menuButtons.size() * 100);
        mainPane.getChildren().add(button);
        menuButtons.add(button);
    }

    private void createButton() { // method to create the buttons
        createStartButton();
        instructionsButton();
        aboutButton();
        resultsButton();
        createExitButton();
    }

    private void moveButtons() { // method to move the buttons
        for(GameButton button : menuButtons) {
            button.moveButton();
        }
    }

    void createStartButton() { // method to create the start button
        startButton = new GameButton("Play");
        addButton(startButton);

        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GameTimer gameTimer = new GameTimer(mainStage);
                gameTimer.start();
            }
        });
    }

    private void createExitButton() { // method to create the exit button
        GameButton exitButton = new GameButton("Exit");
        addButton(exitButton);

        exitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mainStage.close();
            }
        });
    }

    private void aboutButton() { // method to create the about button
        GameButton aboutButton = new GameButton("About");
        addButton(aboutButton);

        aboutButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showSubscene(aboutSubscene);
                moveButtons();
            }
        });
    }

    private void instructionsButton() { // method to create the instructions button
        GameButton instructionsButton = new GameButton("Instructions");
        addButton(instructionsButton);

        instructionsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showSubscene(instructionsSubscene);
                moveButtons();
            }
        });
    }

    private void resultsButton() { // method to create the results button
        GameButton resultsButton = new GameButton("Results");
        addButton(resultsButton);

        resultsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showSubscene(resultsSubscene);
                moveButtons();
            }
        });
    }

    private void createBackground() { // method to create the background of the application
        Image backgroundImage1 = new Image("assets/bgA.png", WIDTH, HEIGHT, false, true);
        BackgroundImage background1 = new BackgroundImage(backgroundImage1, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, null);
        Image backgroundImage2 = new Image("assets/bgB.png", WIDTH, HEIGHT, false, true);
        BackgroundImage background2 = new BackgroundImage(backgroundImage2, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, null);
        ArrayList<BackgroundImage> backgroundImages = new ArrayList<>();
        backgroundImages.add(background1);
        backgroundImages.add(background2);

        Timeline timeline = new Timeline( // for the background animation
        new KeyFrame(Duration.seconds(0), e -> mainPane.setBackground(new Background(background1))),
        new KeyFrame(Duration.seconds(0.5), e -> mainPane.setBackground(new Background(background2))),
        new KeyFrame(Duration.seconds(1), e -> mainPane.setBackground(new Background(background1)))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void createLogo() { // method to set the logo of the application
        ImageView logo = new ImageView("assets/logo.png");
        logo.setFitHeight(LOGO_SIZE);
        logo.setFitWidth(LOGO_SIZE);
        logo.setLayoutX(LOGO_X);
        logo.setLayoutY(LOGO_Y);
        logo.setEffect(new DropShadow());
        mainPane.getChildren().add(logo);
    }
}
