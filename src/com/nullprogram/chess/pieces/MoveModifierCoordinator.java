package com.nullprogram.chess.pieces;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.IMoveList;
import com.nullprogram.chess.IMoveType;
import com.nullprogram.chess.MoveListWrapper;
import com.nullprogram.chess.MoveModifier;
import com.nullprogram.chess.Piece;

public class MoveModifierCoordinator extends MoveModifier {

	public MoveModifierCoordinator(IMoveType[] moves) {
		super(moves);
	}

	@Override
	protected MoveListWrapper createWrapper(Piece p, IMoveList list) {
		return new MoveListWrapperCoordinator(p, list);
	}

	@Override
	protected MoveModifier create(JsonObject json, IMoveType[] moves, JsonDeserializationContext context) throws JsonParseException {
		return new MoveModifierCoordinator(moves);
	}

	@Override
	public String getTypeName() {
		return "ModCoordinator";
	}
}
