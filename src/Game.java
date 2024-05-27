/**
 * Represents a single game. It manages the game, knowing when a game is ended, who was the winner and whether
 * it ended in a draw.
 */
public class Game {

    private static final int DEFAULT_WIN_STREAK = 3;
    private static final int MIN_WIN_STREAK = 2;
    private Board board;
    private Player playerX;
    private Player playerO;
    private Renderer renderer;
    private int winStreak;

    /**
     * Constructs a new game with two given players and renderer, and the default values
     * for board and win streak.
     * @param playerX
     * @param playerO
     * @param renderer
     */
    public Game(Player playerX, Player playerO, Renderer renderer) {
        board = new Board();
        this.playerX = playerX;
        this.playerO = playerO;
        this.renderer = renderer;
        winStreak = DEFAULT_WIN_STREAK;
    }

    /**
     * Constructs a new game with two given players, renderer, size for the board and win streak.
     * @param playerX
     * @param playerO
     * @param size
     * @param winStreak
     * @param renderer
     */
    public Game(Player playerX, Player playerO, int size, int winStreak, Renderer renderer) {
        board = new Board(size);
        this.playerX = playerX;
        this.playerO = playerO;
        this.renderer = renderer;
        if (winStreak > size || winStreak < MIN_WIN_STREAK) {
            winStreak = size;
        }
        this.winStreak = winStreak;
    }

    /**
     * Returns the streak needed to ein the game.
     * @return The length of sequence a player need to win.
     */
    public int getWinStreak() {
        return winStreak;
    }

    /**
     * Returns the size of the board.
     * @return The number of columns/rows of the board. (the board is square)
     */
    public int getBoardSize() {
        return board.getSize();
    }

    /**
     * Runs a complete game - from start to finish, and returns the winner's mark.
     * The game ends when one of the players has a winning streak or when there are no
     * empty spaces left on the board.
     * In case the game ends in a draw, BLANK will return.
     * @return The winner's mark or BLANK in case of a draw.
     */
    public Mark run() {
        for (int i = 0; i < Math.pow(board.getSize(),2); i++) {
            if (i % 2 == 0) {
                playerX.playTurn(board, Mark.X);
                renderer.renderBoard(board);
                if (isWin(Mark.X)) {
                    return Mark.X;
                }
            }
            else {
                playerO.playTurn(board, Mark.O);
                renderer.renderBoard(board);
                if (isWin(Mark.O)) {
                    return Mark.O;
                }
            }
        }
        return Mark.BLANK;
    }

    // Check if we have a winner.
    private boolean isWin(Mark mark) {
        if (rowStreak(mark) || colStreak((mark)) || diagLeftStreak(mark) || diagRightStreak(mark)) {
            return true;
        }
        return false;
    }

    // Check if there is a row streak.
    private boolean rowStreak(Mark mark) {
        for (int i = 0; i < board.getSize() ; i++) {
            int streak = 0;
            for (int j = 0; j < board.getSize(); j++) {
                if (board.getMark(i, j) == mark) {
                    streak++;
                }
                else {
                    streak = 0;
                }
                if (streak == winStreak) {
                    return true;
                }
            }
        }
        return false;
    }

    // Check if there is a col streak.
    private boolean colStreak(Mark mark) {
        for (int i = 0; i < board.getSize() ; i++) {
            int streak = 0;
            for (int j = 0; j < board.getSize(); j++) {
                if (board.getMark(j, i) == mark) {
                    streak++;
                }
                else {
                    streak = 0;
                }
                if (streak == winStreak) {
                    return true;
                }
            }
        }
        return false;
    }

    // Check if there is a right-diagonal streak.
    private boolean diagRightStreak(Mark mark) {
        for (int i = 0 ; i < 2 * board.getSize() - 1 ; i++) {
            if (i < board.getSize()) {
                int row = i;
                int col = 0;
                int streak = 0;
                while (row >= 0) {
                    if (board.getMark(row, col) == mark) {
                        streak++;
                    }
                    else {
                        streak = 0;
                    }
                    if (streak == winStreak) {
                        return true;
                    }
                    row--;
                    col++;
                }
            }
            else {
                int row = board.getSize() - 1;
                int col =  i - board.getSize() + 1;
                int streak = 0;
                while (col < board.getSize()) {
                    if (board.getMark(row, col) == mark) {
                        streak++;
                    }
                    else {
                        streak = 0;
                    }
                    if (streak == winStreak) {
                        return true;
                    }
                    row--;
                    col++;
                }
            }
        }
        return false;
    }

    // Check if there is a left-diagonal streak.
    private boolean diagLeftStreak(Mark mark) {
        for (int i = 2 * board.getSize() - 2 ; i >= 0 ; i--) {
            if (i < board.getSize()) {
                int row = i;
                int col = 0;
                int streak = 0;
                while (row < board.getSize()) {
                    if (board.getMark(row, col) == mark) {
                        streak++;
                    }
                    else {
                        streak = 0;
                    }
                    if (streak == winStreak) {
                        return true;
                    }
                    row++;
                    col++;
                }
            }
            else {
                int row = i - board.getSize();
                int col = board.getSize() - 1;
                int streak = 0;
                while (row >= 0) {
                    if (board.getMark(row, col) == mark) {
                        streak++;
                    }
                    else {
                        streak = 0;
                    }
                    if (streak == winStreak) {
                        return true;
                    }
                    row--;
                    col--;
                }
            }
        }
        return false;
    }
}
