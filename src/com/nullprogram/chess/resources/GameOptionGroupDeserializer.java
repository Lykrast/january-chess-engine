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
import com.nullprogram.chess.boards.GameOption;
import com.nullprogram.chess.boards.GameOptionGroup;

public class GameOptionGroupDeserializer implements JsonDeserializer<GameOptionGroup> {
	public static final GameOptionGroupDeserializer INSTANCE = new GameOptionGroupDeserializer();
	private GameOptionGroupDeserializer() {}

	@Override
	public GameOptionGroup deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject obj = json.getAsJsonObject();
		
		JsonArray array = JSONUtils.getMandatory(obj, "values").getAsJsonArray();
		List<GameOption> list = new ArrayList<>();
		for (JsonElement elem : array)
		{
			list.add(context.deserialize(elem, GameOption.class));
		}
		
		return new GameOptionGroup(JSONUtils.getMandatory(obj, "name").getAsString(),
				list.toArray(new GameOption[list.size()]));
	}

}
