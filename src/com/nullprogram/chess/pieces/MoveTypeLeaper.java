package com.nullprogram.chess.pieces;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.Move;
import com.nullprogram.chess.MoveList;
import com.nullprogram.chess.MoveType;
import com.nullprogram.chess.Piece;
import com.nullprogram.chess.Position;
import com.nullprogram.chess.resources.JSONUtils;

public class MoveTypeLeaper extends MoveType {
	private int near, far;
	
	public MoveTypeLeaper(MoveMode mode, int near, int far)
	{
		super(mode);
		this.near = near;
		this.far = far;
	}

	@Override
	public MoveList getMoves(Piece p, MoveList list) {
        Position pos = p.getPosition();
        list.add(new Move(pos, new Position(pos,  near,  far)), getMoveMode());
        list.add(new Move(pos, new Position(pos,  far,  near)), getMoveMode());
        list.add(new Move(pos, new Position(pos, -far,  near)), getMoveMode());
        list.add(new Move(pos, new Position(pos, -far, -near)), getMoveMode());
        list.add(new Move(pos, new Position(pos,  far, -near)), getMoveMode());
        list.add(new Move(pos, new Position(pos,  near, -far)), getMoveMode());
        list.add(new Move(pos, new Position(pos, -near, -far)), getMoveMode());
        list.add(new Move(pos, new Position(pos, -near,  far)), getMoveMode());
        return list;
	}
	
	@Override
	public MoveType create(JsonObject json, MoveMode mode) throws JsonParseException {
		return new MoveTypeLeaper(mode, JSONUtils.getMandatory(json, "near").getAsInt(), JSONUtils.getMandatory(json, "far").getAsInt());
	}

}
