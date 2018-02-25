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

public class MoveTypeLeaperDiagonal extends MoveType {
	private int range;
	
	public MoveTypeLeaperDiagonal(MoveMode mode, int range)
	{
		super(mode);
		this.range = range;
	}

	@Override
	public IMoveList getMoves(Piece p, IMoveList list) {
        Position pos = p.getPosition();
        list.add(new Move(pos, pos.offset(range,  range)), getMoveMode());
        list.add(new Move(pos, pos.offset(range,  -range)), getMoveMode());
        list.add(new Move(pos, pos.offset(-range,  range)), getMoveMode());
        list.add(new Move(pos, pos.offset(-range,  -range)), getMoveMode());
        return list;
	}
	
	@Override
	public MoveType create(JsonObject json, MoveMode mode, JsonDeserializationContext context) throws JsonParseException {
		return new MoveTypeLeaperDiagonal(mode, JSONUtils.getMandatory(json, "range").getAsInt());
	}

	@Override
	public String getTypeName() {
		return "LeaperDiagonal";
	}

}
