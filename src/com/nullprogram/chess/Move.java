package com.nullprogram.chess;

import com.nullprogram.chess.pieces.Piece;
import com.nullprogram.chess.pieces.PieceRegistry;

/**
 * Represents a single moves on a chess board.
 *
 * This is actually a linked list able to describe several movement
 * actions at once that make up a single turn (castling, for example).
 *
 * If destination position is null, it means to remove the piece in
 * the origin position.
 */
public final class Move {

    /** Originating position. */
    public final Position origin;

    /** Destination position. */
    public final Position destination;

    /** Linked list entry for next part of this move. */
    private Move next;

    /** Piece that was capture: used for undoing a move.  */
    private Piece captured;

    /** New piece to make. */
    private String replacement;

    /** Side of the new piece to make. */
    private Piece.Side replacementSide;

    /** Score for this move. IA use only. */
    public double score;
    
    /** Marks this move as "special", currently this only changes the color it is displayed for clarity */
    private boolean special = false;

    /** Marks this move as a swap, meaning pieces swap places instead of being captured */
    private boolean swap = false;

    /**
     * Create a new move to move a piece from one position to another.
     *
     * @param orig origin position
     * @param dest destination position
     */
    public Move(final Position orig, final Position dest) {
        destination = dest;
        origin = orig;
        next = null;
    }

    /**
     * Create a Move from an existing Move.
     *
     * @param move move to copy
     */
    @Deprecated
    public Move(final Move move) {
        this(move.origin, move.destination);
        captured = move.getCaptured();
        replacement = move.getReplacement();
        replacementSide = move.getReplacementSide();
        special = move.special;
        swap = move.swap;
        if (move.getNext() != null) {
            next = new Move(move.getNext());
        }
    }


    /**
     * Create a copy of this Move.
     * 
     * @return a copy of this Move and all of its following moves in the sequence
     */
    public Move copy() {
    	return new Move(this);
    }
    
    @Override
    public boolean equals(Object o) {
    	if (this == o) return true;
    	if (o == null || !(o instanceof Move)) return false;
    	Move move = (Move)o;
    	
    	//Simple flags
    	if (special != move.special || swap != move.swap) return false;
    	
    	//Start and destination
    	if (!equalsCheckNull(destination, move.destination) || !equalsCheckNull(origin, move.origin)) return false;
    	
    	//Replacement
    	if (replacement != null) {
    		if (!replacement.equals(move.replacement) || replacementSide != move.replacementSide) return false;
    	}
    	else if (move.replacement != null) return false;
    	
    	//Captured piece
    	if (!equalsCheckNull(captured, move.captured)) return false;
    	
    	//Deep equality
    	return equalsCheckNull(next, move.next);
    }
    
    private boolean equalsCheckNull(Object a, Object b) {
    	return a == null ? b == null : a.equals(b);
    }

    /**
     * Set the next movement action for this move.
     *
     * @param move the next move
     */
    public void setNext(final Move move) {
        next = move;
    }

    /**
     * Get the next movement action for this move.
     *
     * @return the next move
     */
    public Move getNext() {
        return next;
    }

    /**
     * Get the last movement action of this move. Returns itself if this is already the last one.
     *
     * @return the last movement action of this move
     */
    public Move getLast() {
    	if (next == null) return this;
    	else return next.getLast();
    }
    
    /**
     * Get the position where a capture could have happened.
     * 
     * @return position where a piece could have been captured
     */
    public Position getCaptureDest() {
    	if (origin == null) return null;
    	if (destination == null) return origin;
    	return destination;
    }

    /**
     * Set the piece that was captured by this move.
     *
     * @param p piece that was captured
     */
    public void setCaptured(final Piece p) {
        captured = p;
    }

    /**
     * Set the piece that was captured by this move.
     *
     * @return piece that was captured
     */
    public Piece getCaptured() {
        return captured;
    }

    /**
     * Set the piece that will be created.
     *
     * @param pieceName piece to be created
     */
    public void setReplacement(final String pieceName) {
        replacement = pieceName;
    }

    /**
     * Get the name of the piece that will be created.
     *
     * @return piece to be created
     */
    public String getReplacement() {
        return replacement;
    }

    /**
     * Set the side of piece that will be created.
     *
     * @param side piece to be created
     */
    public void setReplacementSide(final Piece.Side side) {
        replacementSide = side;
    }

    /**
     * Get the side of the piece that will be created.
     *
     * @return side of piece to be created
     */
    public Piece.Side getReplacementSide() {
        return replacementSide;
    }
    
    /**
     * Check if that moves captures the given position at some point.
     * 
     * @param pos position to check
     * @return true if the move captures the position somewhere in the sequence
     */
    public boolean captures(Position pos) {
    	//New piece
    	if (origin == null) return false;
    	
    	//Capture without movement
    	if (destination == null) {
    		if (pos.equals(origin)) return true;
    	}
    	//Replacement
    	else if (pos.equals(destination))
    	{
    		//Swapping means whatever we're looking for is now at the origin
    		if (swap) return next == null ? false : next.captures(origin);
    		else return true;
    	}
    	
    	//Recursive check
    	return next == null ? false : next.captures(pos);
    }

    @Override
    public String toString() {
    	String s;
    	
    	//Normal move
    	if (origin != null && destination != null)
    	{
    		if (swap) s = origin + "<->" + destination;
    		else s = "" + origin + destination;
    	}
    	//Capture without move
    	else if (origin != null && destination == null)
    	{
    		s = "x" + origin;
    	}
    	//New piece
    	else
    	{
    		s = destination + " " + PieceRegistry.get(replacement).getName();
    	}
    	
    	if (next != null) s += " - " + next.toString();
    	
        return s;
    }
    
    /**
     * Force mark this move as "special" or don't. This is currently only used to change the color the move is displayed for clarity.
     * @param value true to make it special, false to make it no longer special
     */
    public void setSpecial(boolean value) {
    	special = value;
    }
    
    /**
     * Whether or not this move is marked as "special". This is currently only used to change the color the move is displayed for clarity.
     * <br>
     * A move that has other moves in its sequence, or that is a swap is always considered "special".
     * @return whether this move is marked as "special" or not
     */
    public boolean isSpecial() {
    	return special || swap || next != null;
    }
    
    /**
     * Set this move as a swap or not. A swap means that instead of capturing what it lands on, it swaps positions with it.
     * @param value true to make it a swap, false to make it not a swap
     */
    public void setSwap(boolean value) {
    	swap = value;
    }
    
    /**
     * Whether or not this move is a swap. A swap means that instead of capturing what it lands on, it swaps positions with it.
     * @return whether this move is a swap or not
     */
    public boolean isSwap() {
    	return swap;
    }
}
