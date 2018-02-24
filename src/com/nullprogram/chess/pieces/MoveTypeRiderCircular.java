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

public class MoveTypeRiderCircular extends MoveType {
	/**
	 * An array of all 8 directions the rider can move, in the order of the rotation.
	 */
	private final Position[] directions;

	public MoveTypeRiderCircular(MoveMode mode, int near, int far) {
		super(mode);
		directions = new Position[8];
		directions[0] = new Position(near, far);
		directions[1] = new Position(far, near);
		directions[2] = new Position(far, -near);
		directions[3] = new Position(near, -far);
		directions[4] = new Position(-near, -far);
		directions[5] = new Position(-far, -near);
		directions[6] = new Position(-far, near);
		directions[7] = new Position(-near, far);
	}

	@Override
	public IMoveList getMoves(Piece p, IMoveList list) {
        Position start = p.getPosition();
        
        for (int i = 0; i < 8; i++)
        {
        	Position current = start;
        	
        	int breakpoint = -1;
			for (int j = 0; j < 7; j++)
			{
				int mov = (i + j) % 8; //Loop over the array
				current = current.offset(directions[mov]);
				
				if (!list.add(new Move(start, current), getMoveMode()) || !p.getBoard().isFree(current))
				{
					breakpoint = j;
					break;
				}
			}
			
			//Loop backwards if we get stopped before going full circle
			if (breakpoint >= 0)
			{
				current = start;
				int opposite = (i + 4) % 8;
				for (int j = 7; j > breakpoint; j--)
				{
					int mov = (opposite + j) % 8; //Loop over the array
					current = current.offset(directions[mov]);
					
					if (!list.add(new Move(start, current), getMoveMode()) || !p.getBoard().isFree(current)) break;
				}
			}
		}
        
        return list;
	}
	
	@Override
	public MoveType create(JsonObject json, MoveMode mode, JsonDeserializationContext context) throws JsonParseException {
		return new MoveTypeRiderCircular(mode, JSONUtils.getMandatory(json, "near").getAsInt(), JSONUtils.getMandatory(json, "far").getAsInt());
	}

}
