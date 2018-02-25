package com.nullprogram.chess.pieces;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.Direction;
import com.nullprogram.chess.IMoveList;
import com.nullprogram.chess.Move;
import com.nullprogram.chess.MoveType;
import com.nullprogram.chess.Piece;
import com.nullprogram.chess.Position;

public class MoveTypeWithdrawer extends MoveType {
	//TODO make possible in rook/bishop style
	public MoveTypeWithdrawer() {
		super(MoveMode.MOVE_CAPTURE);
	}

	@Override
	public IMoveList getMoves(Piece p, IMoveList list) {
        Position start = p.getPosition();
        
        for (Direction dir : Direction.ALL)
        {
        	Move withdrawal = null;
        	Position withdrew = start.offset(dir.opposite().getPosition());
        	//Check if there's an enemy piece to withdraw
        	if (!p.getBoard().isFree(withdrew) && p.getBoard().isFree(withdrew, p.getSide()))
        	{
        		withdrawal = new Move(withdrew, null);
        	}
        	
        	Position dirpos = dir.getPosition();
        	Position pos = start.offset(dirpos);
        	Move move = new Move(start, pos);
        	move.setNext(withdrawal);
        	while (list.addMove(move) && p.getBoard().isFree(pos))
        	{
        		pos = pos.offset(dirpos);
        		move = new Move(start, pos);
            	move.setNext(withdrawal);
        	}
        }
        
        return list;
	}
	
	@Override
	public MoveType create(JsonObject json, MoveMode mode, JsonDeserializationContext context) throws JsonParseException {
		return new MoveTypeWithdrawer();
	}

	@Override
	public String getTypeName() {
		return "Withdrawer";
	}

}
