package bricker.brick_strategies;

import bricker.gameobjects.Puck;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.Sound;
import danogl.gui.SoundReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.util.Random;

/**
 * Represents a brick's collision strategy. This strategy creates a (fixed) number of pucks, instead of the
 * brick.
 * @author Asaf Korman
 */
public class ExtraPucksStrategy extends BasicCollisionStrategy {
    private static final String PUCK_IMAGE_PATH = "assets/mockBall.png";
    private static final String PUCK_SOUND_PATH = "assets/blop_cut_silenced.wav";
    private static final String PUCK_TAG = "Puck";
    private static final float PUCK_SPEED = 200;
    private static final int EXTRA_PUCKS_NUM = 2;
    private Renderable puckImage;
    private Sound collisionSound;
    private final Vector2 windowDimensions;
    private final Vector2 puckDimensions;
    private GameObjectCollection gameObjects;

    /**
     * Constructs a new instance of this strategy.
     *
     * @param gameObjects The data structure holding all the game objects currently in the game.
     * @param bricksCounter The number of bricks remain on the board.
     * @param imageReader Contains a single method: readImage, which reads an image from disk.
     *                    See its documentation for help.
     * @param soundReader Contains a single method: readSound, which reads a wav file from disk.
     *                    See its documentation for help.
     * @param windowDimensions The size of the game board.
     * @param puckDimensions The size of the puck.
     */
    public ExtraPucksStrategy(GameObjectCollection gameObjects, Counter bricksCounter,
                              ImageReader imageReader, SoundReader soundReader, Vector2 windowDimensions,
                              Vector2 puckDimensions) {
        super(gameObjects, bricksCounter);
        this.gameObjects = gameObjects;
        this.puckImage = imageReader.readImage(PUCK_IMAGE_PATH, true);
        this.collisionSound = soundReader.readSound(PUCK_SOUND_PATH);
        this.windowDimensions = windowDimensions;
        this.puckDimensions = puckDimensions;
    }

    /**
     * creates and add the pucks to the game.
     * @param thisObj The brick that collided with her
     * @param otherObj The ball/puck that collide with the brick.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        super.onCollision(thisObj, otherObj);
        Random random = new Random();
        Puck[] pucks = new Puck[EXTRA_PUCKS_NUM];
        for (int i = 0; i < EXTRA_PUCKS_NUM; i++) {
            pucks[i] = new Puck(thisObj.getTopLeftCorner(), puckDimensions, puckImage,
                    collisionSound, windowDimensions, gameObjects);
            pucks[i].setCenter(thisObj.getCenter());
            double angle = random.nextDouble() * Math.PI;
            float velocityX = (float)Math.cos(angle) * PUCK_SPEED;
            float velocityY = (float)Math.cos(angle) * PUCK_SPEED;
            pucks[i].setVelocity(new Vector2(velocityX, velocityY));
            pucks[i].setTag(PUCK_TAG);
            gameObjects.addGameObject(pucks[i]);
        }
    }
}
