package com.nullprogram.chess.pieces.movement.generic;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.Move;
import com.nullprogram.chess.Position;
import com.nullprogram.chess.pieces.Piece;
import com.nullprogram.chess.pieces.movement.IMoveList;
import com.nullprogram.chess.pieces.movement.MoveType;
import com.nullprogram.chess.resources.JSONUtils;

public abstract class MoveTypeRiderAbstract extends MoveType {
	protected int max;

	public MoveTypeRiderAbstract(MoveMode mode, DirectionMode directionMode, int max) {
		super(mode, directionMode);
		this.max = max;
	}

	@Override
	public IMoveList getMoves(Piece p, IMoveList list) {
		Position start = p.getPosition();

		for (Position dir : getDirections()) {
			if (!dir.match(getDirectionMode(), p)) continue;
			Position pos = start.offset(dir);
			int steps = max;
			// -1 means unlimited, as it can never get to 0
			while (steps != 0 && list.add(new Move(start, pos), getMoveMode()) && p.getBoard().isFree(pos)) {
				steps--;
				pos = pos.offset(dir);
			}
		}

		return list;
	}

	/**
	 * The directions this abstract rider can ride to.
	 * 
	 * @return array of positions used as directions
	 */
	protected abstract Position[] getDirections();

	@Override
	public MoveType create(JsonObject json, MoveMode moveMode, DirectionMode directionMode, JsonDeserializationContext context) throws JsonParseException {
		return create(json, moveMode, directionMode, JSONUtils.getDefaultInt(json, "max", -1), context);
	}

	/**
	 * Creates this MoveTypeRiderAbstract according to the given JsonElement
	 * following the (already deserialized)
	 * MoveMode, DirectionMode and max distance.
	 */
	protected abstract MoveType create(JsonObject json, MoveMode moveMode, DirectionMode directionMode, int max,
			JsonDeserializationContext context) throws JsonParseException;

}
