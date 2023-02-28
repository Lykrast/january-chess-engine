package com.nullprogram.chess.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.Position;
import com.nullprogram.chess.resources.JSONUtils;

public class Area {
	private int xmin, xmax, ymin, ymax;
	private boolean invert;

	/**
	 * Makes the Area using the given coordinates as bounds. Note that they are inclusive bounds.
	 * <br>
	 * Coordinates are 0-indexed on Boards.
	 * -1 may be used to make the Area unbounded in a certain direction.
	 * 
	 * @param xmin leftmost position (inclusive)
	 * @param xmax rightmost position (inclusive)
	 * @param ymin bottommost position (inclusive)
	 * @param ymax topmost position (inclusive)
	 * @param invert set to true to make the area outside the rectangle instead of inside
	 */
	public Area(int xmin, int xmax, int ymin, int ymax, boolean invert) {
		this.xmin = xmin;
		this.xmax = xmax;
		this.ymin = ymin;
		this.ymax = ymax;
		this.invert = invert;
	}

	/**
	 * Tells whether or not the given Position is in this Area.
	 * 
	 * @param pos Position to check
	 * @return true if the Position is in this Area, false otherwise
	 */
	public boolean inside(Position pos) {
		return ((xmin == -1 || pos.x >= xmin)
				&& (xmax == -1 || pos.x <= xmax)
				&& (ymin == -1 || pos.y >= ymin)
				&& (ymax == -1 || pos.y <= ymax)) != invert;
	}

	/**
	 * Attempts to find all required fields to define this Area in the given JsonObject.
	 * <br>
	 * Fields looked for are "xmin", "xmax", "ymin" and "ymax".
	 * Any value not found will be set to -1 by default.
	 * 
	 * @param json JsonObject to search fields in
	 * @return an Area made using the found fields
	 * @throws JsonParseException
	 */
	public static Area fromJson(JsonObject json) throws JsonParseException {
		return new Area(JSONUtils.getDefaultInt(json, "xmin", -1),
				JSONUtils.getDefaultInt(json, "xmax", -1),
				JSONUtils.getDefaultInt(json, "ymin", -1),
				JSONUtils.getDefaultInt(json, "ymax", -1),
				JSONUtils.getDefaultBoolean(json, "invert", false));
	}
}
