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

public class MoveTypeLeaperLame extends MoveType {
	private int near, far;
	
	public MoveTypeLeaperLame(MoveMode mode, int near, int far)
	{
		super(mode);
		this.near = near;
		this.far = far;
	}

	@Override
	public IMoveList getMoves(Piece p, IMoveList list) {
        Position pos = p.getPosition();
        horizontalSlide(p, list, pos, pos, -1, 0);
        horizontalSlide(p, list, pos, pos, 1, 0);
        horizontalSlide(p, list, pos, pos, 0, -1);
        horizontalSlide(p, list, pos, pos, 0, 1);
        return list;
	}
	
	private void horizontalSlide(Piece p, IMoveList list, Position start, Position current, int xdir, int ydir)
	{
		//Slide until we get in the diagonal part
		int dist = far;
		while (dist > near)
		{
			dist--;
			current = current.offset(xdir, ydir);
			//Slide is blocked
			if (!p.getBoard().isFree(current)) return;
		}
		//Orthogonal move, no need to diagonal
		if (near == 0) list.add(new Move(start, current.offset(xdir, ydir)), getMoveMode());
		//Vertical slide
		else if (xdir == 0)
		{
			diagonalSlide(p, list, start, current, -1, ydir);
			diagonalSlide(p, list, start, current, 1, ydir);
		}
		//Horizontal slide
		else
		{
			diagonalSlide(p, list, start, current, xdir, -1);
			diagonalSlide(p, list, start, current, xdir, 1);
		}
	}
	
	private void diagonalSlide(Piece p, IMoveList list, Position start, Position current, int xdir, int ydir)
	{
		//Slide until we get the final move
		int dist = near;
		while (dist > 1) {
			dist--;
			current = current.offset(xdir, ydir);
			//Slide is blocked
			if (!p.getBoard().isFree(current)) return;
		}
		list.add(new Move(start, current.offset(xdir, ydir)), getMoveMode());
	}
	
	@Override
	public MoveType create(JsonObject json, MoveMode mode, JsonDeserializationContext context) throws JsonParseException {
		return new MoveTypeLeaperLame(mode, JSONUtils.getMandatory(json, "near").getAsInt(), JSONUtils.getMandatory(json, "far").getAsInt());
	}

}
