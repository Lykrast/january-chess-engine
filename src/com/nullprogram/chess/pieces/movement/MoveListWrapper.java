package com.nullprogram.chess.pieces.movement;

import com.nullprogram.chess.Move;
import com.nullprogram.chess.pieces.Piece;
import com.nullprogram.chess.pieces.movement.MoveType.MoveMode;

/**
 * A wrapper around a MoveList that modifies and/or restrics Moves that are added to it through this object.
 * @author Lykrast
 */
public abstract class MoveListWrapper implements IMoveList {
	protected Piece piece;
	protected IMoveList list;

	public MoveListWrapper(Piece piece, IMoveList list) {
		this.piece = piece;
		this.list = list;
	}
	
	/**
	 * Modifies a Move to do what the wrapper wants to do or restrict.
	 * @param move Move to check/modify
	 * @return the modified Move, or null if the move shouldn't be added
	 */
	protected abstract Move modify(Move move);

	@Override
	public boolean checksCheck() {
		return list.checksCheck();
	}

	@Override
	public boolean add(Move move) {
		Move modified = modify(move);
		if (modified != null) return list.add(modified);
		else return false;
	}

	@Override
	public boolean add(Move move, MoveMode type) {
		Move modified = modify(move);
		if (modified != null) return list.add(modified, type);
		else return false;
	}

	@Override
	public boolean addMove(Move move) {
		Move modified = modify(move);
		if (modified != null) return list.addMove(modified);
		else return false;
	}

	@Override
	public boolean addCapture(Move move) {
		Move modified = modify(move);
		if (modified != null) return list.addCapture(modified);
		else return false;
	}

	@Override
	public boolean addCaptureOnly(Move move) {
		Move modified = modify(move);
		if (modified != null) return list.addCaptureOnly(modified);
		else return false;
	}

}
