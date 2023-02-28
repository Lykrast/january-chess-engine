package com.nullprogram.chess;

import com.nullprogram.chess.pieces.Piece;
import com.nullprogram.chess.pieces.movement.MoveType.DirectionMode;

/**
 * Represents a position on a Chess board.
 */
public final class Position implements Comparable<Position> {

	/**
	 * The board coordinates of this position, starting at the white
	 * queenside rook.
	 */
	public final int x, y;

	/**
	 * Create a new position with given coordinates.
	 *
	 * @param posX x coordinate
	 * @param posY y coordinate
	 */
	public Position(final int posX, final int posY) {
		x = posX;
		y = posY;
	}

	/**
	 * Create a new position from this one, using the coordinates of the offset as
	 * changes.
	 * 
	 * @param offset change in x and y
	 * @return new position with coordinates of the original offset by those of the
	 *         offset
	 */
	public Position offset(final Position offset) {
		return new Position(x + offset.x, y + offset.y);
	}

	/**
	 * Create a new position from this one.
	 * 
	 * @param deltax change in x
	 * @param deltay change in y
	 */
	public Position offset(final int deltax, final int deltay) {
		return new Position(x + deltax, y + deltay);
	}

	/**
	 * Convert the position to a string.
	 *
	 * @return string form of position
	 */
	@Override
	public String toString() {
		return "" + ((char) ('a' + (char) x)) + (y + 1);
	}

	/**
	 * Check if Position objects are equal.
	 *
	 * @param pos position to be compared
	 * @return true if the positions are equal
	 */
	public boolean equals(final Position pos) {
		return (pos != null) && (pos.x == x) && (pos.y == y);
	}

	/**
	 * Check if object is equal to this Position.
	 *
	 * @param o position to be compared
	 * @return true if the positions are equal
	 */
	@Override
	public boolean equals(final Object o) {
		if (this == o) { return true; }
		if (!(o instanceof Position)) { return false; }
		return equals((Position) o);
	}

	/**
	 * Check if this Position goes in the specified direction (if used as an
	 * offset).
	 * 
	 * @param mode  DirectionMode to check against
	 * @param piece Piece to check for, for the forward and back directions
	 * @return true if this goes according to the mode, false otherwise
	 */
	public boolean match(DirectionMode mode, Piece piece) {
		return match(mode, piece.getSide());
	}

	/**
	 * Check if this Position goes in the specified direction (if used as an
	 * offset).
	 * 
	 * @param mode DirectionMode to check against
	 * @param side Side to check for, for the forward and back directions
	 * @return true if this goes according to the mode, false otherwise
	 */
	public boolean match(DirectionMode mode, Piece.Side side) {
		if (mode.left() && x < 0) return true;
		if (mode.right() && x > 0) return true;

		int adjusted = y * side.value();
		if (mode.forward() && adjusted > 0) return true;
		if (mode.back() && adjusted < 0) return true;

		return false;
	}

	/**
	 * Hash code of this object.
	 *
	 * @return hash code of this object.
	 */
	@Override
	public int hashCode() {
		return x ^ y;
	}

	/**
	 * Compare two Position objects.
	 *
	 * @param pos position to be compared
	 * @return positive if more, negative if less, zero if equal
	 */
	@Override
	public int compareTo(final Position pos) {
		if (pos.y == y) {
			return x - pos.x;
		}
		else {
			return y - pos.y;
		}
	}
}
