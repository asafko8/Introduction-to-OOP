package bricker.brick_strategies;

import bricker.gameobjects.ExtraLife;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * Represents a brick's collision strategy. This strategy creates an opportunity to gain extra life, by
 * dropping the extra life object from the collided brick.
 *
 * @author Asaf Korman
 */
public class ExtraLifeStrategy extends BasicCollisionStrategy {

    private static final String PATH_HEART_IMAGE = "assets/heart.png";
    private static final String EXTRA_LIFE_TAG = "Extra life";
    private static final float FALLEN_SPEED = 100;
    private final GameObjectCollection gameObjects;
    private final Vector2 heartDimensions;
    private final Counter livesCounter;
    private final Vector2 windowDimensions;
    private final Renderable heartImage;

    /**
     * Construcs a new instance of this strategy.
     *
     * @param gameObjects The data structure holding all the game objects currently in the game.
     * @param bricksCounter The number of bricks remain on the board.
     * @param imageReader Contains a single method: readImage, which reads an image from disk.
     *                    See its documentation for help.
     * @param heartDimensions The size of the fallen heart.
     * @param livesCounter The current lives count.
     * @param windowDimensions the size of the game board.
     */
    public ExtraLifeStrategy(GameObjectCollection gameObjects, Counter bricksCounter, ImageReader imageReader,
                             Vector2 heartDimensions, Counter livesCounter, Vector2 windowDimensions) {
        super(gameObjects, bricksCounter);
        this.gameObjects = gameObjects;
        this.heartImage = imageReader.readImage(PATH_HEART_IMAGE, true);
        this.heartDimensions = heartDimensions;
        this.livesCounter = livesCounter;
        this.windowDimensions = windowDimensions;
    }

    /**
     * Create the the extra life object and drop it from the the collided brick.
     *
     * @param thisObj The brick that collided with her
     * @param otherObj The ball/puck that collide with the brick.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        super.onCollision(thisObj, otherObj);
        GameObject fallenHeart = new ExtraLife(thisObj.getCenter(), heartDimensions, heartImage, livesCounter,
                windowDimensions, gameObjects);
        fallenHeart.setCenter(thisObj.getCenter());
        fallenHeart.setVelocity(new Vector2(0, FALLEN_SPEED));
        fallenHeart.setTag(EXTRA_LIFE_TAG);
        gameObjects.addGameObject(fallenHeart);
    }
}
