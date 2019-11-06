package com.nullprogram.chess.pieces.movement.generic;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.Move;
import com.nullprogram.chess.Position;
import com.nullprogram.chess.boards.Board;
import com.nullprogram.chess.pieces.Piece;
import com.nullprogram.chess.pieces.movement.IMoveList;
import com.nullprogram.chess.pieces.movement.MoveType;
import com.nullprogram.chess.resources.JSONUtils;
import com.nullprogram.chess.util.Wrap;

public class MoveTypeLeaperOrthogonalCylinder extends MoveType {
	private int range;
	private Wrap wrap;

	public MoveTypeLeaperOrthogonalCylinder(MoveMode mode, DirectionMode directionMode, int range, Wrap wrap) {
		super(mode, directionMode);
		this.range = range;
		this.wrap = wrap;
	}

	@Override
	public IMoveList getMoves(Piece p, IMoveList list) {
		Position pos = p.getPosition();
		Board b = p.getBoard();
		int forward = p.getSide().value();
		DirectionMode dir = getDirectionMode();

		if (dir.forward()) list.add(new Move(pos, wrap.wrap(b, pos.offset(0, forward * range))), getMoveMode());
		if (dir.back()) list.add(new Move(pos, wrap.wrap(b, pos.offset(0, -forward * range))), getMoveMode());
		if (dir.right()) list.add(new Move(pos, wrap.wrap(b, pos.offset(range, 0))), getMoveMode());
		if (dir.left()) list.add(new Move(pos, wrap.wrap(b, pos.offset(-range, 0))), getMoveMode());
		return list;
	}

	@Override
	public MoveType create(JsonObject json, MoveMode mode, DirectionMode directionMode, JsonDeserializationContext context) throws JsonParseException {
		return new MoveTypeLeaperOrthogonalCylinder(mode, directionMode, JSONUtils.getMandatory(json, "range").getAsInt(), Wrap.fromJson(json));
	}

	@Override
	public String getTypeName() {
		return "LeaperOrthogonalCylinder";
	}

}
