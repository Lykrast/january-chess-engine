package com.nullprogram.chess.pieces;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.IMoveList;
import com.nullprogram.chess.Move;
import com.nullprogram.chess.MoveType;
import com.nullprogram.chess.Piece;
import com.nullprogram.chess.Position;

public class MoveTypeBishop extends MoveType {

	public MoveTypeBishop(MoveMode mode) {
		super(mode);
	}

	@Override
	public IMoveList getMoves(Piece p, IMoveList list) {
        /* Scan each direction and stop looking when we run into something. */
        Position home = p.getPosition();
        int x = home.getX();
        int y = home.getY();
        while (x >= 0 && y >= 0) {
            x--;
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
        while (x < p.getBoard().getWidth() &&
               y < p.getBoard().getHeight()) {

            x++;
            y++;
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
        while (x >= 0 && y < p.getBoard().getHeight()) {
            x--;
            y++;
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
        while (x < p.getBoard().getWidth() && y >= 0) {
            x++;
            y--;
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
	public MoveType create(JsonObject elem, MoveMode mode, JsonDeserializationContext context) throws JsonParseException {
		return new MoveTypeBishop(mode);
	}

}
