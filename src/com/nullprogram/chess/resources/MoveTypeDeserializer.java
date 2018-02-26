package com.nullprogram.chess.resources;

import java.lang.reflect.Type;
import java.util.HashMap;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.MoveType;
import com.nullprogram.chess.MoveType.DirectionMode;

public class MoveTypeDeserializer implements JsonDeserializer<MoveType> {
	public static final MoveTypeDeserializer INSTANCE = new MoveTypeDeserializer();
	private static final HashMap<String, IMoveTypeDeserializer> MAP = new HashMap<>();
	
	private MoveTypeDeserializer() {}

	@Override
	public MoveType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject obj = json.getAsJsonObject();
		String name = JSONUtils.getMandatory(obj, "type").getAsString();
		if (!MAP.containsKey(name)) throw new JsonParseException("Mentions unknown move type: " + name);
		
		JsonElement modeJson = obj.get("mode");
		MoveType.MoveMode mode = MoveType.MoveMode.MOVE_CAPTURE;
		if (modeJson != null)
		{
			mode = MoveType.MoveMode.fromString(modeJson.getAsString());
		}
		
		return MAP.get(name).create(obj, mode, DirectionMode.fromJson(obj.get("direction")), context);
	}
	
	public static void registerDeserializer(IMoveTypeDeserializer deserializer)
	{
		MAP.put(deserializer.getTypeName(), deserializer);
	}

}
