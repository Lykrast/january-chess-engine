package com.nullprogram.chess.pieces;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.IMoveList;
import com.nullprogram.chess.Move;
import com.nullprogram.chess.MoveType;
import com.nullprogram.chess.Piece;
import com.nullprogram.chess.Position;

public class MoveTypeKing extends MoveType {
	public MoveTypeKing(MoveMode mode, DirectionMode directionMode) {
		super(mode, directionMode);
	}

	@Override
	public IMoveList getMoves(Piece p, IMoveList list) {
        Position pos = p.getPosition();
        int forward = p.getSide().value();
        DirectionMode dir = getDirectionMode();
        
        if (dir.forward() || dir.right()) list.add(new Move(pos, pos.offset(1, forward)), getMoveMode());
        if (dir.forward()) list.add(new Move(pos, pos.offset(0, forward)), getMoveMode());
        if (dir.forward() || dir.left()) list.add(new Move(pos, pos.offset(-1, forward)), getMoveMode());
        if (dir.right()) list.add(new Move(pos, pos.offset(1, 0)), getMoveMode());
        if (dir.left()) list.add(new Move(pos, pos.offset(-1, 0)), getMoveMode());
        if (dir.back() || dir.right()) list.add(new Move(pos, pos.offset(1, -forward)), getMoveMode());
        if (dir.back()) list.add(new Move(pos, pos.offset(0, -forward)), getMoveMode());
        if (dir.back() || dir.left()) list.add(new Move(pos, pos.offset(-1, -forward)), getMoveMode());
        
        return list;
	}

	@Override
	public MoveType create(JsonObject elem, MoveMode mode, DirectionMode directionMode, JsonDeserializationContext context) throws JsonParseException {
		return new MoveTypeKing(mode, directionMode);
	}

	@Override
	public String getTypeName() {
		return "King";
	}

}
