package com.nullprogram.chess;

public abstract class MoveType {

    /**
     * Determine moves for given situation.
     *
     * @param p     the piece being tested
     * @param list  list to be appended to
     * @return      the modified list
     */
    public abstract MoveList getMoves(final Piece p, final MoveList list);

}
