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

public class MoveTypeLeaperDiagonal extends MoveType {
	private int range;
	
	public MoveTypeLeaperDiagonal(MoveMode mode, DirectionMode directionMode, int range)
	{
		super(mode, directionMode);
		this.range = range;
	}

	@Override
	public IMoveList getMoves(Piece p, IMoveList list) {
        Position pos = p.getPosition();
        int forward = p.getSide().value();
        DirectionMode dir = getDirectionMode();
        
        if (dir.forward() || dir.right()) list.add(new Move(pos, pos.offset(range, forward * range)), getMoveMode());
        if (dir.back() || dir.right()) list.add(new Move(pos, pos.offset(range, -forward * range)), getMoveMode());
        if (dir.forward() || dir.left()) list.add(new Move(pos, pos.offset(-range, forward * range)), getMoveMode());
        if (dir.back() || dir.left()) list.add(new Move(pos, pos.offset(-range, -forward * range)), getMoveMode());
        return list;
	}
	
	@Override
	public MoveType create(JsonObject json, MoveMode mode, DirectionMode directionMode, JsonDeserializationContext context) throws JsonParseException {
		return new MoveTypeLeaperDiagonal(mode, directionMode, JSONUtils.getMandatory(json, "range").getAsInt());
	}

	@Override
	public String getTypeName() {
		return "LeaperDiagonal";
	}

}
