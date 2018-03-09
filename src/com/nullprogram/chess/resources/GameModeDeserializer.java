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
import com.nullprogram.chess.boards.GameMode;
import com.nullprogram.chess.boards.PiecePlacement;
import com.nullprogram.chess.pieces.PieceRegistry;

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
		
		JsonElement tmp = obj.get("promotions");
		String[] promotions = null;
		if (tmp != null)
		{
			JsonArray array = tmp.getAsJsonArray();
			List<String> list = new ArrayList<>();
			for (JsonElement elem : array)
			{
				String s = elem.getAsString();
				if (!PieceRegistry.exists(s)) throw new JsonParseException("Mentions unknown or invalid piece: " + s);
				list.add(s);
			}
			promotions = list.toArray(new String[list.size()]);
		}
		
		tmp = obj.get("checkpolicy");
		boolean checkMultiple = true;
		if (tmp != null)
		{
			String str = tmp.getAsString();
			if (str.equalsIgnoreCase("ANY")) checkMultiple = false;
			else if (str.equalsIgnoreCase("ALL")) checkMultiple = true;
			else throw new JsonParseException("Invalid check policy : " + str + " - must be ANY or ALL");
		}
		
		return new GameMode(JSONUtils.getMandatory(obj, "name").getAsString(),
				JSONUtils.getMandatory(obj, "width").getAsInt(), 
				JSONUtils.getMandatory(obj, "height").getAsInt(), 
				piecesList.toArray(new PiecePlacement[piecesList.size()]),
				promotions, checkMultiple);
	}

}
