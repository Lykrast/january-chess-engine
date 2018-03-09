package com.nullprogram.chess.pieces.movement.modifier;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.pieces.Piece;
import com.nullprogram.chess.pieces.movement.IMoveList;
import com.nullprogram.chess.pieces.movement.IMoveType;
import com.nullprogram.chess.pieces.movement.MoveListWrapper;
import com.nullprogram.chess.pieces.movement.MoveModifier;

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
