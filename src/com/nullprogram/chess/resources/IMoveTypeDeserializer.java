package com.nullprogram.chess.resources;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.pieces.movement.IMoveType;

public interface IMoveTypeDeserializer {
	/**
	 * Creates a MoveType according to the given JsonElement.
	 * @param json Json to deserialize
	 * @param context TODO
	 * @return a MoveType created
	 */
	public IMoveType create(JsonObject json, JsonDeserializationContext context) throws JsonParseException;
	
	/**
	 * Gives the string that this deserializer is looking for in the type property. Should be in pascal case.
	 * @return name that this deserializer is looking for
	 */
	public String getTypeName();

}
