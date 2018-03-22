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
import com.nullprogram.chess.boards.PiecePlacement;
import com.nullprogram.chess.pieces.PieceRegistry;

public class GameOptionDeserializer implements JsonDeserializer<GameOption> {
	public static final GameOptionDeserializer INSTANCE = new GameOptionDeserializer();
	private GameOptionDeserializer() {}

	@Override
	public GameOption deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject obj = json.getAsJsonObject();

		String[] promotionsW = parsePromotions(obj, "promotionswhite");
		String[] promotionsB = parsePromotions(obj, "promotionsblack");
		//If one of them was not found, use the shared one as default
		if (promotionsW == null || promotionsB == null)
		{
			String[] promotions = parsePromotions(obj, "promotions");
			if (promotions == null) promotions = new String[0];
			if (promotionsW == null) promotionsW = promotions;
			if (promotionsB == null) promotionsB = promotions;
		}
		
		JsonElement tmp = obj.get("pieces");
		PiecePlacement[] pieces;
		if (tmp != null)
		{
			JsonArray piecesJson = tmp.getAsJsonArray();
			List<PiecePlacement> piecesList = new ArrayList<>();
			for (JsonElement elem : piecesJson)
			{
				piecesList.add(context.deserialize(elem, PiecePlacement.class));
			}
			pieces = piecesList.toArray(new PiecePlacement[piecesList.size()]);
		}
		else pieces = new PiecePlacement[0];
		
		return new GameOption(JSONUtils.getMandatory(obj, "name").getAsString(),
				pieces, promotionsW, promotionsB);
	}
	
	private static String[] parsePromotions(JsonObject json, String name)
	{
		JsonElement tmp = json.get(name);
		if (tmp == null) return null;
		
		JsonArray array = tmp.getAsJsonArray();
		List<String> list = new ArrayList<>();
		for (JsonElement elem : array)
		{
			String s = elem.getAsString();
			if (!PieceRegistry.exists(s)) throw new JsonParseException("Mentions unknown or invalid piece: " + s);
			list.add(s);
		}
		return list.toArray(new String[list.size()]);
	}

}
