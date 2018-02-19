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
import com.nullprogram.chess.GameMode;
import com.nullprogram.chess.boards.PiecePlacement;

public class GameModeDeserializer implements JsonDeserializer<GameMode> {
	public static final GameModeDeserializer INSTANCE = new GameModeDeserializer();
	private GameModeDeserializer() {}

	@Override
	public GameMode deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject obj = json.getAsJsonObject();
		
		JsonArray piecesJson = JSONUtils.getMandatory(obj, "pieces").getAsJsonArray();
		List<PiecePlacement> piecesList = new ArrayList<>();
		for (JsonElement elem : piecesJson)
		{
			piecesList.add(context.deserialize(elem, PiecePlacement.class));
		}
		
		return new GameMode(JSONUtils.getMandatory(obj, "name").getAsString(),
				JSONUtils.getMandatory(obj, "width").getAsInt(), 
				JSONUtils.getMandatory(obj, "height").getAsInt(), 
				piecesList.toArray(new PiecePlacement[piecesList.size()]));
	}

}
