package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.util.Counter;

/**
 * Represents the basic collision strategy, which removes the collided brick from the board.
 *
 * @author ASaf Korman
 */
public class BasicCollisionStrategy implements CollisionStrategy {


    private GameObjectCollection gameObjects;
    private Counter bricksCounter;

    /**
     * Constructs a new instance if this strategy.
     *
     * @param gameObjects The data structure holding all the game objects currently in the game.
     * @param bricksCounter The number of bricks remain on the board.
     */
    public BasicCollisionStrategy(GameObjectCollection gameObjects, Counter bricksCounter) {
        this.gameObjects = gameObjects;
        this.bricksCounter = bricksCounter;
    }

    /**
     * Removes the brick from the board.
     *
     * @param thisObj The colliding object.
     * @param otherObj the collided object.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        if (gameObjects.removeGameObject(thisObj, Layer.STATIC_OBJECTS)) {
            bricksCounter.decrement();
        }
    }
}
