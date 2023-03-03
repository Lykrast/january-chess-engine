package com.nullprogram.chess.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.pieces.Piece;

public enum Capturability {
	//TODO more options
	ALWAYS, BY_ROYAL, NEVER;
	
	public boolean canBeCapturedBy(Piece capturer) {
		if (this == BY_ROYAL) return capturer.getModel().isRoyal();
		else return this == ALWAYS;
	}

	/**
	 * Attemps to find a "capturable" value in the given JsonObject and returns its value. <br>
	 * If no field was found, the default ALWAYS one is returned.
	 * 
	 * @param json JsonObject that may contain a symmetry field
	 * @return parsed "capturable" field if one is found, ALWAYS if none is found
	 * @throws JsonParseException if a field is found but does not match any existing Capturability
	 */
	public static Capturability fromJson(JsonObject json) throws JsonParseException {
		JsonElement tmp = json.get("capturable");
		if (tmp != null) {
			String sSym = tmp.getAsString();
			if (sSym.equalsIgnoreCase("ALWAYS")) return ALWAYS;
			else if (sSym.equalsIgnoreCase("BY_ROYAL")) return BY_ROYAL;
			else if (sSym.equalsIgnoreCase("NEVER")) return NEVER;
			else throw new JsonParseException(
					"Invalid capturable value : " + sSym + " - must be ALWAYS, BY_ROYAL or NEVER");
		}
		else return ALWAYS;
	}
}
