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
import com.nullprogram.chess.pieces.Model;
import com.nullprogram.chess.pieces.movement.IMoveType;

public class ModelDeserializer implements JsonDeserializer<Model> {
	public static final ModelDeserializer INSTANCE = new ModelDeserializer();
	private ModelDeserializer() {}

	@Override
	public Model deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject obj = json.getAsJsonObject();
		
		JsonArray movesJson = JSONUtils.getMandatory(obj, "moves").getAsJsonArray();
		List<IMoveType> movesList = new ArrayList<>();
		for (JsonElement elem : movesJson)
		{
			movesList.add(context.deserialize(elem, IMoveType.class));
		}
		
		JsonElement tmp = obj.get("royal");
		boolean royal = false;
		if (tmp != null)
		{
			royal = tmp.getAsBoolean();
		}
		
		tmp = obj.get("value");
		double value = 1.0;
		if (tmp != null)
		{
			value = tmp.getAsDouble();
		}
		
		return new Model(JSONUtils.getMandatory(obj, "name").getAsString(), 
				value, 
				royal, 
				movesList.toArray(new IMoveType[movesList.size()]));
	}

}
