package bricker.main;

import bricker.brick_strategies.BrickStrategyFactory;
import bricker.gameobjects.*;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import danogl.gui.Sound;
import danogl.util.Counter;

import java.awt.event.KeyEvent;
import java.util.Random;

/**
 * The manager class of the Bricker game. Responsible for the initialize, updates and the game control.
 * @author Asaf Korman
 */
public class BrickerGameManager extends GameManager {

    // Constants
    private static final String BALL_IMAGE_PATH = "assets/ball.png";
    private static final String BALL_SOUND_PATH = "assets/blop_cut_silenced.wav";
    private static final String PADDLE_IMAGE_PATH = "assets/paddle.png";
    private static final String BRICK_IMAGE_PATH = "assets/brick.png";
    private static final String HEART_IMAGE_PATH = "assets/heart.png";
    private static final String BACKGROUND_PATH = "assets/DARK_BG2_small.jpeg";
    private static final String PLAY_AGAIN_PROMPT = " Play again?";
    private static final String MAIN_BALL_TAG = "Main Ball";
    private static final String MAIN_PADDLE_TAG = "Main Paddle";
    private static final String GAME_NAME = "Bricker";
    private static final String WIN_PROMPT = "You win!";
    private static final String LOSE_PROMPT = "You lose!";
    private static final int INITIAL_STRATEGY = 10;
    private static final float WALL_WIDTH = 20;
    private static final float WINDOW_WIDTH = 700;
    private static final float WINDOW_HEIGHT = 500;
    private static final float PADDLE_HEIGHT = 15;
    private static final float PADDLE_WIDTH = 100;
    private static final float PADDLE_GAP_FROM_BOTTOM_EDGE = 30;
    private static final float BALL_SIZE = 20;
    private static final float BALL_SPEED = 180;
    private static final float PUCK_SIZE = BALL_SIZE * 0.75f;
    private static final int DEFAULT_BRICKS_ROWS = 7;
    private static final int DEFAULT_BRICKS_PER_ROW = 8;
    private static final int ARGS_PARAM = 2;
    private static final float BRICK_HEIGHT = 15;
    private static final float HEART_SIZE = 25;
    private static final int INIT_LIVES = 3;
    private static final int COLLISIONS_TO_ZOOM_OFF = 4;
    private static final float GAPS_BETWEEN_OBJECTS = 4;
    private static final int TARGET_FRAME = 80;
    // Data members
    private final int bricksRows;
    private final int bricksPerRows;
    private final Counter livesCounter;
    private final Counter bricksCounter;
    private Vector2 windowDimensions;
    private Ball ball;
    private WindowController windowController;
    private UserInputListener inputListener;
    private boolean zoomOn;
    private int collisionsWhenCameraTurnOn;

    /**
     * Constructs a game manager instance.
     *
     * @param windowTitle The title of the game window.
     * @param windowDimensions The size of the game window.
     * @param bricksRows The rows bricks number in the game.
     * @param bricksPerRows The brick per row number.
     */
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions,
                              int bricksRows,int bricksPerRows) {
        super(windowTitle, windowDimensions);
        this.bricksRows = bricksRows;
        this.bricksPerRows = bricksPerRows;
        this.bricksCounter = new Counter(bricksPerRows * bricksRows);
        this.livesCounter = new Counter(INIT_LIVES);
    }

    /**
     * Updates the game every given (deltaTime) seconds.
     *
     * @param deltaTime The time, in seconds, that passed since the last invocation
     *                  of this method (i.e., since the last frame). This is useful
     *                  for either accumulating the total time that passed since some
     *                  event, or for physics integration (i.e., multiply this by
     *                  the acceleration to get an estimate of the added velocity or
     *                  by the velocity to get an estimate of the difference in position).
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float ballHeight = ball.getCenter().y();
        if (ballHeight > WINDOW_HEIGHT) {
            livesCounter.decrement();
            repositionBall();
        }
        checkForEndGame();
        cameraUpdate();
    }

    /*
     * Check if the game is over.
     */
    private void checkForEndGame() {
        String prompt = "";
        if (livesCounter.value() == 0) {
            prompt += LOSE_PROMPT;
        }
        if (bricksCounter.value() == 0 || inputListener.isKeyPressed(KeyEvent.VK_W)) {
            prompt += WIN_PROMPT;
        }
        if (!prompt.isEmpty()) {
            prompt += PLAY_AGAIN_PROMPT;
            if (windowController.openYesNoDialog(prompt)) {
                bricksCounter.increaseBy(bricksRows * bricksPerRows - bricksCounter.value());
                livesCounter.increaseBy(INIT_LIVES - livesCounter.value());
                windowController.resetGame();
            }
            else {
                windowController.closeWindow();
            }
        }
    }

    /**
     * Sets camera zoom in, in case the ChangeCameraStrategy is need to be activated.
     */
    public void setZoomOn() {
        zoomOn = true;
        collisionsWhenCameraTurnOn = ball.getCollisionCounter();
    }

    /*
     * Resposible to shut down the zoom according to the ChangeCameraStrategy.
     */
    private void cameraUpdate() {
        int currCollisions = ball.getCollisionCounter() - collisionsWhenCameraTurnOn;
        if (zoomOn && currCollisions == COLLISIONS_TO_ZOOM_OFF) {
            setCamera(null);
            zoomOn = false;
        }
    }

    /**
     * Initialize the game by create all the game objects and instance necessary for running the game.
     *
     * @param imageReader Contains a single method: readImage, which reads an image from disk.
     *                 See its documentation for help.
     * @param soundReader Contains a single method: readSound, which reads a wav file from
     *                    disk. See its documentation for help.
     * @param inputListener Contains a single method: isKeyPressed, which returns whether
     *                      a given key is currently pressed by the user or not. See its
     *                      documentation.
     * @param windowController Contains an array of helpful, self explanatory methods
     *                         concerning the window.
     */
    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.windowController = windowController;
        this.windowDimensions = windowController.getWindowDimensions();
        this.inputListener = inputListener;
        windowController.setTargetFramerate(TARGET_FRAME);

        // Create game objects
        createBall(imageReader, soundReader);
        createPaddle(imageReader, inputListener);
        createWalls();
        createBackground(imageReader);
        ExtraPaddle extraPaddle = createExtraPaddle(imageReader);
        BrickStrategyFactory strategyFactory = new BrickStrategyFactory(gameObjects(), bricksCounter,
                imageReader, soundReader, windowDimensions, windowController, this,
                new Vector2(HEART_SIZE, HEART_SIZE), livesCounter, extraPaddle,
                new Vector2(PUCK_SIZE, PUCK_SIZE));
        createBricks(imageReader, strategyFactory);
        createGraphicLifeCounter(imageReader);
        createNumericLifeCounter();
    }

    /*
     * Creates the main ball of the game.
     */
    private void createBall(ImageReader imageReader, SoundReader soundReader) {
        Renderable ballImage = imageReader.readImage(BALL_IMAGE_PATH, true);
        Sound collisionSound = soundReader.readSound(BALL_SOUND_PATH);
        ball = new Ball(Vector2.ZERO, new Vector2(BALL_SIZE, BALL_SIZE), ballImage, collisionSound);
        repositionBall();
        ball.setTag(MAIN_BALL_TAG);
        gameObjects().addGameObject(ball);
    }

    /*
     *  Setting a random direction for the ball at the start of a round in the game.
     */
    private void repositionBall() {
        float ballVellX = BALL_SPEED;
        float ballVellY = BALL_SPEED;
        Random rand = new Random();
        if (rand.nextBoolean()) {
            ballVellX *= -1;
        }
        if (rand.nextBoolean()) {
            ballVellY *= -1;
        }
        ball.setCenter(windowDimensions.mult(0.5f));
        ball.setVelocity(new Vector2(ballVellX, ballVellY));
    }

    /*
     * Creates the main paddle of the game.
     */
    private void createPaddle(ImageReader imageReader, UserInputListener inputListener) {
        Renderable paddleImage = imageReader.readImage(PADDLE_IMAGE_PATH, true);
        GameObject paddle = new Paddle(Vector2.ZERO,
                new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT), paddleImage, inputListener,
                new Vector2(WINDOW_WIDTH, WINDOW_HEIGHT), WALL_WIDTH);
        paddle.setCenter(new Vector2(windowDimensions.x()/2,
                windowDimensions.y() - PADDLE_GAP_FROM_BOTTOM_EDGE));
        paddle.setTag(MAIN_PADDLE_TAG);
        gameObjects().addGameObject(paddle);
    }

    /*
     * Creates the extra paddle object of the game.
     */
    private ExtraPaddle createExtraPaddle(ImageReader imageReader) {
        Renderable paddleImage = imageReader.readImage(PADDLE_IMAGE_PATH, true);
        return new ExtraPaddle(Vector2.ZERO, new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT), paddleImage,
                inputListener, windowDimensions, WALL_WIDTH, gameObjects());
    }

    /*
     * Creates the walls of the game which will form its borders
     */
    private void createWalls() {
        GameObject leftWall = new GameObject(Vector2.ZERO,
                new Vector2(WALL_WIDTH, windowDimensions.y()), null);
        GameObject rightWall = new GameObject(new Vector2(windowDimensions.x() - WALL_WIDTH, 0),
                new Vector2(WALL_WIDTH, windowDimensions.y()), null);
        GameObject upperWall = new GameObject(Vector2.ZERO,
                new Vector2(windowDimensions.x(), WALL_WIDTH), null);
        gameObjects().addGameObject(leftWall, Layer.STATIC_OBJECTS);
        gameObjects().addGameObject(rightWall, Layer.STATIC_OBJECTS);
        gameObjects().addGameObject(upperWall, Layer.STATIC_OBJECTS);
    }

    /*
     * Creates the background of the game.
     */
    private void createBackground(ImageReader imageReader) {
        Renderable backgroundImage = imageReader.readImage(BACKGROUND_PATH, false);
        GameObject background = new GameObject(Vector2.ZERO, windowDimensions, backgroundImage);
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(background, Layer.BACKGROUND);
    }

    /*
     * Creates the bricks of the game.
     */
    private void createBricks(ImageReader imageReader, BrickStrategyFactory strategyFactory) {
        float brickWidth = (windowDimensions.x() - (2 * WALL_WIDTH) - bricksPerRows - 1) / bricksPerRows;
        Renderable brickImage = imageReader.readImage(BRICK_IMAGE_PATH, false);
        float brickXcoor = WALL_WIDTH + 1;
        float brickYcoor = WALL_WIDTH + 1;
        for (int i = 0; i < bricksRows; i++) {
            for (int j = 0; j < bricksPerRows; j++) {
                GameObject brick = new Brick(new Vector2(brickXcoor, brickYcoor),
                        new Vector2(brickWidth, BRICK_HEIGHT), brickImage,
                        strategyFactory.getStrategy(INITIAL_STRATEGY));
                gameObjects().addGameObject(brick, Layer.STATIC_OBJECTS);
                brickXcoor += brickWidth + 1;
            }
            brickXcoor = WALL_WIDTH + 1;
            brickYcoor += BRICK_HEIGHT + 1;
        }
    }

    /*
     * Creates the graphic lives counter.
     */
    private void createGraphicLifeCounter(ImageReader imageReader) {
        Renderable heartImage = imageReader.readImage(HEART_IMAGE_PATH, true);
        float heartsXCoor = 1 + HEART_SIZE + GAPS_BETWEEN_OBJECTS;
        float heartsYCoor = WINDOW_HEIGHT - HEART_SIZE - 1;
        GraphicLifeCounter graphicLifeCounter = new GraphicLifeCounter(
                new Vector2(heartsXCoor, heartsYCoor), new Vector2(HEART_SIZE, HEART_SIZE),
                heartImage, livesCounter, gameObjects(), INIT_LIVES);
        gameObjects().addGameObject(graphicLifeCounter, Layer.BACKGROUND);
    }

    /*
     * Creates the numeric lives counter.
     */
    private void createNumericLifeCounter() {
        NumericLifeCounter numericLifeCounter = new NumericLifeCounter(
                new Vector2(1, WINDOW_HEIGHT - HEART_SIZE - 1),
                new Vector2(HEART_SIZE, HEART_SIZE), null, livesCounter);
        gameObjects().addGameObject(numericLifeCounter, Layer.BACKGROUND);
    }

    /**
     * The main method of the game. Initializes and run the game.
     *
     * @param args The command-line arguments. if there are arguments, the first onr will determine how many
     *             bricks will create in a row, and the second one how many rows of bricks will create.
     *             if there is 0 command-line arguments the default setting will create. 7 rows and 8
     *             bricks per row.
     */
    public static void main(String[] args) {
        int bricksPerRows = DEFAULT_BRICKS_PER_ROW;
        int bricksRows = DEFAULT_BRICKS_ROWS;
        if (args.length == ARGS_PARAM) {
            bricksPerRows = Integer.parseInt(args[0]);
            bricksRows = Integer.parseInt(args[1]);
        }
        BrickerGameManager brickerGameManager =
                new BrickerGameManager(GAME_NAME, new Vector2(WINDOW_WIDTH, WINDOW_HEIGHT),
                        bricksRows, bricksPerRows);
        brickerGameManager.run();
    }
}
