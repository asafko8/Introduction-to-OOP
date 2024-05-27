package bricker.gameobjects;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * Representing the graphic life counter, which will be display next to the numeric life counter.
 *
 * @author Asaf Korman
 */
public class GraphicLifeCounter extends GameObject {

    private static final float HEARTS_GAP = 4;
    private static final int MAX_LIVES = 4;
    private final GameObjectCollection gameObjects;
    private final GameObject[] hearts;
    private final Vector2 topLeftCorner;
    private final Vector2 dimensions;
    private final Renderable renderable;
    private final Counter livesCounter;
    private int numOfLives;
    private Vector2 heartCoor;

    /**
     * Constructs a new graphic life counter instance.
     *
     * @param topLeftCorner The position of the object.
     * @param dimensions The size of the object.
     * @param renderable The renderable representing the object.
     * @param livesCounter The lives counter that will up to date.
     * @param gameObjects The data structure holding all the game objects currently in the game.
     * @param numOfLives The number lives that will be set while initializes the game.
     */
    public GraphicLifeCounter(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                              Counter livesCounter, GameObjectCollection gameObjects,
                              int numOfLives) {
        super(topLeftCorner, dimensions, renderable);
        this.topLeftCorner = topLeftCorner;
        this.dimensions = dimensions;
        this.renderable = renderable;
        this.livesCounter = livesCounter;
        this.gameObjects = gameObjects;
        this.numOfLives = numOfLives;
        this.hearts = new GameObject[MAX_LIVES];
        for (int i = 0; i < numOfLives; i++) {
            heartCoor = topLeftCorner.add(Vector2.RIGHT.mult((dimensions.x()  + HEARTS_GAP) * i));
            hearts[i] = new GameObject(heartCoor, dimensions, renderable);
            gameObjects.addGameObject(hearts[i], Layer.BACKGROUND);
        }
    }

    /**
     * Updates the object state every deltaTime seconds. Responsible to display the same number of hearts as
     * the current lives counter.
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
        if (livesCounter.value() < numOfLives) {
            numOfLives--;
            gameObjects.removeGameObject(hearts[numOfLives], Layer.BACKGROUND);
        }
        if (livesCounter.value() > numOfLives) {
            if (livesCounter.value() > MAX_LIVES) {
                livesCounter.decrement();
                return;
            }
            heartCoor = topLeftCorner.add(Vector2.RIGHT.mult((dimensions.x() + HEARTS_GAP) * numOfLives));
            hearts[numOfLives] = new GameObject(heartCoor, dimensions, renderable);
            gameObjects.addGameObject(hearts[numOfLives], Layer.BACKGROUND);
            numOfLives++;
        }
    }
}
