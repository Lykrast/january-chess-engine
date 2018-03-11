package com.nullprogram.chess.pieces.movement.modifier;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
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
		return new MoveListWrapperPromotionSingle(p, list, promoted, area);
	}

	@Override
	protected MoveModifier create(JsonObject json, IMoveType[] moves, JsonDeserializationContext context) throws JsonParseException {
		return new MoveModifierPromotionSingle(moves, JSONUtils.getMandatory(json, "promoted").getAsString(), AreaSided.fromJson(json, true));
	}

	@Override
	public String getTypeName() {
		return "ModPromotionSingle";
	}
}
