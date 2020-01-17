package com.nullprogram.chess.pieces.movement.generic;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.Move;
import com.nullprogram.chess.Position;
import com.nullprogram.chess.pieces.Piece;
import com.nullprogram.chess.pieces.movement.IMoveList;
import com.nullprogram.chess.pieces.movement.MoveType;

public class MoveTypeTeleport extends MoveType {

	public MoveTypeTeleport(MoveMode mode, DirectionMode directionMode) {
		super(mode, directionMode);
	}

	@Override
	public IMoveList getMoves(Piece p, IMoveList list) {
		Position pos = p.getPosition();
		int px = pos.getX(), py = pos.getY();

		for (int x = 0; x < p.getBoard().getWidth(); x++) {
			for (int y = 0; y < p.getBoard().getHeight(); y++) {
				if (x != px && y != py) list.add(new Move(pos, new Position(x, y)), getMoveMode());
			}
		}

		return list;
	}

	@Override
	public MoveType create(JsonObject json, MoveMode mode, DirectionMode directionMode, JsonDeserializationContext context) throws JsonParseException {
		return new MoveTypeTeleport(mode, directionMode);
	}

	@Override
	public String getTypeName() {
		return "Teleport";
	}

}
