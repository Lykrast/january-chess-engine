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

public class MoveTypeWithdrawer extends MoveType {
	//TODO make possible in rook/bishop style
	public MoveTypeWithdrawer(DirectionMode directionMode) {
		super(MoveMode.MOVE_CAPTURE, directionMode);
	}

	@Override
	public IMoveList getMoves(Piece p, IMoveList list) {
        Position start = p.getPosition();
        
        for (Direction dir : Direction.ALL)
        {
        	if (!dir.match(getDirectionMode(), p)) continue;
        	Move withdrawal = null;
        	Position withdrew = start.offset(dir.opposite().getPosition());
        	//Check if there's an enemy piece to withdraw
        	if (p.getBoard().isEnemy(withdrew, p.getSide()))
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
	public MoveType create(JsonObject json, MoveMode mode, DirectionMode directionMode, JsonDeserializationContext context) throws JsonParseException {
		return new MoveTypeWithdrawer(directionMode);
	}

	@Override
	public String getTypeName() {
		return "Withdrawer";
	}

}
