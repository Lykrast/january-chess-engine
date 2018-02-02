package com.nullprogram.chess.pieces;

import com.nullprogram.chess.Model;
import com.nullprogram.chess.Piece;

/**
 * Creates pieces based on their name strings.
 */
public final class PieceFactory {

    /** Hidden constructor. */
    private PieceFactory() {
    }

    /**
     * Create a new piece by name.
     *
     * @param name name of the piece
     * @param side side for the new piece
     * @return the new piece
     */
    public static Piece create(final String name, final Piece.Side side) {
    	Model model = PieceRegistry.get(name);
    	if (model == null) return null;
    	else return new Piece(side, model);
    }
}
