package com.nullprogram.chess;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.resources.IMoveTypeDeserializer;

/**
 * A MoveType that contains 2 groups of MoveTypes, one if a condition is fulfilled and the other if it isn't.
 * @author Lykrast
 */
public abstract class MoveCondition implements IMoveTypeDeserializer, IMoveType {
    private IMoveType[] movesTrue, movesFalse;

	public MoveCondition(IMoveType[] movesTrue, IMoveType[] movesFalse) {
		this.movesTrue = movesTrue;
		this.movesFalse = movesFalse;
	}
	
	/**
	 * Check whether or not the condition is fulfilled to know which moves to use.
	 * @param p Piece that moves
	 * @return true if the condition is fulfilled and movesTrue should be use, false otherwise to use movesFalse 
	 */
	protected abstract boolean evaluate(Piece p);

	@Override
	public IMoveList getMoves(Piece p, IMoveList list) {
		if (evaluate(p))
		{
	        for (IMoveType m : movesTrue) list = m.getMoves(p, list);
		}
		else
		{
	        for (IMoveType m : movesFalse) list = m.getMoves(p, list);
		}
		return list;
	}

	public final IMoveType create(JsonObject json, JsonDeserializationContext context) throws JsonParseException {
		List<IMoveType> movesTrue = new ArrayList<>();
		List<IMoveType> movesFalse = new ArrayList<>();
		
		JsonElement tmp = json.get("true");
		if (tmp != null)
		{
			JsonArray array = tmp.getAsJsonArray();
			for (JsonElement elem : array) movesTrue.add(context.deserialize(elem, IMoveType.class));
		}
		
		tmp = json.get("false");
		if (tmp != null)
		{
			JsonArray array = tmp.getAsJsonArray();
			for (JsonElement elem : array) movesFalse.add(context.deserialize(elem, IMoveType.class));
		}
		
		return create(json, movesTrue.toArray(new IMoveType[movesTrue.size()]), movesFalse.toArray(new IMoveType[movesFalse.size()]), context);
	}
	
	/**
	 * Creates a MoveCondition according to the given JsonElement following the (already deserialized) moves.
	 */
	protected abstract MoveCondition create(JsonObject json, IMoveType[] movesTrue, IMoveType[] movesFalse, JsonDeserializationContext context) throws JsonParseException;

}
