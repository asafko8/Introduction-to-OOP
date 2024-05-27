package bricker.gameobjects;

import danogl.collisions.GameObjectCollection;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Represents the puck object.
 *
 * @author Asaf Korman
 */
public class Puck extends Ball {

    private final Vector2 windowDimensions;
    private final GameObjectCollection gameObjects;

    /**
     * Constructs a new puck instance.
     *
     * @param topLeftCorner The position of the object
     * @param dimensions The size of the puck.
     * @param renderable The renderable representing the object.
     * @param collisionSound The sound heard when the puck colliding with other game objects.
     * @param windowDimensions The size of the game window.
     * @param gameObjects The data structure holding all the game objects currently in the game.
     */
    public Puck(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Sound collisionSound,
                Vector2 windowDimensions, GameObjectCollection gameObjects) {
        super(topLeftCorner, dimensions, renderable, collisionSound);
        this.windowDimensions = windowDimensions;
        this.gameObjects = gameObjects;
    }

    /**
     * Updates the object stat every deltaTime seconds, and removes itself from the game if it's out of
     * bounds. (without decrement lives counter)
     *
     * @param deltaTime The time elapsed, in seconds, since the last frame. Can
     *                  be used to determine a new position/velocity by multiplying
     *                  this delta with the velocity/acceleration respectively
     *                  and adding to the position/velocity:
     *                  velocity += deltaTime*acceleration
     *                  pos += deltaTime*velocity
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (getCenter().y() > windowDimensions.y()) {
            gameObjects.removeGameObject(this);
        }
    }
}
