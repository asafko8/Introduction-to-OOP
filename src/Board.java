/**
 * Responsible for the state of the board: the size of the board, marking squares
 * and saving everything marked on the board.
 * The numbering of the cells on the board starts with 0.
 */
public class Board {

    private static final int DEFAULT_BOARD_SIZE = 4;
    private Mark[][] board;
    private int size;

    /**
     * Constructs a new board with the default board size.
     */
    public Board() {
        createBoard(DEFAULT_BOARD_SIZE);
    }

    /**
     * Constructs a new board with a given board size.
     * @param size The size of the board.
     */
    public Board(int size) {
        createBoard(size);
    }

    // Creates new board according to a given size.
    private void createBoard(int size) {
        this.size = size;
        board = new Mark[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = Mark.BLANK;
            }
        }
    }

    /**
     * Returns the size of the board.
     * @return the number of columns/rows of the board. (the board is square)
     */
    public int getSize() {
        return this.size;
    }

    /**
     * Marks a given cell on the board.
     * @param mark The specific marking that needs to be marked.
     * @param row The specific row of the cell to mark.
     * @param col The specific col of the cell to mark.
     * @return true if the given cell marked successfully, false if not.
     */
    public boolean putMark(Mark mark, int row, int col) {
        if (row < 0 || row >= size || col < 0 || col >= size || board[row][col] != Mark.BLANK) {
            return false;
        }
        board[row][col] = mark;
        return true;
    }

    /**
     * Returns the mark for the given cell.
     * @param row The specific row of the cell.
     * @param col The specific col. of the cell.
     * @return the mark for the given cell, or BLANK in case of invalid coordinates.
     */
    public Mark getMark(int row, int col) {
        if (row < 0 || row >= size || col < 0 || col >= size)
            return Mark.BLANK;
        return board[row][col];
    }
}
