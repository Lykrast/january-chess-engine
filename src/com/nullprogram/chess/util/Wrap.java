package com.nullprogram.chess.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.Position;
import com.nullprogram.chess.boards.Board;

public enum Wrap {
	NONE, HORIZONTAL, VERTICAL, BOTH;

	/**
	 * Wrap the given Position on the given Board following this cylindrical wraping
	 * behavior.
	 * 
	 * @param b   Board the Position is on
	 * @param pos Position to wrap
	 * @return a new Position with adjusted coordinates
	 */
	public Position wrap(Board b, Position pos) {
		if (this == NONE) return pos;
		
		int x = pos.getX(), y = pos.getY();
		if (this == HORIZONTAL || this == BOTH) {
			int w = b.getWidth();
			if (x < 0) x = (x + w) % w;
			else if (x >= w) x %= w;
		}
		if (this == VERTICAL || this == BOTH) {
			int h = b.getHeight();
			if (y < 0) y = (y + h) % h;
			else if (y >= h) x %= h;
		}

		return new Position(x, y);
	}

	/**
	 * Attemps to find a "wrap" value in the given JsonObject and returns its
	 * value. <br>
	 * If no field was found, the default HORIZONTAL one is returned.
	 * 
	 * @param json JsonObject that may contain a symmetry field
	 * @return parsed "wrap" field if one is found, HORIZONTAL if none is found
	 * @throws JsonParseException if a field is found but does not match any
	 *                            existing Wrap
	 */
	public static Wrap fromJson(JsonObject json) throws JsonParseException {
		JsonElement tmp = json.get("wrap");
		if (tmp != null) {
			String sSym = tmp.getAsString();
			if (sSym.equalsIgnoreCase("NONE")) return NONE;
			else if (sSym.equalsIgnoreCase("HORIZONTAL")) return HORIZONTAL;
			else if (sSym.equalsIgnoreCase("VERTICAL")) return VERTICAL;
			else if (sSym.equalsIgnoreCase("BOTH")) return BOTH;
			else throw new JsonParseException(
					"Invalid wrapping value : " + sSym + " - must be NONE, HORIZONTAL, VERTICAL or BOTH");
		}
		else return HORIZONTAL;
	}
}
