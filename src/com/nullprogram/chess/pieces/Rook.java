package com.nullprogram.chess.pieces;

import com.nullprogram.chess.MoveList;
import com.nullprogram.chess.Piece;

/**
 * The Chess rook.
 *
 * This class describes the movement and capture behavior of the Chess
 * rook.
 */
public class Rook extends Piece {

    /** Serialization identifier. */
    private static final long serialVersionUID = 239867335L;

    /**
     * Create a new rook on the given side.
     *
     * @param side piece owner
     */
    public Rook(final Side side) {
        super(side, "Rook");
    }

    @Override
    public final MoveList getMoves(final boolean check) {
        MoveList list = new MoveList(getBoard(), check);
        list = MoveUtil.getRookMoves(this, list);
        return list;
    }
}
