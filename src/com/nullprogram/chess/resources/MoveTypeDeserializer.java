package com.nullprogram.chess.resources;

import java.lang.reflect.Type;
import java.util.HashMap;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.MoveType;

public class MoveTypeDeserializer implements JsonDeserializer<MoveType> {
	public static final MoveTypeDeserializer INSTANCE = new MoveTypeDeserializer();
	private static final HashMap<String, JsonDeserializer<MoveType>> MAP = new HashMap<>();
	
	private MoveTypeDeserializer() {}

	@Override
	public MoveType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject obj = json.getAsJsonObject();
		String name = obj.get("type").getAsString();
		return MAP.get(name).deserialize(json, typeOfT, context);
	}
	
	public static void registerDeserializer(String type, JsonDeserializer<MoveType> deserializer)
	{
		MAP.put(type, deserializer);
	}

}
