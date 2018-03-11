package com.nullprogram.chess.pieces.movement.modifier;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.pieces.Piece;
import com.nullprogram.chess.pieces.movement.IMoveList;
import com.nullprogram.chess.pieces.movement.IMoveType;
import com.nullprogram.chess.pieces.movement.MoveListWrapper;
import com.nullprogram.chess.pieces.movement.MoveModifier;
import com.nullprogram.chess.util.AreaSided;

public class MoveModifierPromotion extends MoveModifier {
	private String[] promoted;
	private AreaSided area;

	public MoveModifierPromotion(IMoveType[] moves, AreaSided area, String... promoted) {
		super(moves);
		this.promoted = promoted;
		this.area = area;
	}

	@Override
	protected MoveListWrapper createWrapper(Piece p, IMoveList list) {
		return new MoveListWrapperPromotion(p, list, promoted, area);
	}

	@Override
	protected MoveModifier create(JsonObject json, IMoveType[] moves, JsonDeserializationContext context) throws JsonParseException {
		JsonElement tmp = json.get("promoted");
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
		
		return new MoveModifierPromotion(moves, AreaSided.fromJson(json, true), promoted);
	}

	@Override
	public String getTypeName() {
		return "ModPromotion";
	}
}
