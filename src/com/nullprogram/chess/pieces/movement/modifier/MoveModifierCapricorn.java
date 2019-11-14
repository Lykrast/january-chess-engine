package com.nullprogram.chess.pieces.movement.modifier;

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

public class MoveModifierCapricorn extends MoveModifier {

	public MoveModifierCapricorn(IMoveType[] moves) {
		super(moves);
	}

	@Override
	protected MoveListWrapper createWrapper(Piece p, IMoveList list) {
		return new Wrapper(p, list);
	}

	@Override
	protected MoveModifier create(JsonObject json, IMoveType[] moves, JsonDeserializationContext context) throws JsonParseException {
		return new MoveModifierCapricorn(moves);
	}

	@Override
	public String getTypeName() {
		return "ModCapricorn";
	}

	public static class Wrapper extends MoveListWrapper {
		public Wrapper(Piece piece, IMoveList list) {
			super(piece, list);
		}

		@Override
		protected Move modify(Move move) {
			// Add at the end of the current move
			Move innermost = move.getLast();

			// Coordinate
			Position dest = move.getDest();
			for (int x = -1; x <= 1; x++) {
				for (int y = -1; y <= 1; y++) {
					if (x == 0 && y == 0) continue;
					innermost = capture(innermost, dest, x, y);
				}
			}

			return move;
		}

		/**
		 * Append the adjacent capture if possible. Returns either the move or the added
		 * move that is now the deepest in the sequence.
		 */
		private Move capture(Move move, Position start, int x, int y) {
			Position target = start.offset(x, y);
			if (piece.getBoard().isEnemy(target, piece.getSide())) {
				Move capture = new Move(target, null);
				move.setNext(capture);
				return capture;
			}
			else return move;
		}

	}
}
