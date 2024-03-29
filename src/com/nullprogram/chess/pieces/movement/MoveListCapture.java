package com.nullprogram.chess.pieces.movement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.nullprogram.chess.Move;
import com.nullprogram.chess.Position;
import com.nullprogram.chess.boards.Board;
import com.nullprogram.chess.pieces.Piece;

/**
 * Custom list of moves to show what squares are being attacked.
 */
public class MoveListCapture implements Iterable<Move>, IMoveList {

	/** The board used to verify positions before adding them. */
	private Board board;

	/** The actual list of moves. */
	private final List<Move> moves = new ArrayList<Move>();

	/**
	 * Create a new move list relative to a board.
	 *
	 * @param verifyBoard the board to be used
	 */
	public MoveListCapture(final Board verifyBoard) {
		board = verifyBoard;
	}

	@Override
	public boolean checksCheck() {
		return false;
	}

	@Override
	public final boolean add(final Move move) {
		moves.add(move);
		return true;
	}

	@Override
	public final boolean add(final Move move, MoveType.MoveMode type) {
		Position dest = move.destination;
		if (!board.inRange(dest)) return false;

		// Add the move to the list if it could capture something
		if (type.captureEnemy) add(move);
		// Simulate the return from a normal MoveList
		if (board.isEmpty(dest)) return true;
		else {
			Piece p = board.getPiece(move.origin);
			// Enemy
			if (board.getPiece(dest).getSide() != p.getSide()) {
				if (type.captureEnemy) return true;
				else return false;
			}
			// Friendly
			else {
				if (type.captureFriendly) return true;
				else return false;
			}
		}
	}

	@Override
	public final boolean addMove(final Move move) {
		return board.isFree(move.destination);
	}

	@Override
	public final boolean addCaptureOnly(final Move move) {
		Piece p = board.getPiece(move.origin);
		add(move);
		return board.canCaptureEnemyAt(move.destination, p);
	}

	/**
	 * Determine if this move list is empty.
	 * 
	 * @return true if empty
	 */
	public final boolean isEmpty() {
		return moves.isEmpty();
	}

	@Override
	public final Iterator<Move> iterator() {
		return moves.iterator();
	}
}
