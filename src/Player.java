/**
 * An interface that represents a particular logic or strategy for
 * taking a turn in a game given a particular board.
 */
public interface Player {
    /**
     * Plays a turn of the player.
     * @param board The game board.
     * @param mark The mark of the player.
     */
    void playTurn(Board board, Mark mark);
}
