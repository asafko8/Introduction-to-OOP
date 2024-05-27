package bricker.brick_strategies;

import bricker.gameobjects.Ball;
import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * Represents a brick's collision strategy. This strategy will focus the camera on the main ball.
 *
 * @author Asaf Korman
 */
public class CameraChangeStrategy extends BasicCollisionStrategy {

    private static final String MAIN_BALL_TAG = "Main Ball";
    private final WindowController windowController;
    private final BrickerGameManager gameManager;

    /**
     * Constructs a new instance of this strategy.
     *
     * @param gameObjects The data structure holding all the game objects currently in the game.
     * @param bricksCounter The number of bricks remain on the board.
     * @param windowController Contains an array of helpful, self-explanatory methods
     *                         concerning the window.
     * @param gameManager Instance of the game manager. need for the camera control.
     */
    public CameraChangeStrategy(GameObjectCollection gameObjects, Counter bricksCounter,
                                WindowController windowController, BrickerGameManager gameManager) {
        super(gameObjects, bricksCounter);
        this.windowController = windowController;
        this.gameManager = gameManager;
    }

    /**
     * Sets camera zoom on the main ball, in case it is not already on.
     *
     * @param thisObj The colliding object.
     * @param otherObj the collided object.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        super.onCollision(thisObj, otherObj);
        // Checking if otherObj is the main ball of the game
        if (otherObj.getTag().equals(MAIN_BALL_TAG) && gameManager.camera() == null) {
            gameManager.setCamera(new Camera(otherObj, Vector2.ZERO,
                    windowController.getWindowDimensions().mult(1.2f),
                    windowController.getWindowDimensions()));
            gameManager.setZoomOn();
        }
    }
}
