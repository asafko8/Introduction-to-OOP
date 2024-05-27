/**
 * Responsible for mapping the string from the command line to an actual player object.
 */
public class PlayerFactory {

    private static final String HUMAN = "human";
    private static final String WHATEVER = "whatever";
    private static final String CLEVER = "clever";
    private static final String GENIUS = "genius";

    /**
     * Constructs a "PlayerFactory".
     */
    public PlayerFactory() { }

    /**
     * Build a new player according to a string given in the command line.
     * @param type The type of the player that will be created.
     * @return An instance of the new player or null in case of invalid name.
     */
    public Player buildPlayer(String type) {
        type = type.toLowerCase();
        switch (type) {
            case HUMAN:
                return new HumanPlayer();
            case CLEVER:
                return new CleverPlayer();
            case WHATEVER:
                return new WhateverPlayer();
            case GENIUS:
                return new GeniusPlayer();
            default:
                return null;
        }
    }
}
