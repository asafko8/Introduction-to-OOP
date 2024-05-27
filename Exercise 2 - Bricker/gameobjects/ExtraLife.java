package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * Represents the extra life object, that falls from a brick that holds ExtraLifeStrategy.
 *
 * @author Asaf Korman.
 */
public class ExtraLife extends GameObject {

    private static final String MAIN_PADDLE_TAG = "Main Paddle";
    private Counter livesCounter;
    private final Vector2 windowDimensions;
    private final GameObjectCollection gameObjects;

    /**
     * Constructs a new instance of the Extra life.
     *
     * @param topLeftCorner The position of the object.
     * @param dimensions The size of the object.
     * @param renderable The renderable representing the object.
     * @param livesCounter The lives counter that will up to date.
     * @param windowDimensions The size of the game board.
     * @param gameObjects The data structure holding all the game objects currently in the game.
     */
    public ExtraLife(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Counter livesCounter,
                     Vector2 windowDimensions, GameObjectCollection gameObjects) {
        super(topLeftCorner, dimensions, renderable);
        this.livesCounter = livesCounter;
        this.windowDimensions = windowDimensions;
        this.gameObjects = gameObjects;
    }

    /**
     * @param other The other GameObject.
     * @return True if the collided object is an instance of the main paddle.
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return other.getTag().equals(MAIN_PADDLE_TAG);
    }

    /**
     * Removes itself from the game object and increment the lives counter.
     *
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (shouldCollideWith(other)) {
            livesCounter.increment();
            gameObjects.removeGameObject(this);
        }
    }

    /**
     * Removes itself from the game object if the main paddle didn't succeed to "catch" it and "falls" out of
     * the game bounds.
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
        if (getCenter().y() >= windowDimensions.y()) {
            gameObjects.removeGameObject(this);
        }
    }
}
