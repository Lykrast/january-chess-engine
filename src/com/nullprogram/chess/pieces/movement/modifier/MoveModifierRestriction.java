package com.nullprogram.chess.pieces.movement.modifier;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.pieces.Piece;
import com.nullprogram.chess.pieces.movement.IMoveList;
import com.nullprogram.chess.pieces.movement.IMoveType;
import com.nullprogram.chess.pieces.movement.MoveListWrapper;
import com.nullprogram.chess.pieces.movement.MoveModifier;

public class MoveModifierRestriction extends MoveModifier {
	public static final int SYM_NONE = 0, SYM_HORIZONTAL = 1, SYM_VERTICAL = 2, SYM_CENTRAL = 3;
	private int symmetry, xmin, xmax, ymin, ymax;
	private boolean invert;

	public MoveModifierRestriction(IMoveType[] moves, int symmetry, int xmin, int xmax, int ymin, int ymax, boolean invert) {
		super(moves);
		this.symmetry = symmetry;
		this.xmin = xmin;
		this.xmax = xmax;
		this.ymin = ymin;
		this.ymax = ymax;
		this.invert = invert;
	}

	@Override
	protected MoveListWrapper createWrapper(Piece p, IMoveList list) {
		//No symmetry check for white
		if (p.getSide() == Piece.Side.WHITE) return new MoveListWrapperRestriction(p, list, xmin, xmax, ymin, ymax, invert);
		
		int xlast = p.getBoard().getWidth() - 1, ylast = p.getBoard().getHeight() - 1;
		switch (symmetry)
		{
		case SYM_HORIZONTAL:
			//Keep -1 as they mean "unbounded"
			return new MoveListWrapperRestriction(p, list, 
					xmax != -1 ? xlast - xmax : xmax, 
					xmin != -1 ? xlast - xmin : xmin, 
					ymin, ymax, invert);
		case SYM_VERTICAL:
			return new MoveListWrapperRestriction(p, list, xmin, xmax, 
					ymax != -1 ? ylast - ymax : ymax, 
					ymin != -1 ? ylast - ymin : ymin, invert);
		case SYM_CENTRAL:
			return new MoveListWrapperRestriction(p, list, 
					xmax != -1 ? xlast - xmax : xmax, 
					xmin != -1 ? xlast - xmin : xmin, 
					ymax != -1 ? ylast - ymax : ymax, 
					ymin != -1 ? ylast - ymin : ymin, invert);
		case SYM_NONE:
		default:
				return new MoveListWrapperRestriction(p, list, xmin, xmax, ymin, ymax, invert);
		}
	}

	@Override
	public String getTypeName() {
		return "ModRestriction";
	}

	@Override
	protected MoveModifier create(JsonObject json, IMoveType[] moves, JsonDeserializationContext context) throws JsonParseException {
		int sym = SYM_VERTICAL;
		JsonElement tmp = json.get("symmetry");
		if (tmp != null)
		{
			String sSym = tmp.getAsString();
			if (sSym.equalsIgnoreCase("NONE")) sym = SYM_NONE;
			else if (sSym.equalsIgnoreCase("HORIZONTAL")) sym = SYM_HORIZONTAL;
			else if (sSym.equalsIgnoreCase("VERTICAL")) sym = SYM_VERTICAL;
			else if (sSym.equalsIgnoreCase("CENTRAL")) sym = SYM_CENTRAL;
			else throw new JsonParseException("Invalid symmetry value : " + sSym + " - must be NONE, HORIZONTAL, VERTICAL or CENTRAL");
		}
		
		tmp = json.get("invert");
		boolean invert = false;
		if (tmp != null)
		{
			invert = tmp.getAsBoolean();
		}
		
		return new MoveModifierRestriction(moves, sym, 
				getDirection(json, "xmin"), 
				getDirection(json, "xmax"), 
				getDirection(json, "ymin"), 
				getDirection(json, "ymax"), 
				invert);
	}
	
	private static int getDirection(JsonObject json, String name) {
		JsonElement elem = json.get(name);
		if (elem == null) return -1;
		else return elem.getAsInt();
	}
}
