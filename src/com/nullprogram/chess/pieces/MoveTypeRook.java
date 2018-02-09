package com.nullprogram.chess.pieces;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.MoveList;
import com.nullprogram.chess.MoveType;
import com.nullprogram.chess.Piece;

public class MoveTypeRook extends MoveType {

	@Override
	public MoveList getMoves(Piece p, MoveList list) {
        list = MoveUtil.getRookMoves(p, list);
        return list;
	}

	@Override
	public MoveType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		return new MoveTypeRook();
	}

}
