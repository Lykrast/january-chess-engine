package com.nullprogram.chess.pieces;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.IMoveList;
import com.nullprogram.chess.MoveListWrapper;
import com.nullprogram.chess.MoveModifier;
import com.nullprogram.chess.MoveType;
import com.nullprogram.chess.Piece;

public class MoveModifierRestriction extends MoveModifier {
	public static final int SYM_NONE = 0, SYM_HORIZONTAL = 1, SYM_VERTICAL = 2, SYM_CENTRAL = 3;
	private int symmetry, xmin, xmax, ymin, ymax;

	public MoveModifierRestriction(MoveType[] moves, int symmetry, int xmin, int xmax, int ymin, int ymax) {
		super(moves);
		this.symmetry = symmetry;
		this.xmin = xmin;
		this.xmax = xmax;
		this.ymin = ymin;
		this.ymax = ymax;
	}

	@Override
	protected MoveListWrapper createWrapper(Piece p, IMoveList list) {
		//No symmetry check for white
		if (p.getSide() == Piece.Side.WHITE) return new MoveListWrapperRestriction(p, list, xmin, xmax, ymin, ymax);
		
		int xlast = p.getBoard().getWidth() - 1, ylast = p.getBoard().getHeight() - 1;
		switch (symmetry)
		{
		case SYM_HORIZONTAL:
			//Keep -1 as they mean "unbounded"
			return new MoveListWrapperRestriction(p, list, 
					xmax != -1 ? xlast - xmax : xmax, 
					xmin != -1 ? xlast - xmin : xmin, 
					ymin, ymax);
		case SYM_VERTICAL:
			return new MoveListWrapperRestriction(p, list, xmin, xmax, 
					ymax != -1 ? ylast - ymax : ymax, 
					ymin != -1 ? ylast - ymin : ymin);
		case SYM_CENTRAL:
			return new MoveListWrapperRestriction(p, list, 
					xmax != -1 ? xlast - xmax : xmax, 
					xmin != -1 ? xlast - xmin : xmin, 
					ymax != -1 ? ylast - ymax : ymax, 
					ymin != -1 ? ylast - ymin : ymin);
		case SYM_NONE:
		default:
				return new MoveListWrapperRestriction(p, list, xmin, xmax, ymin, ymax);
		}
	}

	@Override
	public String getTypeName() {
		return "ModRestriction";
	}

	@Override
	protected MoveModifier create(JsonObject json, MoveMode mode, MoveType[] moves, JsonDeserializationContext context) throws JsonParseException {
		int sym = SYM_VERTICAL;
		JsonElement jSym = json.get("symmetry");
		if (jSym != null)
		{
			String sSym = jSym.getAsString();
			if (sSym.equalsIgnoreCase("none")) sym = SYM_NONE;
			else if (sSym.equalsIgnoreCase("horizontal")) sym = SYM_HORIZONTAL;
			else if (sSym.equalsIgnoreCase("vertical")) sym = SYM_VERTICAL;
			else if (sSym.equalsIgnoreCase("central")) sym = SYM_CENTRAL;
			else throw new JsonParseException("Invalid symmetry value : " + sSym + " - must be none, horizontal, vertical or central");
		}
		
		return new MoveModifierRestriction(moves, sym, 
				getDirection(json, "xmin"), 
				getDirection(json, "xmax"), 
				getDirection(json, "ymin"), 
				getDirection(json, "ymax"));
	}
	
	private static int getDirection(JsonObject json, String name) {
		JsonElement elem = json.get(name);
		if (elem == null) return -1;
		else return elem.getAsInt();
	}
}
