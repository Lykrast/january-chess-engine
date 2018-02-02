package com.nullprogram.chess;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import com.nullprogram.chess.Piece.Side;
import com.nullprogram.chess.pieces.ImageServer;

public class Model {
    /** Name of this piece. */
    private String name;
    
    private double value;
    private List<MoveType> moves;
    
    public Model(String name, double value)
    {
    	this(name, value, new MoveType() {

			@Override
			public MoveList getMoves(Piece p, MoveList list) {
				return list;
			}
    	});
    }
    
    public Model(String name, double value, MoveType... moves)
    {
    	this.name = name;
    	this.value = value;
    	this.moves = new ArrayList<MoveType>();
    	for (MoveType m : moves) this.moves.add(m);
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
    public MoveList getMoves(final Piece p, boolean checkCheck)
    {
        MoveList list = new MoveList(p.getBoard(), checkCheck);
        for (MoveType m : moves) list = m.getMoves(p, list);
        return list;
    }

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
