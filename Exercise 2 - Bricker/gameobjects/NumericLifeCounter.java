package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Representing the numeric life counter, which will be display the bottom left corner of the board game.
 *
 * @author Asaf Korman
 */
public class NumericLifeCounter extends GameObject {

    private Counter livesCounter;
    private TextRenderable textRenderable;

    /**
     * Constructs a new numeric life counter instance.
     *
     * @param topLeftCorner The position of the object.
     * @param dimensions The size of the object.
     * @param renderable The renderable representing the object.
     * @param livesCounter The lives counter that will up to date.
     */
    public NumericLifeCounter(Vector2 topLeftCorner, Vector2 dimensions,
                              Renderable renderable, Counter livesCounter) {
        super(topLeftCorner, dimensions, renderable);
        this.livesCounter = livesCounter;
        textRenderable = new TextRenderable(String.format("%d", livesCounter.value()));
        textRenderable.setColor(Color.GREEN);
        this.renderer().setRenderable(textRenderable);
    }

    /**
     * Updates the object state every deltaTime seconds.
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
        switch (livesCounter.value()) {
        case 1:
            textRenderable.setColor(Color.RED);
            break;
        case 2:
            textRenderable.setColor(Color.YELLOW);
            break;
        default:
            textRenderable.setColor(Color.GREEN);
            break;
        }
        textRenderable.setString(String.format("%d", livesCounter.value()));
    }
}
