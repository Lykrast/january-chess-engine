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

public class MoveModifierCaptureRoyal extends MoveModifier {
	public MoveModifierCaptureRoyal(IMoveType[] moves) {
		super(moves);
	}

	@Override
	protected MoveListWrapper createWrapper(Piece p, IMoveList list) {
		return new Wrapper(p, list);
	}

	@Override
	protected MoveModifier create(JsonObject json, IMoveType[] moves, JsonDeserializationContext context) throws JsonParseException {
		return new MoveModifierCaptureRoyal(moves);
	}

	@Override
	public String getTypeName() {
		return "ModCaptureRoyal";
	}

	public static class Wrapper extends MoveListWrapper {
		public Wrapper(Piece piece, IMoveList list) {
			super(piece, list);
		}

		@Override
		protected Move modify(Move move) {
			// Cannot capture a non royal piece
			Position dest = move.destination;
			if (piece.getBoard().inRange(dest)) {
				Piece target = piece.getBoard().getPiece(dest);
				if (target != null && !target.getModel().isRoyal()) return null;
			}
			return move;
		}

	}
}
