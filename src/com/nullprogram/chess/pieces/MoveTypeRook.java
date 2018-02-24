package com.nullprogram.chess.pieces;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.IMoveList;
import com.nullprogram.chess.Move;
import com.nullprogram.chess.MoveType;
import com.nullprogram.chess.Piece;
import com.nullprogram.chess.Position;

public class MoveTypeRook extends MoveType {

	public MoveTypeRook(MoveMode mode) {
		super(mode);
	}

	@Override
	public IMoveList getMoves(Piece p, IMoveList list) {
		Position home = p.getPosition();
        int x = home.getX();
        int y = home.getY();
        while (x >= 0) {
            x--;
            Position pos = new Position(x, y);
            if (!list.add(new Move(home, pos), getMoveMode())) {
                break;
            }
            if (!p.getBoard().isFree(pos)) {
                break;
            }
        }
        x = home.getX();
        y = home.getY();
        while (x < p.getBoard().getWidth()) {
            x++;
            Position pos = new Position(x, y);
            if (!list.add(new Move(home, pos), getMoveMode())) {
                break;
            }
            if (!p.getBoard().isFree(pos)) {
                break;
            }
        }
        x = home.getX();
        y = home.getY();
        while (y >= 0) {
            y--;
            Position pos = new Position(x, y);
            if (!list.add(new Move(home, pos), getMoveMode())) {
                break;
            }
            if (!p.getBoard().isFree(pos)) {
                break;
            }
        }
        x = home.getX();
        y = home.getY();
        while (y < p.getBoard().getHeight()) {
            y++;
            Position pos = new Position(x, y);
            if (!list.add(new Move(home, pos), getMoveMode())) {
                break;
            }
            if (!p.getBoard().isFree(pos)) {
                break;
            }
        }
        return list;
	}
	
	@Override
	public MoveType create(JsonObject json, MoveMode mode, JsonDeserializationContext context) throws JsonParseException {
		return new MoveTypeRook(mode);
	}

}
