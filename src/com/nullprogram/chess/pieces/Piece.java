package com.nullprogram.chess.pieces;

import java.awt.Image;

import com.nullprogram.chess.Position;
import com.nullprogram.chess.boards.Board;
import com.nullprogram.chess.pieces.movement.MoveList;
import com.nullprogram.chess.pieces.movement.MoveListCapture;

/**
 * The abstract base class for implementing chess pieces. Implementing
 * a new piece takes several steps. Subclass this class, making sure
 * to override the constructor and provide two 200x200 PNG images for
 * the piece using the names PieceName-BLACK.png and
 * PieceName-WHITE.png. That's enough to cover non-AI games.
 *
 * To make it work with the AI, which you will certainly want to do,
 * you need to add a piece weight to the default AI configuration
 * (default.properies) and a parse entry in the Minimax constructor,
 * right after all the other pieces.
 */
public class Piece {
	private Model model;

	/** The side this piece belongs to. */
	private Side side;

	/** The position of this piece. */
	private Position pos;

	/** The board this piece is on. */
	private Board board;

	/** Movement counter. */
	private int moved = 0;

	/**
	 * The side of the piece: white or black.
	 */
	public enum Side {
		/**
		 * The lighter colored side of the board.
		 */
		WHITE(1, "White"),
		/**
		 * The darker colored side of the board.
		 */
		BLACK(-1, "Black");

		/**
		 * Multiplier value of this side.
		 */
		private int value;
		private String name;

		/**
		 * Create a new side with given value.
		 *
		 * @param val value of this side
		 */
		private Side(final int val, final String name) {
			value = val;
			this.name = name;
		}

		/**
		 * Get the value of the side.
		 *
		 * @return value of the side
		 */
		public int value() {
			return value;
		}

		@Override
		public String toString() {
			return name;
		}

		/**
		 * Return the opposing side.
		 *
		 * @return the opposing side
		 */
		public Side opposite() {
			if (this == BLACK) {
				return WHITE;
			}
			else {
				return BLACK;
			}
		}
	}

	/**
	 * When creating a piece, you must always choose a side.
	 */
	protected Piece() {
	}

	public Model getModel() {
		return model;
	}

	public Piece(final Side owner, final Model model) {
		side = owner;
		this.model = model;
	}

	/**
	 * Get the moves for this piece.
	 *
	 * @param checkCheck check for check
	 * @return list of moves
	 */
	public MoveList getMoves(boolean checkCheck) {
		return model.getMoves(this, checkCheck);
	}

	/**
	 * Get all moves for this piece that could capture something.
	 *
	 * @return list of moves
	 */
	public MoveListCapture getCapturingMoves() {
		return model.getCapturingMoves(this);
	}
	
	/**
	 * Can this piece be captured by a given attacking piece.
	 */
	public boolean canBeCapturedBy(Piece capturer) {
		return model.canBeCapturedBy(capturer) && !board.isShielded(pos, side);
	}

	/**
	 * Update the piece's current position on the board.
	 *
	 * @param position new position
	 */
	public final void setPosition(final Position position) {
		pos = position;
	}

	/**
	 * Get the position of this piece on the board.
	 *
	 * @return the piece position
	 */
	public final Position getPosition() {
		return pos;
	}

	/**
	 * Set the board for the current piece.
	 *
	 * This is used in determining moves.
	 *
	 * @param surface the current board
	 */
	public final void setBoard(final Board surface) {
		board = surface;
	}

	/**
	 * Get the board set for the current piece.
	 *
	 * @return the piece's board
	 */
	public final Board getBoard() {
		return board;
	}

	/**
	 * Set the side for this piece.
	 *
	 * @param owner side the new side
	 */
	public final void setSide(final Side owner) {
		side = owner;
	}

	/**
	 * Get the side for this piece.
	 *
	 * @return the piece's side
	 */
	public final Side getSide() {
		return side;
	}

	/**
	 * Get the image that represents this piece.
	 *
	 * This method currently uses reflection.
	 *
	 * @return image for this piece
	 */
	public final Image getImage() {
		return model.getImage(side);
	}

	/**
	 * Return true if piece has moved.
	 *
	 * @return true if piece has moved
	 */
	public final Boolean moved() {
		return moved != 0;
	}

	/**
	 * Increase piece movement counter.
	 */
	public final void incMoved() {
		moved++;
	}

	/**
	 * Decrease piece movement counter.
	 */
	public final void decMoved() {
		moved--;
	}

	@Override
	public String toString() {
		return side + " " + model + " (" + pos + ")";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((model == null) ? 0 : model.hashCode());
		result = prime * result + moved;
		result = prime * result + ((pos == null) ? 0 : pos.hashCode());
		result = prime * result + ((side == null) ? 0 : side.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Piece other = (Piece) obj;
		if (model == null) {
			if (other.model != null) return false;
		}
		else if (!model.equals(other.model)) return false;
		if (moved != other.moved) return false;
		if (pos == null) {
			if (other.pos != null) return false;
		}
		else if (!pos.equals(other.pos)) return false;
		if (side != other.side) return false;
		return true;
	}
}
