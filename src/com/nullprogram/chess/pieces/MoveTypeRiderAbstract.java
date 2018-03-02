package com.nullprogram.chess.pieces;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.IMoveList;
import com.nullprogram.chess.Move;
import com.nullprogram.chess.MoveType;
import com.nullprogram.chess.Piece;
import com.nullprogram.chess.Position;

public abstract class MoveTypeRiderAbstract extends MoveType {
	protected int max;

	public MoveTypeRiderAbstract(MoveMode mode, DirectionMode directionMode, int max) {
		super(mode, directionMode);
		this.max = max;
	}

	@Override
	public IMoveList getMoves(Piece p, IMoveList list) {
        Position start = p.getPosition();
        
        for (Position dir : getDirections())
        {
        	if (!dir.match(getDirectionMode(), p)) continue;
        	Position pos = start.offset(dir);
        	int steps = max;
        	//-1 means unlimited, as it can never get to 0
        	while (steps != 0 && list.add(new Move(start, pos), getMoveMode()) && p.getBoard().isFree(pos))
        	{
        		steps--;
        		pos = pos.offset(dir);
        	}
        }
        
        return list;
	}
	
	/**
	 * The directions this abstract rider can ride to.
	 * @return array of positions used as directions
	 */
	protected abstract Position[] getDirections();
	
	@Override
	public MoveType create(JsonObject json, MoveMode moveMode, DirectionMode directionMode, JsonDeserializationContext context) throws JsonParseException {
		JsonElement tmp = json.get("max");
		int max = -1;
		if (tmp != null)
		{
			max = tmp.getAsInt();
		}
		
		return create(json, moveMode, directionMode, max, context);
	}

	/**
	 * Creates this MoveTypeRiderAbstract according to the given JsonElement following the (already deserialized) 
	 * MoveMode, DirectionMode and max distance.
	 */
	protected abstract MoveType create(JsonObject json, MoveMode moveMode, DirectionMode directionMode, int max, JsonDeserializationContext context) throws JsonParseException;

}
