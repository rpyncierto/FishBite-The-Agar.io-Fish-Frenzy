package game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;

public class GameTimer extends AnimationTimer{

    private static final int WIDTH = 800, HEIGHT = 800;
    private int currentImageIndex = 0;
    private long elapsedTime, elapsedSeconds,startTime;
	static long elapsedMinutes, seconds, milliseconds;
    private Scene scene;
    private VBox statsLayout;
    private Label foodEatenLabel, blobsEatenLabel, currentSizeLabel, timeAliveLabel;
    private Text foodEatenValue, blobsEatenValue, currentSizeValue, timeAliveValue;
    private HBox foodEatenLayout, blobsEatenLayout, currentSizeLayout, timeAliveLayout;
    private static Random RANDOM;
    private ImageView imageView;
    private StackPane stackPane;
    private Rectangle clip;
    static Player player = new Player(); 
    private Group foodGroup, enemyGroup, root;
    private Enemy enemy;
    private Immunity immunityPotion;
    private SpeedBoost speedBoostPotion;
    private static final Image[] BG_IMAGES = {new Image("assets/bgA.png"), new Image("assets/bgB.png")};
    private Timeline bgTimeline, animationTimeline;
    private ArrayList<Consumable> consumables;
    private ObservableList<Enemy> enemies;
    private Map<Enemy, Pair<AnimationTimer, Timeline>> enemyTimers;
    private HashMap<Consumable, AnimationTimer> consumableTimers;
    public static final int MAP_HEIGHT = 2400, MAP_WIDTH = 2400;
    private Stage stage;
    private double newX, newY, gameOverX, gameOverY;
    private GameOverSubscene gameOverSubscene;
    static boolean reset = false;
    static int food, blob, size;


    public GameTimer(Stage mainStage) { // constructor
        RANDOM = new Random();
        clip = new Rectangle(0, 0, 800, 800);
        immunityPotion = new Immunity();
        speedBoostPotion = new SpeedBoost();
        consumables = new ArrayList<>();
        enemies = FXCollections.observableArrayList();
        enemyTimers = new HashMap<>();
        consumableTimers = new HashMap<>();
        gameOverSubscene = new GameOverSubscene();
        stage = mainStage;
        startTime = System.nanoTime();
        setupGame(stage);
        spawnSprites();
        animateSprites();
        moveEnemy();
	}

	@Override
    public void handle(long now) { // call continously

        // get the x and y coordinate of the middle of the player
        double x = (player.getX() + player.getSize()/2);
        double y = (player.getY() + player.getSize()/2);

        // setup for the panning
        newX = (x-400);
        newY = (y-400);

        // actual panning of the map
        if(newX >= 0 && newX <= MAP_WIDTH-WIDTH) { 
            root.setTranslateX(-newX);
            statsLayout.setTranslateX(newX);
            gameOverX = (x - gameOverSubscene.getWidth()/2);
        }

        if(newY >= 0 && newY <= MAP_HEIGHT-HEIGHT) {
            root.setTranslateY(-newY);
            statsLayout.setTranslateY(newY);
            gameOverY = (y - gameOverSubscene.getHeight()/2);
        }

        // animate if there is a food on the front of the player
        if(player.getFoodAhead()) {
            animationTimeline.stop();
            player.updateImage();
            animationTimeline.play();
        }

        // compute for the timer
        elapsedTime = now - startTime;
        elapsedSeconds = elapsedTime / 1000000000;
        elapsedMinutes = elapsedSeconds / 60;
        seconds = elapsedSeconds % 60;
        milliseconds = (elapsedTime / 1000000) % 1000;

        createListener(); // listener for the user input

        // to avoid error while since we are modifying the foodcells and enemy directly
        ArrayList<FoodCell> cellsToAdd = new ArrayList<FoodCell>();
        ArrayList<FoodCell> cellsToRemove = new ArrayList<FoodCell>();
        ArrayList<ImageView> enemyToAdd = new ArrayList<ImageView>();
        ArrayList<ImageView> enemyToRemove = new ArrayList<ImageView>();
        List<Enemy> toRemove = new ArrayList<>(); // list to keep track of enemies to remove
        List<Enemy> toAdd = new ArrayList<>(); // list to keep track of enemies to add

        Iterator<Consumable> iterator = consumables.iterator(); // iterator for the consumables
        while(iterator.hasNext()) {
            Consumable consumable = iterator.next();
            if (player.getSprite().getBoundsInParent().intersects(consumable.getSprite().getBoundsInParent())) { // check if a consumable collides with the player
                System.out.println("Player eats consumable!");
                consumable.setConsumed(true);
                player.setFoodAhead(true);
            }
        }
        
        for (Node foodCell : foodGroup.getChildren()) {
            FoodCell food = (FoodCell) foodCell;

            if (player.getSprite().getBoundsInParent().intersects(food.getBoundsInParent())) { // check if a food collides with the player
                System.out.println("Player eats food!");
                player.setFoodAhead(true);
                player.grow((double) food.getSize());
                food.setEaten(true);
                player.increaseFoodEaten();
            }

            for (Enemy enemy : enemies) {
                if (enemy.getSprite().getBoundsInParent().intersects(food.getBoundsInParent())) { // checks if a food collides with an enemy
                    System.out.println("Enemy eats food!");
                    enemy.setFoodAhead(true);
                    enemy.grow((double) food.getSize());
                    food.setEaten(true);
                }
            }

            if(food.isEaten()) { // if food is eaten, update the foodGroup
                cellsToAdd.add(new FoodCell());
                cellsToRemove.add(food);
            }

        }
        foodGroup.getChildren().removeAll(cellsToRemove);
        foodGroup.getChildren().addAll(cellsToAdd);

        for (Enemy enemy : enemies) {
            if (enemy.getSprite().getBoundsInParent().intersects(player.getSprite().getBoundsInParent())) { // check if enemy collides with the player
                if (enemy.getWidth() > player.getWidth()) { // if enemy is bigger than the player
                    if(!player.isImmune()) {
                        System.out.println("Player has been eaten! GAME OVER....");
                        stop(); // stop the animationTimer
                        
                        player.setEaten(true);

                        // create a rectangle object in the scene
                        Rectangle rect = new Rectangle(0, 0, GameOverSubscene.WIDTH, GameOverSubscene.HEIGHT);
                        rect.setFill(Color.BLACK);
                        rect.setOpacity(0.3);

                        // animation of the stats layout and the rectangle
                        Timeline timeline = new Timeline( 
                            new KeyFrame(Duration.seconds(0), new KeyValue(statsLayout.opacityProperty(), 1)),
                            new KeyFrame(Duration.seconds(1.5), new KeyValue(statsLayout.opacityProperty(), 0)),
                            new KeyFrame(Duration.seconds(0), new KeyValue(rect.opacityProperty(), 0)),
                            new KeyFrame(Duration.seconds(2), new KeyValue(rect.opacityProperty(), 0.2))

                        );
                        timeline.setOnFinished(event -> root.getChildren().remove(statsLayout));
                        timeline.play();

                        // stackpane to hold the gameOverScene
                        StackPane stackPane = new StackPane( rect, gameOverSubscene.displaySubScene(), gameOverSubscene.createButton(stage, root, consumables, enemies, player, enemyTimers, consumableTimers));
                        stackPane.setLayoutX(gameOverX);
                        stackPane.setLayoutY(gameOverY);
                        root.getChildren().add(stackPane);

                    } else {
                        System.out.println("Player is immune!");
                    }
                } else if (enemy.getWidth() < player.getWidth()) { // if player is bigger than the enemy
                    System.out.println("Player eats an enemy!");
                    player.setFoodAhead(true);
                    player.grow(enemy.getSize());
                    enemy.setEaten(true);
                    player.increaseBlobsEaten();
                } else {}

                if(enemy.isEaten()) { //  if enemy is eaten, update the enemies arrayList and enemyGroup
                    Enemy newEnemy = new Enemy(RANDOM.nextInt(Enemy.getImagesLength()));
                    enemies.remove(enemy);
                    enemyToRemove.add(enemy.getSprite());
                    enemies.add(newEnemy);
                    enemyToAdd.add(newEnemy.getSprite());
                }
                if(player.isEaten()) { // if player is eaten, update the root
                    root.getChildren().remove(player.getSprite());
                }
                break; // if may collision break kasi nakain na siya so impossible na makain siya uli 
            }
        }

        for (int i = 0; i < enemies.size(); i++) {
            for (int j = i + 1; j < enemies.size(); j++) {
                Enemy enemy1 = enemies.get(i);
                Enemy enemy2 = enemies.get(j);
                if (enemy1.getSprite().getBoundsInParent().intersects(enemy2.getSprite().getBoundsInParent())) { // check if 2 enemies collide
                    // collision detected
                    if (enemy1.getWidth() > enemy2.getWidth()) { // if first enemey is bigger than second enemy
                        Enemy newEnemy = new Enemy(RANDOM.nextInt(Enemy.getImagesLength()));
                        enemy1.grow(enemy2.getWidth());
                        toRemove.add(enemy2);
                        toAdd.add(newEnemy);
                        enemyToRemove.add(enemy2.getSprite());
                        enemyToAdd.add(newEnemy.getSprite());
                    } else if (enemy1.getWidth() < enemy2.getWidth()) {  // if second enemy is bigger than the first enemy
                        Enemy newEnemy = new Enemy(RANDOM.nextInt(Enemy.getImagesLength()));
                        enemy2.grow(enemy1.getWidth());
                        toRemove.add(enemy1);
                        toAdd.add(newEnemy);
                        enemyToRemove.add(enemy1.getSprite());
                        enemyToAdd.add(newEnemy.getSprite());
                    } else {}
                }
            }
        }
        enemies.removeAll(toRemove); // remove collided enemies
        enemies.addAll(toAdd); // add new enemies
        enemyGroup.getChildren().addAll(enemyToAdd);
        enemyGroup.getChildren().removeAll(enemyToRemove);

        updateScoreBoard(); // updates the score
    }

    public void createEnemyTimer(Enemy enemy) { // method for the animation of the enemy movement
        AnimationTimer timer = new AnimationTimer() {
            int direction;
            @Override
            public void start() {
                // Reset the direction variable every time the timer is started
                direction = (int) (Math.random() * 8);
                super.start();
            }
            @Override
            public void handle(long now) {
                enemy.moveRandomly(direction);
            }
        };
        
        Timeline resetTimer = new Timeline(
            new KeyFrame(Duration.seconds((int) (Math.random() * 3 + 1)), event -> {
                timer.stop();
                timer.start();
            })
        );
        resetTimer.setCycleCount(Animation.INDEFINITE);
        enemyTimers.put(enemy, new Pair<>(timer, resetTimer));
        timer.start();
        resetTimer.play();
    }

    private void moveEnemy() { // method for enemy movement
        for (Enemy enemy : enemies) {
            createEnemyTimer(enemy);
        }

        // Add a listener to the list to be notified when it changes
        enemies.addListener(new ListChangeListener<Enemy>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends Enemy> c) {
                while (c.next()) {
                    if (c.wasAdded()) {
                        // An enemy was added to the list
                        for (Enemy enemy : c.getAddedSubList()) {
                            // Create a new AnimationTimer and Timeline for the enemy
                            createEnemyTimer(enemy);
                        }
                    } else if (c.wasRemoved()) {
                        // An enemy was removed from the list
                        // Stop and remove the AnimationTimer and Timeline for the enemy
                        for (Enemy enemy : c.getRemoved()) {
                            // Stop and remove the AnimationTimer and Timeline here...
                            Pair<AnimationTimer, Timeline> timers = enemyTimers.get(enemy);
                            if (timers != null) {
                                timers.getKey().stop();
                                timers.getValue().stop();
                                // Remove the AnimationTimer and Timeline from the map
                                enemyTimers.remove(enemy);
                            }
                        }
                    }
                }
            }
        });
    }

    private void setupGame(Stage stage) { // method to setup the game stage and scene

        // group to hold the elements
        root = new Group();


        // setup background (with animation)
        imageView = new ImageView(BG_IMAGES[currentImageIndex]);
        bgTimeline = new Timeline(new KeyFrame(Duration.seconds(0.75), e -> {
            imageView.setImage(BG_IMAGES[currentImageIndex]);
            currentImageIndex = (currentImageIndex + 1) % BG_IMAGES.length;
        }));
        bgTimeline.setCycleCount(Animation.INDEFINITE);
        bgTimeline.play();
        imageView.setFitWidth(MAP_WIDTH);
        imageView.setFitHeight(MAP_HEIGHT);
        imageView.setPreserveRatio(false);

        // to hold the background image
        stackPane = new StackPane();
        stackPane.getChildren().addAll(imageView);

        root.getChildren().add(stackPane);

        // setup score board
        foodEatenLabel = new Label("Food Eaten: \t");
        foodEatenLabel.setTextFill(Color.WHITE);
        foodEatenLabel.setFont(Font.font("Courier New", FontWeight.BOLD, 16));
        
        foodEatenValue = new Text("0");
        foodEatenValue.setStyle("-fx-font-weight: bold");
        foodEatenValue.setFill(Color.YELLOW);

        foodEatenLayout = new HBox();
        foodEatenLayout.getChildren().addAll(foodEatenLabel, foodEatenValue);

        blobsEatenLabel = new Label("Blobs Eaten: \t");
        blobsEatenLabel.setTextFill(Color.WHITE);
        blobsEatenLabel.setFont(Font.font("Courier New", FontWeight.BOLD, 16));
        blobsEatenValue = new Text("0");
        blobsEatenValue.setStyle("-fx-font-weight: bold");
        blobsEatenValue.setFill(Color.YELLOW);

        blobsEatenLayout = new HBox();
        blobsEatenLayout.getChildren().addAll(blobsEatenLabel, blobsEatenValue);

        currentSizeLabel = new Label("Current Size: \t");
        currentSizeLabel.setFont(Font.font("Courier New", FontWeight.BOLD, 16));
        currentSizeLabel.setTextFill(Color.WHITE);
        currentSizeValue = new Text("0");
        currentSizeValue.setStyle("-fx-font-weight: bold");
        currentSizeValue.setFill(Color.YELLOW);

        currentSizeLayout = new HBox();
        currentSizeLayout.getChildren().addAll(currentSizeLabel, currentSizeValue);

        timeAliveLabel = new Label("Time Alive: \t");
        timeAliveLabel.setFont(Font.font("Courier New", FontWeight.BOLD, 16));
        timeAliveLabel.setTextFill(Color.WHITE);
        timeAliveValue = new Text("0");
        timeAliveValue.setStyle("-fx-font-weight: bold");
        timeAliveValue.setFill(Color.YELLOW);
        timeAliveLayout = new HBox();
        timeAliveLayout.getChildren().addAll(timeAliveLabel, timeAliveValue);

        // Add the stats layouts to a VBox layout
        statsLayout = new VBox();
        statsLayout.getChildren().addAll(foodEatenLayout, blobsEatenLayout, currentSizeLayout, timeAliveLayout);

        // Setting the spacing between the elements in the layout.
        statsLayout.setSpacing(5);
        statsLayout.setPadding(new Insets(10, 10, 10, 10));
        statsLayout.setStyle("-fx-background-color: rgba(0, 0, 0, 0.2);");
        // Setting the layout for the stats panel.
        statsLayout.setLayoutX(10);
        statsLayout.setLayoutY(10);

        root.getChildren().add(statsLayout);
        
        // add the group to the scene
        scene = new Scene(root, MAP_WIDTH, MAP_HEIGHT);

        // holds the clop and content of the scene
        Group camera = new Group();

        // creates 800 by 800 clip
        // Set the clip as the clip for the camera
        camera.setClip(clip);
        // Add the content of the scene to the camera
        camera.getChildren().add(scene.getRoot());

        scene = new Scene(camera);

        // set the scene on the stage to camera
        stage.setScene(scene);

        // give the group focus so that it will receive the key events
        root.requestFocus();

        // show the stage
        stage.show();
    }

    private void updateScoreBoard() { // method for updating the score board
        food = player.getFoodEaten();
        foodEatenValue.setText(Integer.toString(food));
        blob = player.getBlobsEaten();
        blobsEatenValue.setText(Integer.toString(blob));
        size = (int) player.getSize();
        currentSizeValue.setText(Integer.toString(size));
        timeAliveValue.setText(String.format("%02d:%02d:%02d", elapsedMinutes, seconds, milliseconds)); 
    }

    private void createListener() { // method for the listeners
        root.setOnKeyPressed(event -> {
            player.update(event.getCode());
        });

        root.setOnKeyReleased(event -> {
            player.stop(event.getCode());
        });
        player.move();
    }

    private void spawnConsumables() { // method for spawning consumables

        // timer for the immunityPotion
        AnimationTimer immunityTimer = new AnimationTimer() {
            long startTime, elapsedTime, powerUpDuration = 5000, respawnDelay = 10000; // 5 and 10 seconds
            boolean immunityActive, immunityChecked, immunityTaken;
            FadeTransition ft;
        
            @Override
            public void start() {
                super.start();
                startTime = 0;
                elapsedTime = 0;
                immunityActive = false;
                immunityTaken = false;
                immunityChecked = false;
                immunityPotion = new Immunity();
                immunityPotion.setConsumed(false);
                immunityPotion.getX();
                immunityPotion.getY();
                root.getChildren().add(immunityPotion.getSprite());
                consumables.add(immunityPotion);
                startTime = System.currentTimeMillis();
            }
        
            @Override
            public void handle(long now) {
                elapsedTime = System.currentTimeMillis() - startTime;

                    if(!immunityChecked) { // to make sure that this will only executed once
                        if (immunityPotion.isConsumed()) { // potion is consumed
                            System.out.println("Immunity taken");
                            player.getSprite().setOpacity(0.5);

                            // animation
                            ft = new FadeTransition(Duration.seconds(1), player.getSprite());
                            ft.setFromValue(1.0);
                            ft.setToValue(0.5);
                            ft.play();

                            root.getChildren().remove(immunityPotion.getSprite());
                            consumables.remove(immunityPotion);
                            startTime = System.currentTimeMillis();
                            player.setImmune(true);
                            immunityActive = true;
                            immunityChecked = true;
                        }
                    }
            
                    if (immunityActive && elapsedTime >= powerUpDuration) { // potion duration is up
                        System.out.println("Immunity ended");
                        
                        // remove animation
                        ft.setFromValue(0.5);
                        ft.setToValue(1.0);
                        ft.play();
                        player.getSprite().setEffect(null);

                        player.setImmune(false);
                        immunityPotion.setConsumed(false);
                        immunityActive = false;
                        immunityTaken = true;
                    }
                        
    
                    if(immunityTaken) { // respawn the potion
                        if (elapsedTime >= (powerUpDuration + respawnDelay)) {
                            System.out.println("Immunity Potion Respawning");
                            this.start();
                        }
                    }
                
                    if (!immunityPotion.isConsumed() && elapsedTime >= powerUpDuration) { // if the potion is not consumed and the time is up
                        consumables.remove(immunityPotion);
                        immunityPotion.setConsumed(false);
                        root.getChildren().remove(immunityPotion.getSprite());
                        if (elapsedTime >= (5000 + respawnDelay)) {
                            this.start();
                        }
                    }
            }
        };
        consumableTimers.put(immunityPotion, immunityTimer);
        immunityTimer.start(); 

        // timer for the speedBoostPotion
        AnimationTimer speedBoostTimer = new AnimationTimer() {
            long startTime, elapsedTime, powerUpDuration = 5000, respawnDelay = 10000; // 5 and 10 seconds
            boolean speedBoostActive, speedBoostChecked, speedBoostTaken;

            @Override
            public void start() {
                super.start();
                startTime = 0;
                elapsedTime = 0;
                speedBoostActive = false;
                speedBoostTaken = false;
                speedBoostChecked = false;
                speedBoostPotion = new SpeedBoost();
                speedBoostPotion.setConsumed(false);
                root.getChildren().add(speedBoostPotion.getSprite());
                speedBoostPotion.getX();
                speedBoostPotion.getY();
                consumables.add(speedBoostPotion);
                startTime = System.currentTimeMillis();
            }

            @Override
            public void handle(long now) {
                elapsedTime = System.currentTimeMillis() - startTime;
                
                    if (!speedBoostChecked) { // to make sure that this will only be executed once
                        if (speedBoostPotion.isConsumed()) { // potion is taken
                            System.out.println("Speed boost taken");

                            // animation
                            DropShadow glow = new DropShadow();
                            glow.setColor(Color.YELLOW);
                            glow.setHeight(50);
                            glow.setWidth(50);
                            glow.setSpread(0.7);
                            glow.setRadius(10);
                            glow.setInput(new Glow());

                            player.getSprite().setEffect(glow);
                            root.getChildren().remove(speedBoostPotion.getSprite());
                            consumables.remove(speedBoostPotion);
                            startTime = System.currentTimeMillis();
                            player.setBoosted(true);
                            speedBoostActive = true;
                            speedBoostChecked = true;
                        }
                    }
    
                    if (speedBoostActive && elapsedTime >= powerUpDuration) { // potion duration is up
                        System.out.println("Speed boost ended");

                        // remove animation
                        player.getSprite().setEffect(null);

                        player.setBoosted(false);
                        speedBoostPotion.setConsumed(false);
                        speedBoostActive = false;
                        speedBoostTaken = true;
                    }
    
                    if (speedBoostTaken) { // respawn potion
                        if (elapsedTime >= (powerUpDuration + respawnDelay)) {
                            System.out.println("Speed Boost Potion Respawning");
                            this.start();
                        }
                    }
    
                    if (!speedBoostPotion.isConsumed() && elapsedTime >= powerUpDuration) { // if potion not consumed and time is up
                        consumables.remove(speedBoostPotion);
                        root.getChildren().remove(speedBoostPotion.getSprite());
                        speedBoostPotion.setConsumed(false);
                        if (elapsedTime >= (powerUpDuration + respawnDelay)) {
                            this.start();
                        }
                    }
            }
        };
        consumableTimers.put(speedBoostPotion, speedBoostTimer);
        speedBoostTimer.start();
    }

    private void animateSprites() { // animate the sprites
        animationTimeline = new Timeline( new KeyFrame(Duration.seconds(0.25), event -> {
            player.updateImage();
        }));
        animationTimeline.setCycleCount(Animation.INDEFINITE);
        player.setAnimation(animationTimeline);
        animationTimeline.play();

        animationTimeline = new Timeline( new KeyFrame(Duration.seconds(0.2), event -> {
            for (Enemy enemy: enemies) {
                enemy.updateImage();
                enemy.setAnimation(animationTimeline);
            }
        }));
        animationTimeline.setCycleCount(Animation.INDEFINITE);
        animationTimeline.play();
    }

    private void spawnSprites() { // spawn sprites
        // group to hold the food cells
        foodGroup = new Group();

        // spawn 50 of them
        for (int i = 0; i < 50; i++) {
            FoodCell food = new FoodCell();
            foodGroup.getChildren().add(food);
            // foodCells.add(food);
        }

        // add the food group to the root group
        root.getChildren().add(foodGroup);

        spawnConsumables();

        // initialize player 
        root.getChildren().add(player.getSprite());
        player.getSprite().requestFocus();

        // spawn enemies
        spawnEnemies();
        
    } 

    public void spawnEnemies() { // spawn enemies
        enemyGroup = new Group();

        for (int i = 0; i<10; i++) {
            enemy = new Enemy(RANDOM.nextInt(Enemy.getImagesLength()));
            enemies.add(enemy);
            enemyGroup.getChildren().add(enemy.getSprite());
        }
        root.getChildren().add(enemyGroup); 
    }

    static void createPlayer() { // create player, used when playing a game  again
        player = new Player();
    }
}
