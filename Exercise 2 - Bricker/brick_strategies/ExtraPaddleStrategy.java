package bricker.brick_strategies;

import bricker.gameobjects.ExtraPaddle;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * Represents a brick's collision strategy. This strategy creates an extra paddle that will be place in the
 * center of the board, instead of the brick.
 *
 * @author Asaf Korman
 */
public class ExtraPaddleStrategy extends BasicCollisionStrategy {

    private final GameObjectCollection gameObjects;
    private final ExtraPaddle extraPaddle;

    /**
     * Constructs a new instance of this strategy.
     *
     * @param gameObjects The data structure holding all the game objects currently in the game.
     * @param bricksCounter The number of bricks remain on the board.
     * @param extraPaddle The instance of the extra paddle that will be activated when this strategy is on.
     */
    public ExtraPaddleStrategy(GameObjectCollection gameObjects, Counter bricksCounter,
                               ExtraPaddle extraPaddle) {
        super(gameObjects, bricksCounter);
        this.gameObjects = gameObjects;
        this.extraPaddle = extraPaddle;
    }

    /**
     * Activate the extra paddle in case it is not already on.
     *
     * @param thisObj The brick that collided with her
     * @param otherObj The ball/puck that collide with the brick.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        super.onCollision(thisObj, otherObj);
        if (!extraPaddle.isOnScreen()) {
            extraPaddle.setOnScreen();
            gameObjects.addGameObject(extraPaddle);
        }
    }
}
