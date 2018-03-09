package com.nullprogram.chess.pieces.movement.generic;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.Direction;
import com.nullprogram.chess.Move;
import com.nullprogram.chess.Position;
import com.nullprogram.chess.pieces.Piece;
import com.nullprogram.chess.pieces.movement.IMoveList;
import com.nullprogram.chess.pieces.movement.MoveType;

public class MoveTypeLongLeaper extends MoveType {
	//TODO make possible in rook/bishop style
	public MoveTypeLongLeaper(DirectionMode directionMode) {
		super(MoveMode.MOVE_CAPTURE, directionMode);
	}

	@Override
	public IMoveList getMoves(Piece p, IMoveList list) {
        Position start = p.getPosition();
        
        for (Direction dir : Direction.ALL)
        {
        	if (!dir.match(getDirectionMode(), p)) continue;
        	Move sequence = null;
        	
        	Position dirpos = dir.getPosition();
        	Position pos = start.offset(dirpos);
        	while (p.getBoard().inRange(pos))
        	{
        		//Empty position, do the leaps
        		if (p.getBoard().isEmpty(pos))
        		{
            		Move move = new Move(start, pos);
                	move.setNext(sequence);
                	if (!list.addMove(move)) break;
        		}
        		//Enemy, leap over and add to capture sequence
        		else if (p.getBoard().isEnemy(pos, p.getSide()))
        		{
        			sequence = appendSequence(sequence, new Move(pos, null));
            		pos = pos.offset(dirpos);
            		Move move = new Move(start, pos);
                	move.setNext(sequence);
                	if (!list.addMove(move)) break;
        		}
        		//Found an obstruction, stop
        		else break;
        		pos = pos.offset(dirpos);
        	}
        }
        
        return list;
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
		return new MoveTypeLongLeaper(directionMode);
	}

	@Override
	public String getTypeName() {
		return "LongLeaper";
	}

}
