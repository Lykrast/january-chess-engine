package com.nullprogram.chess;

import java.awt.Image;

import com.nullprogram.chess.Piece.Side;
import com.nullprogram.chess.pieces.ImageServer;

public abstract class Model {
    /** Name of this piece. */
    private String name;
    
    private double value;
    
    public Model(String name, double value)
    {
    	this.name = name;
    	this.value = value;
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
     * Get the moves for this piece.
     *
     * @param checkCheck check for check
     * @return           list of moves
     */
    public abstract MoveList getMoves(final Piece p, boolean checkCheck);

    /**
     * Get the image that represents this piece.
     *
     * This method currently uses reflection.
     *
     * @return     image for this piece
     */
    public final Image getImage(Side s) {
        return ImageServer.getTile(name + "-" + s);
    }

}
