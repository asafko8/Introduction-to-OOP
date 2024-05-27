package bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;

/**
 * The CollisionStrategy interface defines a contract about a collision behavior of two game objects that a
 * strategy need to implement.
 *
 * @author Asaf Korman
 */
public interface CollisionStrategy {

    /**
     * Executes the strategy.
     *
     * @param thisObj The colliding object.
     * @param otherObj the collided object.
     */
    public void onCollision(GameObject thisObj, GameObject otherObj);

}
