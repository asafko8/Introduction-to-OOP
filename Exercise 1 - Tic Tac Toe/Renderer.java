/**
 * An interface that represents a rendering to display the game board on the screen.
 */
public interface Renderer {
    /**
     * Rendering the board.
     * @param board The board to be rendered.
     */
    void renderBoard(Board board);
}
