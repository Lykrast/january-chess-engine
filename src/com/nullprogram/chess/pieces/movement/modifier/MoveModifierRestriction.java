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
import com.nullprogram.chess.util.AreaSided;

public class MoveModifierRestriction extends MoveModifier {
	private AreaSided area;
	private boolean invert;

	public MoveModifierRestriction(IMoveType[] moves, AreaSided area, boolean invert) {
		super(moves);
		this.area = area;
		this.invert = invert;
	}

	@Override
	protected MoveListWrapper createWrapper(Piece p, IMoveList list) {
		return new MoveListWrapperRestriction(p, list, area, invert);
	}

	@Override
	public String getTypeName() {
		return "ModRestriction";
	}

	@Override
	protected MoveModifier create(JsonObject json, IMoveType[] moves, JsonDeserializationContext context) throws JsonParseException {	
		JsonElement tmp = json.get("invert");
		boolean invert = false;
		if (tmp != null)
		{
			invert = tmp.getAsBoolean();
		}
		
		return new MoveModifierRestriction(moves, AreaSided.fromJson(json), invert);
	}
}
