package com.nullprogram.chess.pieces.movement;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.pieces.Piece;
import com.nullprogram.chess.resources.IMoveTypeDeserializer;
import com.nullprogram.chess.resources.JSONUtils;

/**
 * A MoveType that contains more MoveTypes but applies changes to the moves they attempt.
 * @author Lykrast
 */
public abstract class MoveModifier implements IMoveTypeDeserializer, IMoveType {
	private IMoveType[] moves;

	public MoveModifier(IMoveType[] moves) {
		this.moves = moves;
	}

	/**
	 * Creates a MoveListWrapper around the given IMoveList with the given Piece to modify or restrict added Moves.
	 * @param p    Piece that moves
	 * @param list IMoveList to wrap
	 * @return a MoveListWrapper around the given IMoveList
	 */
	protected abstract MoveListWrapper createWrapper(Piece p, IMoveList list);

	@Override
	public IMoveList getMoves(Piece p, IMoveList list) {
		IMoveList wrapper = createWrapper(p, list);
		for (IMoveType m : moves) wrapper = m.getMoves(p, wrapper);
		return list;
	}

	@Override
	public final IMoveType create(JsonObject json, JsonDeserializationContext context) throws JsonParseException {
		JsonArray movesJson = JSONUtils.getMandatory(json, "moves").getAsJsonArray();
		List<IMoveType> movesList = new ArrayList<>();
		for (JsonElement elem : movesJson) {
			if (elem.isJsonNull()) throw new JsonParseException("MoveModifier contains a null element in its moves");
			movesList.add(context.deserialize(elem, IMoveType.class));
		}
		return create(json, movesList.toArray(new IMoveType[movesList.size()]), context);
	}

	/**
	 * Creates a MoveType according to the given JsonElement following the (already deserialized) moves.
	 */
	protected abstract MoveModifier create(JsonObject json, IMoveType[] moves, JsonDeserializationContext context) throws JsonParseException;

}
