package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

/**
 * Represents the paddle object.
 *
 * @author Asaf Korman
 */
public class Paddle extends GameObject {

    private static final float MOVEMENT_SPEED = 300;
    private final UserInputListener inputListener;
    private final Vector2 windowDimensions;
    private final float minGapFromEdges;

    /**
     * Constructs a new paddle instance.
     *
     * @param topLeftCorner The position of the object.
     * @param dimensions The size of the paddle.
     * @param renderable The renderable representing the object.
     * @param inputListener The input listener which waits for user  key board inputs and acts according them.
     * @param windowDimensions The size of the game window.
     * @param minGapFromEdges The minimum distance paddle can be from the sides edges.
     */
    public Paddle(Vector2 topLeftCorner,
                  Vector2 dimensions,
                  Renderable renderable,
                  UserInputListener inputListener,
                  Vector2 windowDimensions, float minGapFromEdges) {
        super(topLeftCorner, dimensions, renderable);
        this.inputListener = inputListener;
        this.windowDimensions = windowDimensions;
        this.minGapFromEdges = minGapFromEdges;
    }

    /**
     * Updates thee object state every deltaTime seconds, and taking care for it to not cross the game bounds.
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
        Vector2 movmentDir = Vector2.ZERO;
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT) &&
                getTopLeftCorner().x() >= minGapFromEdges) {
            movmentDir = movmentDir.add(Vector2.LEFT);
        }
        float rightBorder = windowDimensions.x() - minGapFromEdges - getDimensions().x();
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT) &&  getTopLeftCorner().x() <= rightBorder) {
            movmentDir = movmentDir.add(Vector2.RIGHT);
        }
        setVelocity(movmentDir.mult(MOVEMENT_SPEED));
    }
}
