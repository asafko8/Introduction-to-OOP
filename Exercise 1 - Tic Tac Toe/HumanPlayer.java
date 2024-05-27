/**
 * Represents a human player. Responsible for taking coordinates from the
 * human through the console and play his turn.
 */
public class HumanPlayer implements Player{

    /**
     * Constructs a new player for the human who plays through the console.
     */
    public HumanPlayer() { }

    /**
     * Requests coordinates from the user and places his mark if the coordinates
     * are "good" - that is, normal and not occupied.
     * In case the input is incorrect, it will print a message about it and
     * expect a new input.
     * @param board The game board.
     * @param mark The mark of the player.
     */
    public void playTurn(Board board, Mark mark) {
        System.out.println(Constants.playerRequestInputString(mark.name()));
        boolean inputValidation = false;
        while (!inputValidation) {
            int coor = KeyboardInput.readInt();
            int row = coor / 10;
            int col = coor % 10;
            if (row < 0 || row >= board.getSize() || col < 0 || col >= board.getSize()) {
                System.out.println(Constants.INVALID_COORDINATE);
            } else {
                inputValidation = board.putMark(mark, row, col);
                if (!inputValidation) {
                    System.out.println(Constants.OCCUPIED_COORDINATE);
                }
            }
        }
    }
}
