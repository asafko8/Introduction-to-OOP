/**
 * Represents an automatic player for the hard level.
 */
public class GeniusPlayer implements Player {

    /**
     * Construcs a new "genius" player.
     */
    public GeniusPlayer() { }

    /**
     * Plays a turn by marking any of the linked cells to his previous marks.
     * @param board The game board.
     * @param mark The mark of the player.
     */
    @Override
    public void playTurn(Board board, Mark mark) {
        if (board.putMark(mark, board.getSize() - 1, board.getSize() - 1)) {
            return;
        } else {
            for (int i = board.getSize() - 1 ; i >= 0 ; i--) {
                for (int j = board.getSize() - 1 ; j >= 0 ; j--) {
                    if (markLinkedCell(board, mark, i, j)) {
                        return;
                    }
                }
            }
        }
    }

    // Looking for the first linked available cell to mark.
    private boolean markLinkedCell(Board board, Mark mark, int row, int col) {
        if (markByCol(board, mark, row, col)) {
            return true;
        }
        if (markByRow(board, mark, row, col)) {
            return true;
        }
        return markByDiagonal(board, mark, row, col);
    }

    // Looking for a cell in the given column to mark.
    private boolean markByCol(Board board, Mark mark, int row, int col) {
        for (int i = row ; i >= 0 ; i--) {
            Mark currMark = board.getMark(i, col);
            if (currMark == mark) {
                continue;
            } else if (currMark == Mark.BLANK) {
                return board.putMark(mark, i, col);
            } else {
                break;
            }
        }
        return false;
    }

    // Looking for a cell in the given row to mark.
    private boolean markByRow(Board board, Mark mark, int row, int col) {
        for (int i = col ; i >= 0 ; i--) {
            Mark currMark = board.getMark(row, i);
            if (currMark == mark) {
                continue;
            } else if (currMark == Mark.BLANK) {
                return board.putMark(mark, row, i);
            } else {
                break;
            }
        }
        return false;
    }

    // Looking for a cell in the current diagonal to mark.
    private boolean markByDiagonal(Board board, Mark mark, int row, int col) {
        while (row >= 0 && col >= 0) {
            Mark currMark = board.getMark(row, col);
            if (currMark == mark) {
                row--;
                col--;
                continue;
            } else if (currMark == Mark.BLANK) {
                return board.putMark(mark, row, col);
            } else {
                break;
            }
        }
        return false;
    }
}