package com.nullprogram.chess.pieces.movement.generic;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.Move;
import com.nullprogram.chess.Position;
import com.nullprogram.chess.boards.Board;
import com.nullprogram.chess.pieces.Piece;
import com.nullprogram.chess.pieces.movement.IMoveList;
import com.nullprogram.chess.pieces.movement.MoveType;
import com.nullprogram.chess.util.Wrap;

public abstract class MoveTypeRiderCylinderAbstract extends MoveType {
	protected int max;
	private Wrap wrap;

	public MoveTypeRiderCylinderAbstract(MoveMode mode, DirectionMode directionMode, int max, Wrap wrap) {
		super(mode, directionMode);
		this.max = max;
		this.wrap = wrap;
	}

	@Override
	public IMoveList getMoves(Piece p, IMoveList list) {
		Position start = p.getPosition();
		Board b = p.getBoard();

		for (Position dir : getDirections()) {
			if (!dir.match(getDirectionMode(), p)) continue;
			Position pos = wrap.wrap(b, start.offset(dir));
			int steps = max;
			// -1 means unlimited, as it can never get to 0
			while (steps != 0 && list.add(new Move(start, pos), getMoveMode()) && p.getBoard().isFree(pos)) {
				steps--;
				pos = wrap.wrap(b, pos.offset(dir));
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
		JsonElement tmp = json.get("max");
		int max = -1;
		if (tmp != null) {
			max = tmp.getAsInt();
		}

		return create(json, moveMode, directionMode, max, Wrap.fromJson(json), context);
	}

	/**
	 * Creates this MoveTypeRiderAbstract according to the given JsonElement
	 * following the (already deserialized) MoveMode, DirectionMode, max and Wrap
	 * distance.
	 */
	protected abstract MoveType create(JsonObject json, MoveMode moveMode, DirectionMode directionMode, int max, Wrap wrap, JsonDeserializationContext context) throws JsonParseException;

}
