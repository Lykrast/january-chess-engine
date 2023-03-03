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

public class MoveModifierNemesis extends MoveModifier {
	public MoveModifierNemesis(IMoveType[] moves) {
		super(moves);
	}

	@Override
	protected MoveListWrapper createWrapper(Piece p, IMoveList list) {
		return new Wrapper(p, list);
	}

	@Override
	public String getTypeName() {
		return "ModNemesis";
	}

	@Override
	protected MoveModifier create(JsonObject json, IMoveType[] moves, JsonDeserializationContext context) throws JsonParseException {
		return new MoveModifierNemesis(moves);
	}

	public static class Wrapper extends MoveListWrapper {
		private List<Position> royals;

		public Wrapper(Piece piece, IMoveList list) {
			super(piece, list);
			royals = piece.getBoard().findRoyal(piece.getSide().opposite());
		}

		@Override
		protected Move modify(Move move) {
			Position start = move.origin;
			Position dest = move.destination;
			for (Position roy : royals) {
				//Must reduce manhattan distance
				if (Math.abs(start.x - roy.x) + Math.abs(start.y - roy.y) > Math.abs(dest.x - roy.x) + Math.abs(dest.y - roy.y)) return move;
			}
			return null;
		}

	}

}
