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
	
	public MoveTypeLeaperLame(MoveMode mode, DirectionMode directionMode, int near, int far)
	{
		super(mode, directionMode);
		this.near = near;
		this.far = far;
	}

	@Override
	public IMoveList getMoves(Piece p, IMoveList list) {
        int forward = p.getSide().value();
        DirectionMode dir = getDirectionMode();
        
        if (dir.left()) horizontalSlide(p, list, -1, 0);
        if (dir.right()) horizontalSlide(p, list, 1, 0);
        if (dir.back()) horizontalSlide(p, list, 0, -forward);
        if (dir.forward()) horizontalSlide(p, list, 0, forward);
        return list;
	}
	
	/**
	 * Does the horizontal slide part of the move, followed by diagonal slides if necessary.
	 * 
	 * @param p Piece that is moving
	 * @param list IMoveList to add moves once we get there
	 * @param xdir direction of the x movement, must be -1 or 1 if ydir is 0, or 0 if ydir is non 0
	 * @param ydir direction of the y movement, must be -1 or 1 if xdir is 0, or 0 if xdir is non 0
	 */
	private void horizontalSlide(Piece p, IMoveList list, int xdir, int ydir)
	{
		//Slide until we get in the diagonal part
		Position current = p.getPosition();
		int dist = far;
		while (dist > near)
		{
			dist--;
			current = current.offset(xdir, ydir);
			//Slide is blocked
			if (!p.getBoard().isFree(current)) return;
		}
		//Orthogonal move, no need to diagonal
		if (near == 0)
		{
			list.add(new Move(p.getPosition(), current.offset(xdir, ydir)), getMoveMode());
		}
		//Vertical slide
		else if (xdir == 0)
		{
			diagonalSlide(p, list, current, -1, ydir);
			diagonalSlide(p, list, current, 1, ydir);
		}
		//Horizontal slide
		else
		{
			diagonalSlide(p, list, current, xdir, -1);
			diagonalSlide(p, list, current, xdir, 1);
		}
	}
	
	/**
	 * Does the diagonal slide part of the move.
	 * 
	 * @param p Piece that is moving
	 * @param list IMoveList to add moves once we get there
	 * @param current current Position of the move
	 * @param xdir direction of the x movement, must be -1 or 1
	 * @param ydir direction of the y movement, must be -1 or 1
	 */
	private void diagonalSlide(Piece p, IMoveList list, Position current, int xdir, int ydir)
	{
		//Slide until we get the final move
		int dist = near;
		while (dist > 1) {
			dist--;
			current = current.offset(xdir, ydir);
			//Slide is blocked
			if (!p.getBoard().isFree(current)) return;
		}
		list.add(new Move(p.getPosition(), current.offset(xdir, ydir)), getMoveMode());
	}
	
	@Override
	public MoveType create(JsonObject json, MoveMode mode, DirectionMode directionMode, JsonDeserializationContext context) throws JsonParseException {
		return new MoveTypeLeaperLame(mode, directionMode, JSONUtils.getMandatory(json, "near").getAsInt(), JSONUtils.getMandatory(json, "far").getAsInt());
	}

	@Override
	public String getTypeName() {
		return "LeaperLame";
	}

}
