package com.nullprogram.chess.pieces.movement.modifier;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.pieces.Piece;
import com.nullprogram.chess.pieces.movement.IMoveList;
import com.nullprogram.chess.pieces.movement.IMoveType;
import com.nullprogram.chess.pieces.movement.MoveListWrapper;
import com.nullprogram.chess.pieces.movement.MoveModifier;

public class MoveModifierSwap extends MoveModifier {

	public MoveModifierSwap(IMoveType[] moves) {
		super(moves);
	}

	@Override
	protected MoveListWrapper createWrapper(Piece p, IMoveList list) {
		return new MoveListWrapperSwap(p, list);
	}

	@Override
	protected MoveModifier create(JsonObject json, IMoveType[] moves, JsonDeserializationContext context) throws JsonParseException {
		return new MoveModifierSwap(moves);
	}

	@Override
	public String getTypeName() {
		return "ModSwap";
	}
}
