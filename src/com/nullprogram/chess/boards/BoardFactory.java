package com.nullprogram.chess.boards;

import com.nullprogram.chess.Board;

/**
 * Creates a new board on demand, for use of board copying.
 */
public final class BoardFactory {

    /**
     * The Gothic chess board.
     */
    private static Class<? extends Gothic> gothic = (new Gothic()).getClass();

    /**
     * The standard chess board.
     */
    private static Class<? extends StandardBoard> standard = (new StandardBoard()).getClass();
    private static Class<? extends BoardWildebeest> wildebeest = (new BoardWildebeest()).getClass();

    /**
     * An empty chess board.
     */
    private static Class<? extends EmptyBoard> empty = (new EmptyBoard()).getClass();

    /**
     * Hidden constructor.
     */
    private BoardFactory() {
    }

    /**
     * Create a new chess board of the given class.
     *
     * @param board class to be created
     * @return a fresh board
     */
    public static Board create(final Class<?> board) {
        if (board.equals(standard)) {
            return new StandardBoard();
        } else if (board.equals(gothic)) {
            return new Gothic();
        } else if (board.equals(empty)) {
            return new EmptyBoard();
        } else if (board.equals(wildebeest)) {
            return new BoardWildebeest();
        } else {
            /* Throw exception? */
            return null;
        }
    }
}
