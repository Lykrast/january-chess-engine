package com.nullprogram.chess;

import java.awt.Image;

import com.nullprogram.chess.Piece.Side;
import com.nullprogram.chess.resources.ImageServer;

public class Model {
    /** Name of this piece. */
    private String name;
    
    private boolean royal;
    
    private double value;
    private IMoveType[] moves;
    
    public Model(String name, double value, IMoveType... moves)
    {
    	this(name, value, false, moves);
    }
    
    public Model(String name, double value, boolean royal, IMoveType... moves)
    {
    	this.name = name;
    	this.value = value;
    	this.moves = moves;
    	this.royal = royal;
    }
    
    public double getValue()
    {
    	return value;
    }
    
    public String getName()
    {
    	return name;
    }
    
    /**
     * If the piece is considered royal and cannot be put in check.
     */
    public boolean isRoyal()
    {
    	return royal;
    }

    /**
     * Get the moves for this piece.
     *
     * @param checkCheck check for check
     * @return           list of moves
     */
    public MoveList getMoves(final Piece p, boolean checkCheck)
    {
        IMoveList list = new MoveList(p.getBoard(), checkCheck);
        for (IMoveType m : moves) list = m.getMoves(p, list);
        return (MoveList)list;
    }

    /**
     * Get the image that represents this piece.
     *
     * This method currently uses reflection.
     *
     * @return     image for this piece
     */
    public final Image getImage(Side s) {
        return ImageServer.getPieceTile(name, s);
    }

}
