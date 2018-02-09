package com.nullprogram.chess.pieces;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.Move;
import com.nullprogram.chess.MoveList;
import com.nullprogram.chess.MoveType;
import com.nullprogram.chess.Piece;
import com.nullprogram.chess.Position;

public class MoveTypeLeaper extends MoveType {
	private int near, far;
	
	public MoveTypeLeaper(int near, int far)
	{
		this.near = near;
		this.far = far;
	}

	@Override
	public MoveList getMoves(Piece p, MoveList list) {
        Position pos = p.getPosition();
        list.addCapture(new Move(pos, new Position(pos,  near,  far)));
        list.addCapture(new Move(pos, new Position(pos,  far,  near)));
        list.addCapture(new Move(pos, new Position(pos, -far,  near)));
        list.addCapture(new Move(pos, new Position(pos, -far, -near)));
        list.addCapture(new Move(pos, new Position(pos,  far, -near)));
        list.addCapture(new Move(pos, new Position(pos,  near, -far)));
        list.addCapture(new Move(pos, new Position(pos, -near, -far)));
        list.addCapture(new Move(pos, new Position(pos, -near,  far)));
        return list;
	}

	@Override
	public MoveType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject obj = json.getAsJsonObject();
		return new MoveTypeLeaper(obj.get("near").getAsInt(), obj.get("far").getAsInt());
	}

}
