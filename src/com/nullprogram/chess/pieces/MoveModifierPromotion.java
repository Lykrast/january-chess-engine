package com.nullprogram.chess.pieces;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.IMoveList;
import com.nullprogram.chess.MoveListWrapper;
import com.nullprogram.chess.MoveModifier;
import com.nullprogram.chess.MoveType;
import com.nullprogram.chess.Piece;

public class MoveModifierPromotion extends MoveModifier {
	private String[] promoted;
	private int rows;

	public MoveModifierPromotion(MoveType[] moves, int rows, String... promoted) {
		super(moves);
		this.promoted = promoted;
		this.rows = rows;
	}

	@Override
	protected MoveListWrapper createWrapper(Piece p, IMoveList list) {
		return new MoveListWrapperPromotion(p, list, promoted, rows);
	}

	@Override
	protected MoveModifier create(JsonObject json, MoveMode mode, MoveType[] moves, JsonDeserializationContext context) throws JsonParseException {
		JsonElement tmp = json.get("rows");
		int rows = 1;
		if (tmp != null)
		{
			rows = tmp.getAsInt();
		}
		
		tmp = json.get("promoted");
		String[] promoted = null;
		if (tmp != null)
		{
			JsonArray array = tmp.getAsJsonArray();
			List<String> list = new ArrayList<>();
			for (JsonElement elem : array)
			{
				list.add(elem.getAsString());
			}
			promoted = list.toArray(new String[list.size()]);
		}
		
		return new MoveModifierPromotion(moves, rows, promoted);
	}

	@Override
	public String getTypeName() {
		return "ModPromotion";
	}
}
