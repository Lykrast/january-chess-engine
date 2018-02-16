package com.nullprogram.chess.resources;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.Model;
import com.nullprogram.chess.Piece;
import com.nullprogram.chess.Piece.Side;
import com.nullprogram.chess.boards.PiecePlacement;
import com.nullprogram.chess.pieces.PieceRegistry;

public class PiecePlacementDeserializer implements JsonDeserializer<PiecePlacement> {
	public static final PiecePlacementDeserializer INSTANCE = new PiecePlacementDeserializer();
	
	private PiecePlacementDeserializer() {}

	@Override
	public PiecePlacement deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject obj = json.getAsJsonObject();
		
		int x = obj.get("x").getAsInt();
		int y = obj.get("y").getAsInt();
		Piece.Side side = null;
		String jSide = obj.get("side").getAsString();
		if (jSide.equalsIgnoreCase("white"))
		{
			side = Side.WHITE;
		}
		else if (jSide.equalsIgnoreCase("black"))
		{
			side = Side.BLACK;
		}
		Model model = PieceRegistry.get(obj.get("piece").getAsString());
		
		return new PiecePlacement(x, y, side, model);
	}

}
