package com.nullprogram.chess.pieces;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.IMoveList;
import com.nullprogram.chess.IMoveType;
import com.nullprogram.chess.MoveListWrapper;
import com.nullprogram.chess.MoveModifier;
import com.nullprogram.chess.Piece;

public class MoveModifierCaptureNonRoyal extends MoveModifier {
	public MoveModifierCaptureNonRoyal(IMoveType[] moves) {
		super(moves);
	}

	@Override
	protected MoveListWrapper createWrapper(Piece p, IMoveList list) {
		return new MoveListWrapperCaptureNonRoyal(p, list);
	}

	@Override
	protected MoveModifier create(JsonObject json, IMoveType[] moves, JsonDeserializationContext context) throws JsonParseException {
		return new MoveModifierCaptureNonRoyal(moves);
	}

	@Override
	public String getTypeName() {
		return "ModCaptureNonRoyal";
	}
}