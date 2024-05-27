package bricker.gameobjects;

import bricker.brick_strategies.CollisionStrategy;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Represents the Brick object.
 *
 * @author Asaf Korman
 */
public class Brick extends GameObject {

    private CollisionStrategy collisionStrategy;

    /**
     * Constructs a new brick instance.
     *
     * @param topLeftCorner The position of the brick/
     * @param dimensions The size of the brick.
     * @param renderable The renderable representing the object.
     * @param collisionStrategy The strategy that will be implemented on collision with the ball and pucks.
     */
    public Brick(Vector2 topLeftCorner,
                 Vector2 dimensions,
                 Renderable renderable,
                 CollisionStrategy collisionStrategy) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionStrategy = collisionStrategy;
    }

    /**
     * Activate the chosen strategy for a brick.
     *
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        collisionStrategy.onCollision(this, other);
    }
}
