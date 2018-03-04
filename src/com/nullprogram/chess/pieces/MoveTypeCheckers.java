package com.nullprogram.chess.pieces;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.Direction;
import com.nullprogram.chess.IMoveList;
import com.nullprogram.chess.Move;
import com.nullprogram.chess.MoveType;
import com.nullprogram.chess.Piece;
import com.nullprogram.chess.Position;

public class MoveTypeCheckers extends MoveType {
	/** If true, only keep the longest capturing sequences */
	private boolean longestOnly;
	
	public MoveTypeCheckers(MoveMode mode, DirectionMode directionMode, boolean longestOnly)
	{
		super(mode, directionMode);
		this.longestOnly = longestOnly;
	}

	@Override
	public IMoveList getMoves(Piece p, IMoveList list) {
        Position pos = p.getPosition();
        
        if (getMoveMode() != MoveMode.CAPTURE)
        {
        	for (Position dir : Direction.DIAGONAL_POS)
        	{
        		if (!dir.match(getDirectionMode(), p)) continue;
        		list.addMove(new Move(pos, pos.offset(dir)));
        	}
        }
        if (getMoveMode() != MoveMode.MOVE) makeCaptures(p, list, p.getPosition(), null);
        
        return list;
	}
	
	/**
	 * Attemps to add another capture to the current sequence, and add any newfound sequence to the list.
	 * 
	 * @param p Piece that is moving
	 * @param list IMoveList to put the moves in
	 * @param curPos current Position of the sequence
	 * @param sequence current sequence of captures
	 * @return true if at least one capture could be appended
	 */
	private boolean makeCaptures(Piece p, IMoveList list, Position curPos, final Move sequence) {
		boolean added = false;
		for (Position dir : Direction.DIAGONAL_POS)
		{
			if (!dir.match(getDirectionMode(), p)) continue;
			Position capture = curPos.offset(dir);
			//Check if not already in the sequence
			if (sequence != null && sequence.captures(capture)) continue;
			//Check if enemy
			if (p.getBoard().isEnemy(capture, p.getSide()))
			{
				Position next = capture.offset(dir);
				//Check if can jump
				if (p.getBoard().isFree(next))
				{
					added = true;
					Move start = new Move(p.getPosition(), next);
					Move newSequence = appendSequence(sequence, new Move(capture, null));
					start.setNext(newSequence);
					
					//Chain jump
					if (longestOnly) {
						//Only add if we can't go further
						if (!makeCaptures(p, list, next, newSequence)) list.add(start);
					}
					else {
						list.add(start);
						makeCaptures(p, list, next, newSequence);
					}
				}
			}
		}
		return added;
	}
	
	/**
	 * Makes a copy of the given sequence and appends the move to it.
	 * 
	 * @param sequence sequence (Move) to append to, can be null
	 * @param next Move to append to the sequence
	 * @return a copy of the sequence with the appended move
	 */
	private Move appendSequence(final Move sequence, Move next)
	{
		if (sequence == null) return next;
		Move newSequence = sequence.copy();
		newSequence.getLast().setNext(next);
		return newSequence;
	}
	
	@Override
	public MoveType create(JsonObject json, MoveMode mode, DirectionMode directionMode, JsonDeserializationContext context) throws JsonParseException {
		JsonElement elem = json.get("longestonly");
		boolean longestOnly = true;
		if (elem != null) longestOnly = elem.getAsBoolean();
		return new MoveTypeCheckers(mode, directionMode, longestOnly);
	}

	@Override
	public String getTypeName() {
		return "Checkers";
	}

}
