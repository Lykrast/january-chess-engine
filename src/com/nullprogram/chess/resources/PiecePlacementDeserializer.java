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
		
		String piece = JSONUtils.getMandatory(obj, "piece").getAsString();
		if (!PieceRegistry.exists(piece)) throw new JsonParseException("Mentions unknown or invalid piece: " + piece);
		
		Model model = PieceRegistry.get(piece);
		
		int x = JSONUtils.getMandatory(obj, "x").getAsInt();
		int y = JSONUtils.getMandatory(obj, "y").getAsInt();
		Piece.Side side = null;
		String jSide = JSONUtils.getMandatory(obj, "side").getAsString();
		if (jSide.equalsIgnoreCase("white"))
		{
			side = Side.WHITE;
		}
		else if (jSide.equalsIgnoreCase("black"))
		{
			side = Side.BLACK;
		}
		else throw new JsonParseException("Mentions invalid side (should be \"black\" or \"white\"): " + jSide);
		
		return new PiecePlacement(x, y, side, model);
	}

}
