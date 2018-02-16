package com.nullprogram.chess.resources;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.Model;
import com.nullprogram.chess.MoveType;

public class ModelDeserializer implements JsonDeserializer<Model> {
	public static final ModelDeserializer INSTANCE = new ModelDeserializer();
	private ModelDeserializer() {}

	@Override
	public Model deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject obj = json.getAsJsonObject();
		
		JsonArray movesJson = obj.get("moves").getAsJsonArray();
		List<MoveType> movesList = new ArrayList<>();
		for (JsonElement elem : movesJson)
		{
			movesList.add(MoveTypeDeserializer.INSTANCE.deserialize(elem, typeOfT, context));
		}
		
		JsonElement r = obj.get("royal");
		boolean royal = false;
		if (r != null)
		{
			royal = r.getAsBoolean();
		}
		
		return new Model(obj.get("name").getAsString(), 
				obj.get("value").getAsDouble(), 
				royal, 
				movesList.toArray(new MoveType[movesList.size()]));
	}

}
