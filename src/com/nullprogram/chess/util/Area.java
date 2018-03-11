package com.nullprogram.chess.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.Position;

public class Area {
	private int xmin, xmax, ymin, ymax;

	/**
	 * Makes the Area using the given coordinates as bounds. Note that they are inclusive bounds.
	 * <br>
	 * Coordinates are 0-indexed on Boards. -1 may be used to make the Area unbounded in a certain direction.
	 * @param xmin leftmost position (inclusive)
	 * @param xmax rightmost position (inclusive)
	 * @param ymin bottommost position (inclusive)
	 * @param ymax topmost position (inclusive)
	 */
	public Area(int xmin, int xmax, int ymin, int ymax) {
		this.xmin = xmin;
		this.xmax = xmax;
		this.ymin = ymin;
		this.ymax = ymax;
	}
	
	/**
	 * Tells whether or not the given Position is in this Area.
	 * @param pos Position to check
	 * @return true if the Position is in this Area, false otherwise
	 */
	public boolean inside(Position pos)
	{
        return ((xmin == -1 || pos.getX() >= xmin)
        		&& (xmax == -1 || pos.getX() <= xmax)
        		&& (ymin == -1 || pos.getY() >= ymin)
        		&& (ymax == -1 || pos.getY() <= ymax));
	}
	
	/**
	 * Attempts to find all required fields to define this Area in the given JsonObject.
	 * <br>
	 * Fields looked for are "xmin", "xmax", "ymin" and "ymax". Any value not found will be set to -1 by default.
	 * @param json JsonObject to search fields in
	 * @return an Area made using the found fields
	 * @throws JsonParseException 
	 */
	public static Area fromJson(JsonObject json) throws JsonParseException
	{
		return new Area(getJsonBound(json, "xmin"), getJsonBound(json, "xmax"), getJsonBound(json, "ymin"), getJsonBound(json, "ymax"));
	}
	
	private static int getJsonBound(JsonObject json, String name) {
		JsonElement elem = json.get(name);
		if (elem == null) return -1;
		else return elem.getAsInt();
	}
}
