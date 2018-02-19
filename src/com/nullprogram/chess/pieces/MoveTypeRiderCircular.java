package com.nullprogram.chess.pieces;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.Move;
import com.nullprogram.chess.MoveList;
import com.nullprogram.chess.MoveType;
import com.nullprogram.chess.Piece;
import com.nullprogram.chess.Position;

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
	public MoveList getMoves(Piece p, MoveList list) {
        Position start = p.getPosition();
        
        for (int i = 0; i < 8; i++)
        {
        	Position current = start;
			for (int j = 0; j < 8; j++)
			{
				int mov = (i + j) % 8; //Loop over the array
				current = current.offset(directions[mov]);
				
				if (!list.add(new Move(start, current), getMoveMode()) || !p.getBoard().isFree(current)) break;
			}
		}
        
        return list;
	}
	
	@Override
	public MoveType create(JsonObject json, MoveMode mode) throws JsonParseException {
		return new MoveTypeRiderCircular(mode, json.get("near").getAsInt(), json.get("far").getAsInt());
	}

}
