package com.nullprogram.chess.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.Position;
import com.nullprogram.chess.boards.Board;
import com.nullprogram.chess.pieces.Piece;
import com.nullprogram.chess.pieces.Piece.Side;

public class AreaSided {
	private Area area;
	private Symmetry symmetry;
	
	public AreaSided(Area area, Symmetry symmetry) {
		this.area = area;
		this.symmetry = symmetry;
	}
	
	/**
	 * Tells whether or not the given Position is in this Area for the given Side.
	 * @param pos Position to check
	 * @param b Board in which the position is
	 * @param side Side to check
	 * @return true if the Position is in this Area for this Side, false otherwise
	 */
	public boolean inside(Position pos, Board b, Side side)
	{
		if (side == Side.BLACK) return area.inside(symmetry.transpose(b, pos));
		else return area.inside(pos);
	}

	/**
	 * Tells whether or not the given Piece is in this Area.
	 * @param p Piece to check
	 * @return true if the Piece is in this Area, false otherwise
	 */
	public boolean inside(Piece p)
	{
		return inside(p.getPosition(), p.getBoard(), p.getSide());
	}
	
	/**
	 * Attempts to find all required fields to define this SidedArea in the given JsonObject.
	 * <br>
	 * For more details, see {@link Area#fromJson(JsonObject) Area.fromJson} and {@link Symmetry#fromJson(JsonObject) Symmetry.fromJson}.
	 * @param json JsonObject to search fields in
	 * @return an AreaSided made using the found fields
	 * @throws JsonParseException 
	 */
	public static AreaSided fromJson(JsonObject json) throws JsonParseException
	{
		return new AreaSided(Area.fromJson(json), Symmetry.fromJson(json));
	}
}
