package com.nullprogram.chess.pieces.movement.modifier;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.Move;
import com.nullprogram.chess.pieces.Piece;
import com.nullprogram.chess.pieces.movement.IMoveList;
import com.nullprogram.chess.pieces.movement.IMoveType;
import com.nullprogram.chess.pieces.movement.MoveListWrapper;
import com.nullprogram.chess.pieces.movement.MoveModifier;
import com.nullprogram.chess.resources.JSONUtils;
import com.nullprogram.chess.util.AreaSided;

public class MoveModifierPromotionSingle extends MoveModifier {
	private String promoted;
	private AreaSided area;

	public MoveModifierPromotionSingle(IMoveType[] moves, String promoted, AreaSided area) {
		super(moves);
		this.promoted = promoted;
		this.area = area;
	}

	@Override
	protected MoveListWrapper createWrapper(Piece p, IMoveList list) {
		return new Wrapper(p, list, promoted, area);
	}

	@Override
	protected MoveModifier create(JsonObject json, IMoveType[] moves, JsonDeserializationContext context) throws JsonParseException {
		return new MoveModifierPromotionSingle(moves, JSONUtils.getMandatory(json, "promoted").getAsString(), AreaSided.fromJson(json, true));
	}

	@Override
	public String getTypeName() {
		return "ModPromotionSingle";
	}

	public static class Wrapper extends MoveListWrapper {
		private String promoted;
		private AreaSided area;

		public Wrapper(Piece piece, IMoveList list, String promoted, AreaSided area) {
			super(piece, list);
			this.promoted = promoted;
			this.area = area;
		}

		@Override
		protected Move modify(Move move) {
			if (!area.inside(move.destination, piece.getBoard(), piece.getSide())) return move;

			// Add at the end of the current move
			Move innermost = move.getLast();

			innermost.setNext(new Move(move.destination, null)); // remove the piece
			Move upgrade = new Move(null, move.destination);
			upgrade.setReplacement(promoted); // put the replacement
			upgrade.setReplacementSide(piece.getSide());
			innermost.getNext().setNext(upgrade);

			return move;
		}

	}
}
