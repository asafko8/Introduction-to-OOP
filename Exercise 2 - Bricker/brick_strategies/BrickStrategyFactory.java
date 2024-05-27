package bricker.brick_strategies;

import bricker.gameobjects.ExtraPaddle;
import bricker.main.BrickerGameManager;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.util.Random;

/**
 * Responsible for creating the collision strategies of the game.
 *
 * @author ASaf Korman
 */
public class BrickStrategyFactory {

    private static final int SPECIAL_STRATEGY_WITHOUT_EXTRA_BEHAVIOR = 4;
    private static final int EXTRA_PUCKS = 0;
    private static final int EXTRA_PADDLE = 1;
    private static final int CAMERA_CHANGE = 2;
    private static final int EXTRA_LIFE = 3;
    private static final int EXTRA_BEHAVIOR = 4;
    private final Random random;
    private final GameObjectCollection gameObjects;
    private final Counter bricksCounter;
    private final ImageReader imageReader;
    private final SoundReader soundReader;
    private final Vector2 windowDimensions;
    private final WindowController windowController;
    private final BrickerGameManager gameManager;
    private final Vector2 heartDimensions;
    private final Counter livesCounter;
    private final ExtraPaddle extraPaddle;
    private final Vector2 puckDimensions;

    /**
     *  Constructs a new instance of the factory.
     *
     * @param gameObjects The data structure holding all the game objects currently in the game.
     * @param bricksCounter The number of bricks remain on the board.
     * @param imageReader Contains a single method: readImage, which reads an image from disk.
     *                    See its documentation for help.
     * @param soundReader  Contains a single method: readSound, which reads a wav file from disk.
     *                     disk. See its documentation for help.
     * @param windowDimensions The size of the board game.
     * @param windowController Contains an array of helpful, self-explanatory methods
     *                         concerning the window.
     * @param gameManager Instance of the game manager.
     * @param heartDimensions the size of the extra life object.
     * @param livesCounter The lives counter.
     * @param extraPaddle Instance of the extra paddle.
     * @param puckDimensions The size of the puck object.
     */
    public BrickStrategyFactory(GameObjectCollection gameObjects, Counter bricksCounter,
                                ImageReader imageReader, SoundReader soundReader, Vector2 windowDimensions,
                                WindowController windowController, BrickerGameManager gameManager,
                                Vector2 heartDimensions, Counter livesCounter, ExtraPaddle extraPaddle,
                                Vector2 puckDimensions) {
        this.gameObjects = gameObjects;
        this.bricksCounter = bricksCounter;
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.windowDimensions = windowDimensions;
        this.windowController = windowController;
        this.gameManager = gameManager;
        this.heartDimensions = heartDimensions;
        this.livesCounter = livesCounter;
        this.extraPaddle = extraPaddle;
        this.puckDimensions = puckDimensions;
        random = new Random();
    }

    /**
     * Creates and return a strategy chosen randomly by a given range.
     *
     * @param range The range that will determine which strategies can be pick.
     *              4 - Extra Pucks, Extra Paddle, Camera Change and Extra Life.
     *              5 - All the above plus the Extra Behavior strategy.
     *              10 - All the strategy in the game, while the Basic strategy has 50% to be selected, and
     *              all the rest 10%.
     * @return Instance of the selected strategy.
     */
    public CollisionStrategy getStrategy(int range) {
        int percentage = random.nextInt(range);
        CollisionStrategy collisionStrategy;
        switch (percentage) {
        case EXTRA_PUCKS:
            collisionStrategy = new ExtraPucksStrategy(gameObjects, bricksCounter, imageReader, soundReader,
                    windowDimensions, puckDimensions);
            break;
        case EXTRA_PADDLE:
            collisionStrategy = new ExtraPaddleStrategy(gameObjects, bricksCounter, extraPaddle);
            break;
        case CAMERA_CHANGE:
            collisionStrategy = new CameraChangeStrategy(gameObjects, bricksCounter, windowController,
                    gameManager);
            break;
        case EXTRA_LIFE:
            collisionStrategy = new ExtraLifeStrategy(gameObjects, bricksCounter, imageReader,
                    heartDimensions, livesCounter, windowDimensions);
            break;
        case EXTRA_BEHAVIOR:
            CollisionStrategy strategyToDecorate = getStrategy(SPECIAL_STRATEGY_WITHOUT_EXTRA_BEHAVIOR);
            collisionStrategy = new ExtraBehaviorStrategy(strategyToDecorate, this, bricksCounter);
            break;
        default:    // Selected strategy is the Basic one.
            collisionStrategy = new BasicCollisionStrategy(gameObjects, bricksCounter);
            break;
        }
        return collisionStrategy;
    }
}
