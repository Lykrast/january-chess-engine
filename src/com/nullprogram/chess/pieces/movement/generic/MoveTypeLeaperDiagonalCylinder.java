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

public class MoveTypeLeaperDiagonalCylinder extends MoveType {
	private int range;
	private Wrap wrap;

	public MoveTypeLeaperDiagonalCylinder(MoveMode mode, DirectionMode directionMode, int range, Wrap wrap) {
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

		if (dir.forward() || dir.right()) list.add(new Move(pos, wrap.wrap(b, pos.offset(range, forward * range))), getMoveMode());
		if (dir.back() || dir.right()) list.add(new Move(pos, wrap.wrap(b, pos.offset(range, -forward * range))), getMoveMode());
		if (dir.forward() || dir.left()) list.add(new Move(pos, wrap.wrap(b, pos.offset(-range, forward * range))), getMoveMode());
		if (dir.back() || dir.left()) list.add(new Move(pos, wrap.wrap(b, pos.offset(-range, -forward * range))), getMoveMode());
		return list;
	}

	@Override
	public MoveType create(JsonObject json, MoveMode mode, DirectionMode directionMode, JsonDeserializationContext context) throws JsonParseException {
		return new MoveTypeLeaperDiagonalCylinder(mode, directionMode, JSONUtils.getMandatory(json, "range").getAsInt(), Wrap.fromJson(json));
	}

	@Override
	public String getTypeName() {
		return "LeaperDiagonalCylinder";
	}

}
