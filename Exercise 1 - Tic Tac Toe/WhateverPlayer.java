import java.util.Random;

/**
 * Represents an automatic player for the easy level.
 */
public class WhateverPlayer implements Player {

    private Random rand;

    /**
     * Constructs a new "whatever" player.
     */
    public WhateverPlayer() {
        this.rand = new Random();
    }

    /**
     * Plays a turn by putting marks randomly on the board.
     * @param board The game board.
     * @param mark The mark of the player.
     */
    public void playTurn(Board board, Mark mark) {
        boolean inputValidation = false;
        while (!inputValidation) {
            int row = rand.nextInt(board.getSize());
            int col = rand.nextInt(board.getSize());
            inputValidation = board.putMark(mark, row, col);
        }
    }
}
