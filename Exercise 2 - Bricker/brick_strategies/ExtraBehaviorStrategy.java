package bricker.brick_strategies;

import danogl.GameObject;
import danogl.util.Counter;

/**
 * Represents a brick's collision strategy. The brick that will get this strategy will gain an extra behavior
 * or behaviors (plural), that can sums up to a fixed maximum of behaviors per brick.
 *
 * @author Asaf Korman
 */
public class ExtraBehaviorStrategy implements CollisionStrategy {


    private static final int SPECIAL_STRATEGY_WITHOUT_EXTRA_BEHAVIOR = 4;
    private static final int SPECIAL_STRATEGY = 5;
    private static final int MAX_STRATEGIES = 3;
    private static final int INITIAL_STRATEGY_NUM = 2;
    private final BrickStrategyFactory strategyFactory;
    private CollisionStrategy[] collisionStrategies;
    private int strategiesCounter;
    private final Counter bricksCounter;

    /**
     * Constructs a new instance of this strategy.
     *
     * @param strategyToDecorate The first strategy of the brick.
     * @param strategyFactory an instance of the collisions factory that will help us to build the extra
     *                        behaviors.
     * @param bricksCounter The number of bricks remain on the board.
     */
    public ExtraBehaviorStrategy(CollisionStrategy strategyToDecorate, BrickStrategyFactory strategyFactory,
                                 Counter bricksCounter) {
        this.strategyFactory = strategyFactory;
        this.bricksCounter = bricksCounter;
        this.collisionStrategies = new CollisionStrategy[MAX_STRATEGIES];
        collisionStrategies[0] = strategyToDecorate;
        collisionStrategies[1] = strategyFactory.getStrategy(SPECIAL_STRATEGY);
        this.strategiesCounter = INITIAL_STRATEGY_NUM;
    }

    /**
     * Creates and activate the strategies the brick will hold.
     *
     * @param thisObj The brick that collided with her
     * @param otherObj The ball/puck that collide with the brick.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        for (int i = 1; i < MAX_STRATEGIES; i++) {
            if (collisionStrategies[i] == null) {
                return;
            }
            if (collisionStrategies[i] instanceof ExtraBehaviorStrategy) {
                collisionStrategies[i] = strategyFactory.getStrategy(SPECIAL_STRATEGY_WITHOUT_EXTRA_BEHAVIOR);
                if (strategiesCounter < MAX_STRATEGIES) {
                    collisionStrategies[strategiesCounter] =
                            strategyFactory.getStrategy(SPECIAL_STRATEGY_WITHOUT_EXTRA_BEHAVIOR);
                    strategiesCounter++;
                }
            }
        }
        for (int i = 0; i < strategiesCounter; i++) {
            collisionStrategies[i].onCollision(thisObj, otherObj);
        }
        strategiesCounter--;
        bricksCounter.increaseBy(strategiesCounter);
    }
}