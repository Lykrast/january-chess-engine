package com.nullprogram.chess.pieces;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.IMoveType;
import com.nullprogram.chess.MoveCondition;
import com.nullprogram.chess.Piece;

public class MoveConditionVirgin extends MoveCondition {

	public MoveConditionVirgin(IMoveType[] movesTrue, IMoveType[] movesFalse) {
		super(movesTrue, movesFalse);
	}

	@Override
	protected boolean evaluate(Piece p) {
		return !p.moved();
	}

	@Override
	protected MoveCondition create(JsonObject json, IMoveType[] movesTrue, IMoveType[] movesFalse, JsonDeserializationContext context) throws JsonParseException {
		return new MoveConditionVirgin(movesTrue, movesFalse);
	}

	@Override
	public String getTypeName() {
		return "CondVirgin";
	}
}
