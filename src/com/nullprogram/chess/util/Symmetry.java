package com.nullprogram.chess.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.Position;
import com.nullprogram.chess.boards.Board;

public enum Symmetry {
	NONE,
	HORIZONTAL,
	VERTICAL,
	CENTRAL;
	
	/**
	 * Transpose the given Position on the given Board following this symmetry.
	 * @param b Board the Position is on
	 * @param pos Position to transpose
	 * @return a new Position with transposed coordinates
	 */
	public Position transpose(Board b, Position pos)
	{
		if (this == NONE) return pos;
		
		int xlast = b.getWidth() - 1, ylast = b.getHeight() - 1;
		if (this == HORIZONTAL) return new Position(xlast - pos.getX(), pos.getY());
		else if (this == VERTICAL) return new Position(pos.getX(), ylast - pos.getY());
		else if (this == CENTRAL) return new Position(xlast - pos.getX(), ylast - pos.getY());
		
		//Shouldn't ever reach this point
		return null;
	}
	
	/**
	 * Attemps to find a "symmetry" value in the given JsonObject and returns its value.
	 * <br>
	 * If no field was found, the default VERTICAL one is returned.
	 * @param json JsonObject that may contain a symmetry field
	 * @return parsed "symmetry" field if one is found, VERTICAL if none is found
	 * @throws JsonParseException if a field is found but does not match any existing Symmetry
	 */
	public static Symmetry fromJson(JsonObject json) throws JsonParseException
	{
		JsonElement tmp = json.get("symmetry");
		if (tmp != null)
		{
			String sSym = tmp.getAsString();
			if (sSym.equalsIgnoreCase("NONE")) return NONE;
			else if (sSym.equalsIgnoreCase("HORIZONTAL")) return HORIZONTAL;
			else if (sSym.equalsIgnoreCase("VERTICAL")) return VERTICAL;
			else if (sSym.equalsIgnoreCase("CENTRAL")) return CENTRAL;
			else throw new JsonParseException("Invalid symmetry value : " + sSym + " - must be NONE, HORIZONTAL, VERTICAL, or CENTRAL");
		}
		else return VERTICAL;
	}
}
