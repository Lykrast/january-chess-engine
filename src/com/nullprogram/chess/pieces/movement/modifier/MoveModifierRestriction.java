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
import com.nullprogram.chess.util.AreaSided;

public class MoveModifierRestriction extends MoveModifier {
	private AreaSided area;

	public MoveModifierRestriction(IMoveType[] moves, AreaSided area) {
		super(moves);
		this.area = area;
	}

	@Override
	protected MoveListWrapper createWrapper(Piece p, IMoveList list) {
		return new Wrapper(p, list, area);
	}

	@Override
	public String getTypeName() {
		return "ModRestriction";
	}

	@Override
	protected MoveModifier create(JsonObject json, IMoveType[] moves, JsonDeserializationContext context) throws JsonParseException {
		return new MoveModifierRestriction(moves, AreaSided.fromJson(json));
	}

	public static class Wrapper extends MoveListWrapper {
		private AreaSided area;

		public Wrapper(Piece piece, IMoveList list, AreaSided area) {
			super(piece, list);
			this.area = area;
		}

		@Override
		protected Move modify(Move move) {
			Position dest = move.getDest();
			if (area.inside(dest, piece.getBoard(), piece.getSide())) return move;
			else return null;
		}

	}

}
