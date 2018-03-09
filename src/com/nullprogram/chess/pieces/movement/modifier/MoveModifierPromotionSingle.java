package com.nullprogram.chess.pieces.movement.modifier;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.pieces.Piece;
import com.nullprogram.chess.pieces.movement.IMoveList;
import com.nullprogram.chess.pieces.movement.IMoveType;
import com.nullprogram.chess.pieces.movement.MoveListWrapper;
import com.nullprogram.chess.pieces.movement.MoveModifier;
import com.nullprogram.chess.resources.JSONUtils;

public class MoveModifierPromotionSingle extends MoveModifier {
	private String promoted;
	private int rows;

	public MoveModifierPromotionSingle(IMoveType[] moves, String promoted, int rows) {
		super(moves);
		this.promoted = promoted;
		this.rows = rows;
	}

	@Override
	protected MoveListWrapper createWrapper(Piece p, IMoveList list) {
		return new MoveListWrapperPromotionSingle(p, list, promoted, rows);
	}

	@Override
	protected MoveModifier create(JsonObject json, IMoveType[] moves, JsonDeserializationContext context) throws JsonParseException {
		JsonElement jRows = json.get("rows");
		int rows = 1;
		if (jRows != null)
		{
			rows = jRows.getAsInt();
		}
		return new MoveModifierPromotionSingle(moves, JSONUtils.getMandatory(json, "promoted").getAsString(), rows);
	}

	@Override
	public String getTypeName() {
		return "ModPromotionSingle";
	}
}
