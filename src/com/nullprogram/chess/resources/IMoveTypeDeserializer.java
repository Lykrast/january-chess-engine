package com.nullprogram.chess.resources;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.MoveType;
import com.nullprogram.chess.MoveType.DirectionMode;
import com.nullprogram.chess.MoveType.MoveMode;

public interface IMoveTypeDeserializer {
	/**
	 * Creates a MoveType according to the given JsonElement following the (already deserialized) MoveMode and DirectionMode.
	 * @param json Json to deserialize
	 * @param moveMode MoveMode to follow
	 * @param directionMode DirectionMode to follow
	 * @param context TODO
	 * @return a MoveType created with the given MoveMode
	 */
	public MoveType create(JsonObject json, MoveMode moveMode, DirectionMode directionMode, JsonDeserializationContext context) throws JsonParseException;
	
	/**
	 * Gives the string that this deserializer is looking for in the type property. Should be in pascal case.
	 * @return name that this deserializer is looking for
	 */
	public String getTypeName();

}
