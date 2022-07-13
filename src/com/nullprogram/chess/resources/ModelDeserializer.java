package com.nullprogram.chess.resources;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.pieces.AIHints;
import com.nullprogram.chess.pieces.Model;
import com.nullprogram.chess.pieces.movement.IMoveType;

public class ModelDeserializer implements JsonDeserializer<Model> {
	public static final ModelDeserializer INSTANCE = new ModelDeserializer();
    private static final Logger LOG = Logger.getLogger("com.nullprogram.chess.resources.ModelDeserializer");

	private ModelDeserializer() {}

	@Override
	public Model deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject obj = json.getAsJsonObject();

		JsonArray movesJson = JSONUtils.getMandatory(obj, "moves").getAsJsonArray();
		List<IMoveType> movesList = new ArrayList<>();
		for (JsonElement elem : movesJson) {
			movesList.add(context.deserialize(elem, IMoveType.class));
		}

		boolean royal = JSONUtils.getDefaultBoolean(obj, "royal", false);
		//TODO: more general Immobilizer support
		boolean immobilizer = JSONUtils.getDefaultBoolean(obj, "immobilizer", false);
		double value = JSONUtils.getDefaultDouble(obj, "value", 1.0);
		
		String name = JSONUtils.getMandatory(obj, "name").getAsString();
		
		JsonElement tmp = obj.get("icon");
		String icon = null;
		if (tmp != null) icon = tmp.getAsString();
		else {
			icon = name.replace(' ', '_').toLowerCase();
			LOG.warning("Piece " + name + " has no icon defined, default to " + icon);
		}

		return new Model(name, icon, value, royal, immobilizer, AIHints.fromJson(obj), movesList.toArray(new IMoveType[movesList.size()]));
	}

}
