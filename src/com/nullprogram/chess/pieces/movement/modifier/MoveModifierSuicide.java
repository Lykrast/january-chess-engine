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

public class MoveModifierSuicide extends MoveModifier {
	public MoveModifierSuicide(IMoveType[] moves) {
		super(moves);
	}

	@Override
	protected MoveListWrapper createWrapper(Piece p, IMoveList list) {
		return new Wrapper(p, list);
	}

	@Override
	protected MoveModifier create(JsonObject json, IMoveType[] moves, JsonDeserializationContext context) throws JsonParseException {
		return new MoveModifierSuicide(moves);
	}

	@Override
	public String getTypeName() {
		return "ModSuicide";
	}

	public static class Wrapper extends MoveListWrapper {
		public Wrapper(Piece piece, IMoveList list) {
			super(piece, list);
		}

		@Override
		protected Move modify(Move move) {
			//Remove the piece if it moved to a non empty location (capture)
			//Assume the piece is where the initial move ended
			Position dest = move.destination;
			if (piece.getBoard().inRange(dest) && !piece.getBoard().isEmpty(dest)) move.getLast().setNext(new Move(dest, null));

			return move;
		}

	}
}
