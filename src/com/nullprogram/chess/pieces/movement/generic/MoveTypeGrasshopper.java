package com.nullprogram.chess.pieces.movement.generic;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.Direction;
import com.nullprogram.chess.Move;
import com.nullprogram.chess.Position;
import com.nullprogram.chess.pieces.Piece;
import com.nullprogram.chess.pieces.movement.IMoveList;
import com.nullprogram.chess.pieces.movement.MoveType;
import com.nullprogram.chess.resources.JSONUtils;

public class MoveTypeGrasshopper extends MoveType {
	private int max;
	//TODO make possible in rook/bishop style
	public MoveTypeGrasshopper(MoveMode mode, DirectionMode directionMode, int max) {
		super(mode, directionMode);
		this.max = max;
	}

	@Override
	public IMoveList getMoves(Piece p, IMoveList list) {
		Position home = p.getPosition();
		for (Position dir : Direction.ALL_POS) {
			if (!dir.match(getDirectionMode(), p)) continue;
			Position pos = home.offset(dir);
			int steps = max;
			// -1 means unlimited, as it can never get to 0
			while (steps != 0 && p.getBoard().inRange(pos)) {
				if (!p.getBoard().isFree(pos)) {
					// Jump over hurdle
					list.add(new Move(home, pos.offset(dir)), getMoveMode());
					break;
				}

				pos = pos.offset(dir);
				steps--;
			}
		}
		return list;
	}

	@Override
	public MoveType create(JsonObject json, MoveMode mode, DirectionMode directionMode, JsonDeserializationContext context) throws JsonParseException {
		return new MoveTypeGrasshopper(mode, directionMode, JSONUtils.getDefaultInt(json, "max", -1));
	}

	@Override
	public String getTypeName() {
		return "Grasshopper";
	}

}
