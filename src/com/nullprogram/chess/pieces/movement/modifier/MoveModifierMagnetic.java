package com.nullprogram.chess.pieces.movement.modifier;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.pieces.Piece;
import com.nullprogram.chess.pieces.movement.IMoveList;
import com.nullprogram.chess.pieces.movement.IMoveType;
import com.nullprogram.chess.pieces.movement.MoveListWrapper;
import com.nullprogram.chess.pieces.movement.MoveModifier;
import com.nullprogram.chess.pieces.movement.modifier.MoveListWrapperMagnetic.Behavior;
import com.nullprogram.chess.resources.JSONUtils;

public class MoveModifierMagnetic extends MoveModifier {
	private Behavior friendly, enemy;
	private boolean affectRoyal;
	
	public MoveModifierMagnetic(IMoveType[] moves, Behavior friendly, Behavior enemy, boolean affectRoyal) {
		super(moves);
		
		this.friendly = friendly;
		this.enemy = enemy;
		this.affectRoyal = affectRoyal;
	}

	@Override
	protected MoveListWrapper createWrapper(Piece p, IMoveList list) {
		return new MoveListWrapperMagnetic(p, list, friendly, enemy, affectRoyal);
	}

	@Override
	protected MoveModifier create(JsonObject json, IMoveType[] moves, JsonDeserializationContext context) throws JsonParseException {
		return new MoveModifierMagnetic(moves, Behavior.fromJson(json.get("friendly")), Behavior.fromJson(json.get("enemy")), JSONUtils.getDefaultBoolean(json, "affectroyal", true));
	}

	@Override
	public String getTypeName() {
		return "ModMagnetic";
	}
}
