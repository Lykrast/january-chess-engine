package com.nullprogram.chess.pieces;

import com.nullprogram.chess.MoveList;
import com.nullprogram.chess.Piece;

/**
 * The Camel, which is a (1,3) Leaper.
 */
public class Camel extends Piece {

    /** Serialization identifier. */
	private static final long serialVersionUID = 2225568474083936877L;

	/**
     * Create a new knight on the given side.
     *
     * @param side piece owner
     */
    public Camel(final Side side) {
        super(side, "Camel");
    }

    @Override
    public final MoveList getMoves(final boolean check) {
        MoveList list = new MoveList(getBoard(), check);
        list = MoveUtil.getLeaperMoves(this, list, 1, 3);
        return list;
    }
}
