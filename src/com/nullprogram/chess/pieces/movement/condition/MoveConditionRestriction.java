package com.nullprogram.chess.pieces.movement.condition;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.pieces.Piece;
import com.nullprogram.chess.pieces.movement.IMoveType;
import com.nullprogram.chess.pieces.movement.MoveCondition;
import com.nullprogram.chess.util.AreaSided;

public class MoveConditionRestriction extends MoveCondition {
	private AreaSided area;

	public MoveConditionRestriction(IMoveType[] movesTrue, IMoveType[] movesFalse, AreaSided area) {
		super(movesTrue, movesFalse);
		this.area = area;
	}

	@Override
	protected boolean evaluate(Piece p) {
		return area.inside(p);
	}

	@Override
	protected MoveCondition create(JsonObject json, IMoveType[] movesTrue, IMoveType[] movesFalse, JsonDeserializationContext context) throws JsonParseException {	
		return new MoveConditionRestriction(movesTrue, movesFalse, AreaSided.fromJson(json));
	}

	@Override
	public String getTypeName() {
		return "CondRestriction";
	}
}
