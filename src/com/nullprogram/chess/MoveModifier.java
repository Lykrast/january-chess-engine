package com.nullprogram.chess;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.resources.JSONUtils;

/**
 * A MoveType that contains more MoveTypes but applies changes to the moves they attempt.
 * @author Lykrast
 */
public abstract class MoveModifier extends MoveType {
    private MoveType[] moves;

	public MoveModifier(MoveMode mode, MoveType[] moves) {
		super(mode);
		this.moves = moves;
	}
	
	/**
	 * Creates a MoveListWrapper around the given IMoveList with the given Piece to modify or restrict added Moves.
	 * @param p Piece that moves
	 * @param list IMoveList to wrap
	 * @return a MoveListWrapper around the given IMoveList
	 */
	protected abstract MoveListWrapper createWrapper(Piece p, IMoveList list);

	@Override
	public IMoveList getMoves(Piece p, IMoveList list) {
		IMoveList wrapper = createWrapper(p, list);
        for (MoveType m : moves) wrapper = m.getMoves(p, wrapper);
		return list;
	}

	@Override
	public final MoveType create(JsonObject json, MoveMode mode, JsonDeserializationContext context) throws JsonParseException {
		JsonArray movesJson = JSONUtils.getMandatory(json, "moves").getAsJsonArray();
		List<MoveType> movesList = new ArrayList<>();
		for (JsonElement elem : movesJson)
		{
			movesList.add(context.deserialize(elem, MoveType.class));
		}
		return create(json, mode, movesList.toArray(new MoveType[movesList.size()]), context);
	}
	
	/**
	 * Creates a MoveType according to the given JsonElement following the (already deserialized) MoveMode and moves.
	 */
	protected abstract MoveModifier create(JsonObject json, MoveMode mode, MoveType[] moves, JsonDeserializationContext context) throws JsonParseException;

}
