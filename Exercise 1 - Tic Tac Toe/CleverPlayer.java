import java.util.Random;

/**
 * Represents an automatic player for the medium level.
 */
public class CleverPlayer implements Player{

    private Random rand;
    private WhateverPlayer whateverPlayer;
    private GeniusPlayer geniusPlayer;

    /**
     * Constructs a new "clever" player.
     */
    public CleverPlayer() {
        rand = new Random();
        whateverPlayer = new WhateverPlayer();
        geniusPlayer = new GeniusPlayer();
    }

    /**
     * Plays a turn. In a single turn this player will act 45% like "whatever" player
     * and 55% like "genius" player.
     * @param board The game board.
     * @param mark The mark of the player.
     */
    public void playTurn(Board board, Mark mark) {
        int geniusOrWhatever = rand.nextInt(100);
        if (geniusOrWhatever > 54) {
            whateverPlayer.playTurn(board, mark);
        } else {
            geniusPlayer.playTurn(board, mark);
        }
    }
}
