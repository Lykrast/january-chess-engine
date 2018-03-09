package com.nullprogram.chess.pieces.movement.condition;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.Position;
import com.nullprogram.chess.pieces.Piece;
import com.nullprogram.chess.pieces.movement.IMoveType;
import com.nullprogram.chess.pieces.movement.MoveCondition;

public class MoveConditionRestriction extends MoveCondition {
	public static final int SYM_NONE = 0, SYM_HORIZONTAL = 1, SYM_VERTICAL = 2, SYM_CENTRAL = 3;
	private int symmetry, xmin, xmax, ymin, ymax;

	public MoveConditionRestriction(IMoveType[] movesTrue, IMoveType[] movesFalse, int symmetry, int xmin, int xmax, int ymin, int ymax) {
		super(movesTrue, movesFalse);
		this.symmetry = symmetry;
		this.xmin = xmin;
		this.xmax = xmax;
		this.ymin = ymin;
		this.ymax = ymax;
	}

	@Override
	protected boolean evaluate(Piece p) {
		Position pos = p.getPosition();
		int tmpXmin = xmin, tmpXmax = xmax, tmpYmin = ymin, tmpYmax = ymax;
		if (p.getSide() == Piece.Side.BLACK)
		{
			int xlast = p.getBoard().getWidth() - 1, ylast = p.getBoard().getHeight() - 1;
			switch (symmetry)
			{
			case SYM_HORIZONTAL:
				//Keep -1 as they mean "unbounded"
				tmpXmax = xmin != -1 ? xlast - xmin : -1;
				tmpXmin = xmax != -1 ? xlast - xmax : -1;
				break;
			case SYM_VERTICAL:
				tmpYmax = ymin != -1 ? ylast - ymin : -1;
				tmpYmin = ymax != -1 ? ylast - ymax : -1;
				break;
			case SYM_CENTRAL:
				tmpXmax = xmin != -1 ? xlast - xmin : -1;
				tmpXmin = xmax != -1 ? xlast - xmax : -1;
				tmpYmax = ymin != -1 ? ylast - ymin : -1;
				tmpYmin = ymax != -1 ? ylast - ymax : -1;
				break;
			case SYM_NONE:
			default:
				break;
			}
		}
		//-1 means unbounded in that direction
        return ((tmpXmin == -1 || pos.getX() >= tmpXmin)
        		&& (tmpXmax == -1 || pos.getX() <= tmpXmax)
        		&& (tmpYmin == -1 || pos.getY() >= tmpYmin)
        		&& (tmpYmax == -1 || pos.getY() <= tmpYmax));
	}

	@Override
	protected MoveCondition create(JsonObject json, IMoveType[] movesTrue, IMoveType[] movesFalse, JsonDeserializationContext context) throws JsonParseException {
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
		
		return new MoveConditionRestriction(movesTrue, movesFalse, sym, 
				getDirection(json, "xmin"), 
				getDirection(json, "xmax"), 
				getDirection(json, "ymin"), 
				getDirection(json, "ymax"));
	}

	@Override
	public String getTypeName() {
		return "CondRestriction";
	}
	
	private static int getDirection(JsonObject json, String name) {
		JsonElement elem = json.get(name);
		if (elem == null) return -1;
		else return elem.getAsInt();
	}
}
