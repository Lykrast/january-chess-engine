package com.nullprogram.chess.pieces;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.Move;
import com.nullprogram.chess.MoveList;
import com.nullprogram.chess.MoveType;
import com.nullprogram.chess.Piece;
import com.nullprogram.chess.Position;
import com.nullprogram.chess.resources.JSONUtils;

public class MoveTypeLeaperOrthogonal extends MoveType {
	private int range;
	
	public MoveTypeLeaperOrthogonal(MoveMode mode, int range)
	{
		super(mode);
		this.range = range;
	}

	@Override
	public MoveList getMoves(Piece p, MoveList list) {
        Position pos = p.getPosition();
        list.add(new Move(pos, new Position(pos,  0,  range)), getMoveMode());
        list.add(new Move(pos, new Position(pos,  0,  -range)), getMoveMode());
        list.add(new Move(pos, new Position(pos,  range,  0)), getMoveMode());
        list.add(new Move(pos, new Position(pos,  -range,  0)), getMoveMode());
        return list;
	}
	
	@Override
	public MoveType create(JsonObject json, MoveMode mode) throws JsonParseException {
		return new MoveTypeLeaperOrthogonal(mode, JSONUtils.getMandatory(json, "range").getAsInt());
	}

}
