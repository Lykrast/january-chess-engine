package com.nullprogram.chess.pieces;

import com.nullprogram.chess.MoveList;
import com.nullprogram.chess.Piece;

/**
 * The Wildbeest, which is a Camel and Knight compound.
 */
public class Wildebeest extends Piece {
	
	/** Serialization identifier. */
	private static final long serialVersionUID = -2476070048358077069L;

	/**
     * Create a new knight on the given side.
     *
     * @param side piece owner
     */
    public Wildebeest(final Side side) {
        super(side, "Wildebeest");
    }

    @Override
    public final MoveList getMoves(final boolean check) {
        MoveList list = new MoveList(getBoard(), check);
        list = MoveUtil.getKnightMoves(this, list);
        list = MoveUtil.getLeaperMoves(this, list, 1, 3);
        return list;
    }
}
