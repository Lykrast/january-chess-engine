package com.nullprogram.chess.pieces.movement.modifier;

import java.util.List;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.Move;
import com.nullprogram.chess.Position;
import com.nullprogram.chess.pieces.Piece;
import com.nullprogram.chess.pieces.movement.IMoveList;
import com.nullprogram.chess.pieces.movement.IMoveType;
import com.nullprogram.chess.pieces.movement.MoveListWrapper;
import com.nullprogram.chess.pieces.movement.MoveModifier;

public class MoveModifierCoordinator extends MoveModifier {

	public MoveModifierCoordinator(IMoveType[] moves) {
		super(moves);
	}

	@Override
	protected MoveListWrapper createWrapper(Piece p, IMoveList list) {
		return new Wrapper(p, list);
	}

	@Override
	protected MoveModifier create(JsonObject json, IMoveType[] moves, JsonDeserializationContext context) throws JsonParseException {
		return new MoveModifierCoordinator(moves);
	}

	@Override
	public String getTypeName() {
		return "ModCoordinator";
	}

	public static class Wrapper extends MoveListWrapper {
		// A new one wrapper is created per move calculation, so we only need to find the royal piece once
		private List<Position> royal;

		public Wrapper(Piece piece, IMoveList list) {
			super(piece, list);
			royal = piece.getBoard().findRoyal(piece.getSide());
		}

		@Override
		protected Move modify(Move move) {
			// No royal piece to coordinate with
			if (royal.isEmpty()) return move;

			// Add at the end of the current move
			Move innermost = move.getLast();

			// Coordinate
			// Assume the end is where our piece ends up, not always true but for what I do it'll work
			for (Position p : royal) innermost = coordinate(innermost, innermost.destination, p);

			return move;
		}

		/**
		 * Append the coordination captures of a given king if possible. Returns either
		 * the move or the added move that is now the deepest in the sequence.
		 */
		private Move coordinate(Move move, Position dest, Position king) {
			move = coordinate(move, dest.x, king.y);
			move = coordinate(move, king.x, dest.y);

			return move;
		}

		/**
		 * Append a single coordination capture if possible. Returns either the move or
		 * the added move that is now the deepest in the sequence.
		 */
		private Move coordinate(Move move, int x, int y) {
			Position target = new Position(x, y);
			if (piece.getBoard().isEnemy(target, piece.getSide())) {
				Move coordination = new Move(target, null);
				move.setNext(coordination);
				return coordination;
			}
			else return move;
		}

	}
}
