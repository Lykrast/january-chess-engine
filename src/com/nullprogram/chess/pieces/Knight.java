package com.nullprogram.chess.pieces;

import com.nullprogram.chess.MoveList;
import com.nullprogram.chess.Piece;

/**
 * The Chess knight.
 *
 * This class describes the movement and capture behavior of the Chess
 * knight.
 */
public class Knight extends Piece {

    /** Serialization identifier. */
    private static final long serialVersionUID = -524621034L;

    /**
     * Create a new knight on the given side.
     *
     * @param side piece owner
     */
    public Knight(final Side side) {
        super(side, "Knight");
    }

    @Override
    public final MoveList getMoves(final boolean check) {
        MoveList list = new MoveList(getBoard(), check);
        list = MoveUtil.getKnightMoves(this, list);
        return list;
    }
}
