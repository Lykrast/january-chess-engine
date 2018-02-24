package com.nullprogram.chess.pieces;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.IMoveList;
import com.nullprogram.chess.MoveListWrapper;
import com.nullprogram.chess.MoveModifier;
import com.nullprogram.chess.MoveType;
import com.nullprogram.chess.Piece;
import com.nullprogram.chess.resources.JSONUtils;

public class MoveModifierPromotion extends MoveModifier {
	private String promoted;
	private int rows;

	public MoveModifierPromotion(MoveMode mode, MoveType[] moves, String promoted, int rows) {
		super(mode, moves);
		this.promoted = promoted;
		this.rows = rows;
	}

	@Override
	protected MoveListWrapper createWrapper(Piece p, IMoveList list) {
		return new MoveListWrapperPromotion(p, list, promoted, rows);
	}

	@Override
	protected MoveModifier create(JsonObject json, MoveMode mode, MoveType[] moves, JsonDeserializationContext context) throws JsonParseException {
		JsonElement jRows = json.get("rows");
		int rows = 1;
		if (jRows != null)
		{
			rows = jRows.getAsInt();
		}
		return new MoveModifierPromotion(mode, moves, JSONUtils.getMandatory(json, "promoted").getAsString(), rows);
	}
}
