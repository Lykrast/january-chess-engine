package com.nullprogram.chess.pieces;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.Move;
import com.nullprogram.chess.MoveList;
import com.nullprogram.chess.MoveType;
import com.nullprogram.chess.Piece;
import com.nullprogram.chess.Position;

public class MoveTypeLeaperDiagonal extends MoveType {
	private int range;
	
	public MoveTypeLeaperDiagonal(MoveMode mode, int range)
	{
		super(mode);
		this.range = range;
	}

	@Override
	public MoveList getMoves(Piece p, MoveList list) {
        Position pos = p.getPosition();
        list.add(new Move(pos, new Position(pos,  range,  range)), getMoveMode());
        list.add(new Move(pos, new Position(pos,  range,  -range)), getMoveMode());
        list.add(new Move(pos, new Position(pos,  -range,  range)), getMoveMode());
        list.add(new Move(pos, new Position(pos,  -range,  -range)), getMoveMode());
        return list;
	}
	
	@Override
	public MoveType create(JsonObject json, MoveMode mode) throws JsonParseException {
		return new MoveTypeLeaperDiagonal(mode, json.get("range").getAsInt());
	}

}
