package com.nullprogram.chess;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.pieces.*;
import com.nullprogram.chess.resources.IMoveTypeDeserializer;
import com.nullprogram.chess.resources.MoveTypeDeserializer;

/**
 * A concrete MoveType with a direction and a capture mode.
 * @author Lykrast
 */
public abstract class MoveType implements IMoveTypeDeserializer, IMoveType {
	private MoveMode moveMode;
	private DirectionMode directionMode;
	
	protected MoveType(MoveMode moveMode, DirectionMode directionMode)
	{
		this.moveMode = moveMode;
		this.directionMode = directionMode;
	}
	
	protected MoveMode getMoveMode() {
		return moveMode;
	}
	
	protected DirectionMode getDirectionMode() {
		return directionMode;
	}
	

	/**
	 * Creates a MoveType according to the given JsonElement following the (already deserialized) MoveMode and DirectionMode.
	 * @param json Json to deserialize
	 * @param moveMode MoveMode to follow
	 * @param directionMode DirectionMode to follow
	 * @param context TODO
	 * @return a MoveType created with the given MoveMode
	 */
	protected abstract MoveType create(JsonObject json, MoveMode moveMode, DirectionMode directionMode, JsonDeserializationContext context) throws JsonParseException;
	
	@Override
	public IMoveType create(JsonObject json, JsonDeserializationContext context) throws JsonParseException
	{
		JsonObject obj = json.getAsJsonObject();
		
		JsonElement modeJson = obj.get("mode");
		MoveType.MoveMode mode = MoveType.MoveMode.MOVE_CAPTURE;
		if (modeJson != null)
		{
			mode = MoveType.MoveMode.fromString(modeJson.getAsString());
		}
		
		return create(obj, mode, DirectionMode.fromJson(obj.get("direction")), context);
	}

    /* (non-Javadoc)
	 * @see com.nullprogram.chess.IMoveType#getMoves(com.nullprogram.chess.Piece, com.nullprogram.chess.IMoveList)
	 */
    @Override
	public abstract IMoveList getMoves(final Piece p, final IMoveList list);
    
    /**
     * Registers all deserializers for Json loading.
     */
    public static void registerDeserializers()
    {
    	//Generics
    	MoveTypeDeserializer.registerDeserializer(new MoveTypeLeaper(MoveMode.MOVE_CAPTURE, DirectionMode.ALL, 0, 0));
    	MoveTypeDeserializer.registerDeserializer(new MoveTypeLeaperNarrow(MoveMode.MOVE_CAPTURE, DirectionMode.ALL, 0, 0));
    	MoveTypeDeserializer.registerDeserializer(new MoveTypeLeaperWide(MoveMode.MOVE_CAPTURE, DirectionMode.ALL, 0, 0));
    	MoveTypeDeserializer.registerDeserializer(new MoveTypeLeaperLame(MoveMode.MOVE_CAPTURE, DirectionMode.ALL, 0, 0));
    	MoveTypeDeserializer.registerDeserializer(new MoveTypeLeaperOrthogonal(MoveMode.MOVE_CAPTURE, DirectionMode.ALL, 0));
    	MoveTypeDeserializer.registerDeserializer(new MoveTypeLeaperDiagonal(MoveMode.MOVE_CAPTURE, DirectionMode.ALL, 0));
    	MoveTypeDeserializer.registerDeserializer(new MoveTypeRider(MoveMode.MOVE_CAPTURE, DirectionMode.ALL, 0, 0, 0));
    	MoveTypeDeserializer.registerDeserializer(new MoveTypeRiderCircular(MoveMode.MOVE_CAPTURE, 0,0));
    	MoveTypeDeserializer.registerDeserializer(new MoveTypeRiderBent(MoveMode.MOVE_CAPTURE, DirectionMode.ALL, 0, 0, 0, 0, 0));
    	MoveTypeDeserializer.registerDeserializer(new MoveTypeRiderCrooked(MoveMode.MOVE_CAPTURE, DirectionMode.ALL, 0, 0));
    	
    	//Modifiers
    	MoveTypeDeserializer.registerDeserializer(new MoveModifierPromotionSingle(null, "", 0));
    	MoveTypeDeserializer.registerDeserializer(new MoveModifierPromotion(null, 0));
    	MoveTypeDeserializer.registerDeserializer(new MoveModifierRestriction(null, 0, 0, 0, 0, 0));
    	MoveTypeDeserializer.registerDeserializer(new MoveModifierCaptureRoyal(null));
    	MoveTypeDeserializer.registerDeserializer(new MoveModifierCaptureNonRoyal(null));
    	MoveTypeDeserializer.registerDeserializer(new MoveModifierCoordinator(null));
    	
    	//Presets
    	MoveTypeDeserializer.registerDeserializer(new MoveTypeWazir(MoveMode.MOVE_CAPTURE, DirectionMode.ALL));
    	MoveTypeDeserializer.registerDeserializer(new MoveTypeFerz(MoveMode.MOVE_CAPTURE, DirectionMode.ALL));
    	MoveTypeDeserializer.registerDeserializer(new MoveTypeDabbaba(MoveMode.MOVE_CAPTURE, DirectionMode.ALL));
    	MoveTypeDeserializer.registerDeserializer(new MoveTypeAlfil(MoveMode.MOVE_CAPTURE, DirectionMode.ALL));
    	
    	MoveTypeDeserializer.registerDeserializer(new MoveTypeKnight(MoveMode.MOVE_CAPTURE, DirectionMode.ALL));
    	MoveTypeDeserializer.registerDeserializer(new MoveTypeKing(MoveMode.MOVE_CAPTURE, DirectionMode.ALL));
    	MoveTypeDeserializer.registerDeserializer(new MoveTypeRook(MoveMode.MOVE_CAPTURE, DirectionMode.ALL, 0));
    	MoveTypeDeserializer.registerDeserializer(new MoveTypeBishop(MoveMode.MOVE_CAPTURE, DirectionMode.ALL, 0));
    	
    	//Specialized
    	MoveTypeDeserializer.registerDeserializer(new MoveTypePawn(1));
    	MoveTypeDeserializer.registerDeserializer(new MoveTypeCastle());
    	MoveTypeDeserializer.registerDeserializer(new MoveTypeCannon(DirectionMode.ALL));
    	MoveTypeDeserializer.registerDeserializer(new MoveTypeVao(DirectionMode.ALL));
    	MoveTypeDeserializer.registerDeserializer(new MoveTypeGrasshopper(MoveMode.MOVE_CAPTURE, DirectionMode.ALL));
    	MoveTypeDeserializer.registerDeserializer(new MoveTypeLocust(DirectionMode.ALL));
    	MoveTypeDeserializer.registerDeserializer(new MoveTypeEdgehog(MoveMode.MOVE_CAPTURE, DirectionMode.ALL));
    	MoveTypeDeserializer.registerDeserializer(new MoveTypeBishopReflecting(MoveMode.MOVE_CAPTURE, DirectionMode.ALL));
    	MoveTypeDeserializer.registerDeserializer(new MoveTypeAdvancer(DirectionMode.ALL));
    	MoveTypeDeserializer.registerDeserializer(new MoveTypeWithdrawer(DirectionMode.ALL));
    	MoveTypeDeserializer.registerDeserializer(new MoveTypeCheckers(MoveMode.MOVE_CAPTURE, DirectionMode.ALL, true));
    }
    
    /**
     * Tell if the move can capture and move without capturing or not.
     * @author Lykrast
     */
    public static enum MoveMode {
    	MOVE_CAPTURE,
    	MOVE,
    	CAPTURE;
    	
    	/**
    	 * Return a MoveMode corresponding to the given string.
    	 * <br>
    	 * This is not case sensitive, and accepted values are "move", "capture" and "move_capture".
    	 * @param s string to convert
    	 * @return MoveMode corresponding to the string, or null
    	 */
    	public static MoveMode fromString(String s)
    	{
    		if (s.equalsIgnoreCase("move"))
    		{
    			return MOVE;
    		}
    		
    		if (s.equalsIgnoreCase("capture"))
    		{
    			return CAPTURE;
    		}
    		
    		if (s.equalsIgnoreCase("move_capture"))
    		{
    			return MOVE_CAPTURE;
    		}
    		
    		return null;
    	}
    }
    
    /**
     * A simple object to tell what direction the move can go towards.
     * @author Lykrast
     */
    public static class DirectionMode {
    	private boolean forward, back, left, right;
    	private static final String STR_ALL = "ALL", 
    			STR_HORIZONTAL = "HORIZONTAL",
    			STR_VERTICAL = "VERTICAL",
    			STR_FORWARD = "FORWARD", 
    	    	STR_BACK = "BACK", 
    	    	STR_LEFT = "LEFT", 
    	    	STR_RIGHT = "RIGHT";
    	//A few constants to save up a lil bit of memory when common ones are used
    	//Probably not worth it
    	public static final DirectionMode ALL = new DirectionMode(true, true, true, true),
    			HORIZONTAL = new DirectionMode(false, false, true, true), 
    			VERTICAL = new DirectionMode(true, true, false, false), 
    			FORWARD = new DirectionMode(true, false, false, false), 
    			BACK = new DirectionMode(true, false, false, false), 
    			LEFT = new DirectionMode(true, false, false, false),
    			RIGHT = new DirectionMode(true, false, false, false);
    	private static final String ERROR = "Invalid direction : %s - must be " + 
				STR_ALL+", "+STR_HORIZONTAL+", "+STR_VERTICAL+", "+STR_FORWARD+", "+STR_BACK+", "+STR_LEFT+" or "+STR_RIGHT;

		public DirectionMode(boolean forward, boolean back, boolean left, boolean right) {
			this.forward = forward;
			this.back = back;
			this.left = left;
			this.right = right;
		}
		
		public boolean forward() { return forward; }
		public boolean back() { return back; }
		public boolean left() { return left; }
		public boolean right() { return right; }
		
		public static DirectionMode fromJson(JsonElement json) throws JsonParseException
		{
			if (json == null) return ALL;
			
			//Single direction
			if (json.isJsonPrimitive())
			{
				String sJson = json.getAsString();
				if (sJson.equals(STR_ALL)) return ALL;
				else if (sJson.equals(STR_HORIZONTAL)) return HORIZONTAL;
				else if (sJson.equals(STR_VERTICAL)) return VERTICAL;
				else if (sJson.equals(STR_FORWARD)) return FORWARD;
				else if (sJson.equals(STR_BACK)) return BACK;
				else if (sJson.equals(STR_LEFT)) return LEFT;
				else if (sJson.equals(STR_RIGHT)) return RIGHT;
				else throw new JsonParseException(String.format(ERROR, sJson));
			}
			
			//Multiple directions
			JsonArray array = json.getAsJsonArray();
			DirectionMode mode = new DirectionMode(false, false, false, false);
			for (JsonElement e : array)
			{
				String sJson = e.getAsString();
				if (sJson.equals(STR_ALL)) return ALL;
				else if (sJson.equals(STR_HORIZONTAL)) {
					mode.left = true;
					mode.right = true;
				}
				else if (sJson.equals(STR_VERTICAL)) {
					mode.forward = true;
					mode.back = true;
				}
				else if (sJson.equals(STR_FORWARD)) mode.forward = true;
				else if (sJson.equals(STR_BACK)) mode.back = true;
				else if (sJson.equals(STR_LEFT)) mode.left = true;
				else if (sJson.equals(STR_RIGHT)) mode.right = true;
				else throw new JsonParseException(String.format(ERROR, sJson));
			}
			
			return mode;
		}
    }

}
