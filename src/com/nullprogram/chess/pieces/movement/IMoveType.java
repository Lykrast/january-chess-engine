package com.nullprogram.chess.pieces.movement;

import com.nullprogram.chess.pieces.Piece;

public interface IMoveType {

	/**
	 * Determine moves for given situation.
	 *
	 * @param p     the piece being tested
	 * @param list  list to be appended to
	 * @return      the modified list
	 */
	IMoveList getMoves(Piece p, IMoveList list);

}