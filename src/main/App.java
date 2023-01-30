/*
 * As of 12/6/2022 3:04 pm, I already have the menu interface but not yet functioning when clicked
 * As of 12/6/2022 7:42 pm, I already made the buttons functioning, the game itself is next
 * As of 12/7/2022 1:20 am, I already have some classes to be used in the game, but not yet integrated
 * As of 12/9/2022 10:02 pm, I already implemented the game itself, the panning of the map but not yet the objects, just explored only on how I will implement the map
 * As of 12/10/2022 4:00 pm, was able adjust the menu interface because of logo addition
 * as of 12/28/2022 2:24 I was able to do the panning of the map with accordance to the players move ment
 * as of 12/28/2022 14:13 I was able to impelement the foodcells as well its method of removing and adding 
 * as of 12/29/2022 12:56 i was able to implement the enemy class as well as its methods but not when it collides with the player
 * as of 12/29/2022 1:13 i was able to do the collision and respawning of enemies
 * as of 12/30/2022 still working on the randomization of enemies, find it hard
 * as of 12/31/2022 1:23 i was able to do the randomization of movement of enemies
 * as of 12/31/2022 14:00 i was able to do the collision of player and enemies and their respawning, make the camera based on the player coordinates
 * as of 1/1/2023 13:37 pm i was able to implement the respawning of the powerup if not consumed for 5 secs 
 * as of 1/2/2023 2:33 am i was able to implement the powerups, score board and timer
 * as of 1/2/2023 6:16 pm i was able to remove and simplify any repeating methods and attributes
 * as of 1/3/2023 5;22 pm was able to do the animation of teh sprites
 * as of 1/4/2023 1:55 am was able to integrate the menu and the game, was also able to put the subscenes of each button
 * as of 1/4/2023 3:51 am was able to do the ui of the subscenes
 * as of 1/4/2023 4:20 am was able to polish such that the enemies player and foodcells do not exceed the scene
 * as of 1/4/2023 4:28 am was able to implement the boundary such that if sprite (player and enemies) eats a food in the boundary, when they grow they will not exceed the scene (NOT YET FINISHED)
 * as of 1/4/2023 5:41 pm, was able to fo the panning and the spawning of the sprites specially the consumables such that it will not spawn outside the the scene
 * as of 1/9/2023 9:50 pm, was able to debug the collision of enemies since new additional enemies are not catch by the condition
 * as of 1/10/2023 2:00 am, was able to do the gameOver subscene
 * as of 1/10/2023 3:32 pm, was able to integrate the menu button to play the game again without duplicating the objects in the stage
 * as of 1/11/2023 2:26 am, was able to document the entire application
 */

/*********************************************************************************************************************************************************
 * This is an application similar to agar.io with a theme similar to hungry shark. This uses the W, A, S, and D key to navigate the player in the map    *
 * with a goal of increasing its size by eating fish food or pellets and smaller fishes while avoiding larger fishes as they can eat your player which   *
 * makes the game over. There are 2 powerups in this game, the immunity, which will make your player temporarily invulnerable to being eaten by bigger   *
 * fishes, and speedboost, which will make your player's speed double temporarily. This applies the concept of OOP and uses the javafx library.          *
 *                                                                                                                                                       *
 * @author Reymond P. Yncierto                                                                                                                           *
 * @created 2022-12-6 8:00                                                                                                                               *
 * @finished 2023-1-11 2:26                                                                                                                              *
 *********************************************************************************************************************************************************/
package main;

import game.ViewManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        try {
            ViewManager viewManager = new ViewManager(); // instance
            stage = viewManager.getMainStage(); // returns Stage object
            stage.setTitle("HungryFish: The Agar.io Fish Frenzy");
            stage.setResizable(false); // so that window size can't be change
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
