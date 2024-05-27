/**
 * Implements the renderer interface, but it's an empty renderer implementation that
 * doesn't actually display anything on the screen.
 * This class use us run a game between automatic players and without prints the board
 * on the screen.
 */
public class VoidRenderer implements Renderer {
    /**
     * Does nothing with the given board.
     * @param board The board to be rendered.
     */
    public void renderBoard(Board board) { }
}
