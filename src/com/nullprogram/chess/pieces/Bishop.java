package com.nullprogram.chess.pieces;

import com.nullprogram.chess.MoveList;
import com.nullprogram.chess.Piece;

/**
 * The Chess bishop.
 *
 * This class describes the movement and capture behavior of the Chess
 * bishop.
 */
public class Bishop extends Piece {

    /** Serialization identifier. */
    private static final long serialVersionUID = 292046969L;

    /**
     * Create bishop with given side.
     *
     * @param side piece side
     */
    public Bishop(final Side side) {
        super(side, "Bishop");
    }

    @Override
    public final MoveList getMoves(final boolean check) {
        MoveList list = new MoveList(getBoard(), check);
        list = MoveUtil.getBishopMoves(this, list);
        return list;
    }
}
