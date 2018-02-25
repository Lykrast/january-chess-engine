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
	public MoveTypeKing(MoveMode mode) {
		super(mode);
	}

	@Override
	public IMoveList getMoves(Piece p, IMoveList list) {
        Position pos = p.getPosition();
        for (int y = -1; y <= 1; y++) {
            for (int x = -1; x <= 1; x++) {
                if (x != 0 || y != 0) {
                    list.add(new Move(pos, pos.offset(x, y)), getMoveMode());
                }
            }
        }
        return list;
	}

	@Override
	public MoveType create(JsonObject elem, MoveMode mode, JsonDeserializationContext context) throws JsonParseException {
		return new MoveTypeKing(mode);
	}

	@Override
	public String getTypeName() {
		return "King";
	}

}
