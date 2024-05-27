package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * Representing the extra paddle object of the game. Will be activated only when the strategy is on.
 */
public class ExtraPaddle extends Paddle{

    private static final int COLLISION_TO_REMOVE = 4;
    private static final String MAIN_BALL_TAG = "Main Ball";
    private static final String PUCK_TAG = "Puck";
    private final Counter hitsOnAddPaddle;
    private final Vector2 windowDimensions;
    private final GameObjectCollection gameObjects;
    private boolean onScreen;

    /**
     * Constructs a new instanceof the extra paddle.
     *
     * @param topLeftCorner The position of the extra object.
     * @param dimensions The size of the object.
     * @param renderable The renderable representing the object.
     * @param inputListener The input listener which waits for user  key board inputs and acts according them.
     * @param windowDimensions the size od the game board.
     * @param minGapFromEdges The minimum distance paddle can be from the sides edges.
     * @param gameObjects The data structure holding all the game objects currently in the game.
     */
    public ExtraPaddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                       UserInputListener inputListener, Vector2 windowDimensions,
                       float minGapFromEdges, GameObjectCollection gameObjects) {
        super(topLeftCorner, dimensions, renderable, inputListener, windowDimensions, minGapFromEdges);
        this.windowDimensions = windowDimensions;
        this.gameObjects = gameObjects;
        this.hitsOnAddPaddle = new Counter();
        this.onScreen = false;
    }

    /**
     * Responsible to follow the number of collisions it has with the main ball or the pucks,
     * and to remove itself from game object after a fixed amount of collisions.
     *
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (onScreen) {
            if (other.getTag().equals(MAIN_BALL_TAG) || other.getTag().equals(PUCK_TAG)) {
                hitsOnAddPaddle.increment();
                if (hitsOnAddPaddle.value() == COLLISION_TO_REMOVE) {
                    onScreen = false;
                    gameObjects.removeGameObject(this);
                }
            }
        }
    }

    /**
     * Notifies those who request if the extra paddle is on the screen or not.
     *
     * @return True if it is display on the screen. False otherwise.
     */
    public boolean isOnScreen () {
        return onScreen;
    }

    /**
     * Sets a multiple actions needed at the beginning of ExtraPaddleStrategy session.
     */
    public void setOnScreen() {
        onScreen = true;
        hitsOnAddPaddle.reset(); // Change!
        this.setCenter(windowDimensions.mult(0.5f));
    }
}
