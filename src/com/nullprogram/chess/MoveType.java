package com.nullprogram.chess;

import com.nullprogram.chess.pieces.*;
import com.nullprogram.chess.resources.IMoveTypeDeserializer;
import com.nullprogram.chess.resources.MoveTypeDeserializer;

public abstract class MoveType implements IMoveTypeDeserializer {
	private MoveMode mode;
	
	protected MoveType(MoveMode mode)
	{
		this.mode = mode;
	}
	
	protected MoveMode getMoveMode() {
		return mode;
	}

    /**
     * Determine moves for given situation.
     *
     * @param p     the piece being tested
     * @param list  list to be appended to
     * @return      the modified list
     */
    public abstract IMoveList getMoves(final Piece p, final IMoveList list);
    
    /**
     * Registers all deserializers for Json loading.
     */
    public static void registerDeserializers()
    {
    	//Generics
    	MoveTypeDeserializer.registerDeserializer(new MoveTypeLeaper(MoveMode.MOVE_CAPTURE, 0,0));
    	MoveTypeDeserializer.registerDeserializer(new MoveTypeLeaperLame(MoveMode.MOVE_CAPTURE, 0,0));
    	MoveTypeDeserializer.registerDeserializer(new MoveTypeLeaperOrthogonal(MoveMode.MOVE_CAPTURE, 0));
    	MoveTypeDeserializer.registerDeserializer(new MoveTypeLeaperDiagonal(MoveMode.MOVE_CAPTURE, 0));
    	MoveTypeDeserializer.registerDeserializer(new MoveTypeRider(MoveMode.MOVE_CAPTURE, 0,0));
    	MoveTypeDeserializer.registerDeserializer(new MoveTypeRiderCircular(MoveMode.MOVE_CAPTURE, 0,0));
    	
    	//Modifiers
    	MoveTypeDeserializer.registerDeserializer(new MoveModifierPromotionSingle(null, "", 0));
    	MoveTypeDeserializer.registerDeserializer(new MoveModifierPromotion(null, 0));
    	
    	//Presets
    	MoveTypeDeserializer.registerDeserializer(new MoveTypeWazir(MoveMode.MOVE_CAPTURE));
    	MoveTypeDeserializer.registerDeserializer(new MoveTypeFerz(MoveMode.MOVE_CAPTURE));
    	MoveTypeDeserializer.registerDeserializer(new MoveTypeDabbaba(MoveMode.MOVE_CAPTURE));
    	MoveTypeDeserializer.registerDeserializer(new MoveTypeAlfil(MoveMode.MOVE_CAPTURE));
    	
    	MoveTypeDeserializer.registerDeserializer(new MoveTypeKnight(MoveMode.MOVE_CAPTURE));
    	MoveTypeDeserializer.registerDeserializer(new MoveTypeKing(MoveMode.MOVE_CAPTURE));
    	MoveTypeDeserializer.registerDeserializer(new MoveTypeRook(MoveMode.MOVE_CAPTURE));
    	MoveTypeDeserializer.registerDeserializer(new MoveTypeBishop(MoveMode.MOVE_CAPTURE));
    	
    	//Specialized
    	MoveTypeDeserializer.registerDeserializer(new MoveTypePawn(1));
    	MoveTypeDeserializer.registerDeserializer(new MoveTypeCastle());
    	MoveTypeDeserializer.registerDeserializer(new MoveTypeCannon());
    	MoveTypeDeserializer.registerDeserializer(new MoveTypeVao());
    	MoveTypeDeserializer.registerDeserializer(new MoveTypeGrasshopper(MoveMode.MOVE_CAPTURE));
    	MoveTypeDeserializer.registerDeserializer(new MoveTypeLocust());
    	MoveTypeDeserializer.registerDeserializer(new MoveTypeEdgehog(MoveMode.MOVE_CAPTURE));
    	MoveTypeDeserializer.registerDeserializer(new MoveTypeBishopReflecting(MoveMode.MOVE_CAPTURE));
    	MoveTypeDeserializer.registerDeserializer(new MoveTypeAdvancer());
    	MoveTypeDeserializer.registerDeserializer(new MoveTypeWithdrawer());
    }
    
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

}
