package com.nullprogram.chess;

import com.nullprogram.chess.pieces.MoveTypeBishop;
import com.nullprogram.chess.pieces.MoveTypeCastle;
import com.nullprogram.chess.pieces.MoveTypeKing;
import com.nullprogram.chess.pieces.MoveTypeKnight;
import com.nullprogram.chess.pieces.MoveTypeLeaper;
import com.nullprogram.chess.pieces.MoveTypePawn;
import com.nullprogram.chess.pieces.MoveTypeRook;
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
    public abstract MoveList getMoves(final Piece p, final MoveList list);
    
    /**
     * Registers all deserializers for Json loading.
     */
    public static void registerDeserializers()
    {
    	MoveTypeDeserializer.registerDeserializer("Pawn", new MoveTypePawn());
    	MoveTypeDeserializer.registerDeserializer("Rook", new MoveTypeRook(MoveMode.MOVE_CAPTURE));
    	MoveTypeDeserializer.registerDeserializer("Leaper", new MoveTypeLeaper(MoveMode.MOVE_CAPTURE, 0,0));
    	MoveTypeDeserializer.registerDeserializer("Knight", new MoveTypeKnight(MoveMode.MOVE_CAPTURE));
    	MoveTypeDeserializer.registerDeserializer("Bishop", new MoveTypeBishop(MoveMode.MOVE_CAPTURE));
    	MoveTypeDeserializer.registerDeserializer("King", new MoveTypeKing(MoveMode.MOVE_CAPTURE));
    	MoveTypeDeserializer.registerDeserializer("Castle", new MoveTypeCastle());
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
