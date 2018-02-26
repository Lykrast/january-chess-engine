package com.nullprogram.chess.pieces;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.IMoveList;
import com.nullprogram.chess.Move;
import com.nullprogram.chess.MoveType;
import com.nullprogram.chess.Piece;
import com.nullprogram.chess.Position;
import com.nullprogram.chess.resources.JSONUtils;

public class MoveTypeLeaperOrthogonal extends MoveType {
	private int range;
	
	public MoveTypeLeaperOrthogonal(MoveMode mode, DirectionMode directionMode, int range)
	{
		super(mode, directionMode);
		this.range = range;
	}

	@Override
	public IMoveList getMoves(Piece p, IMoveList list) {
        Position pos = p.getPosition();
        int forward = p.getSide().value();
        DirectionMode dir = getDirectionMode();
        
        if (dir.forward()) list.add(new Move(pos, pos.offset(0, forward * range)), getMoveMode());
        if (dir.back()) list.add(new Move(pos, pos.offset(0, -forward * range)), getMoveMode());
        if (dir.right()) list.add(new Move(pos, pos.offset(range, 0)), getMoveMode());
        if (dir.left()) list.add(new Move(pos, pos.offset(-range, 0)), getMoveMode());
        return list;
	}
	
	@Override
	public MoveType create(JsonObject json, MoveMode mode, DirectionMode directionMode, JsonDeserializationContext context) throws JsonParseException {
		return new MoveTypeLeaperOrthogonal(mode, directionMode, JSONUtils.getMandatory(json, "range").getAsInt());
	}

	@Override
	public String getTypeName() {
		return "LeaperOrthogonal";
	}

}
