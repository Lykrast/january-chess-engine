package com.nullprogram.chess.resources;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.MoveType;
import com.nullprogram.chess.MoveType.MoveMode;

public interface IMoveTypeDeserializer {
	/**
	 * Creates a MoveType according to the given JsonElement following the (already deserialized) MoveMode.
	 * @param json Json to deserialize
	 * @param mode MoveMode to follow
	 * @return a MoveType created with the given MoveMode
	 */
	public MoveType create(JsonObject json, MoveMode mode) throws JsonParseException;

}
