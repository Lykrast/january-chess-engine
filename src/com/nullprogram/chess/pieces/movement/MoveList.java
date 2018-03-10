package com.nullprogram.chess.pieces.movement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.nullprogram.chess.Move;
import com.nullprogram.chess.Position;
import com.nullprogram.chess.boards.Board;
import com.nullprogram.chess.pieces.Piece;

/**
 * Safe list of moves.
 *
 * Before a move is added it can be checked for some basic validity.
 */
public class MoveList implements Iterable<Move>, Serializable, IMoveList {

    /** Versioning for object serialization. */
    private static final long serialVersionUID = -25601206293390593L;

    /** The board used to verify positions before adding them. */
    private Board board;

    /** Should we check for check when verifying moves. */
    private boolean check;

    /** The actual list of moves. */
    private final List<Move> moves = new ArrayList<Move>();

    /**
     * Create a new move list relative to a board.
     *
     * @param verifyBoard the board to be used
     */
    public MoveList(final Board verifyBoard) {
        this(verifyBoard, true);
    }

    /**
     * Create a new move list relative to a board.
     *
     * @param checkCheck  check for check
     * @param verifyBoard the board to be used
     */
    public MoveList(final Board verifyBoard, final boolean checkCheck) {
        board = verifyBoard;
        check = checkCheck;
    }
    
    /* (non-Javadoc)
	 * @see com.nullprogram.chess.IMoveList#checksCheck()
	 */
    @Override
	public boolean checksCheck()
    {
    	return check;
    }

    /* (non-Javadoc)
	 * @see com.nullprogram.chess.IMoveList#add(com.nullprogram.chess.Move)
	 */
    @Override
	public final boolean add(final Move move) {
        moves.add(move);
        return true;
    }

    /**
     * Add a collection of moves to this one.
     * @param list a collection of moves
     * @return true
     */
    public final boolean addAll(final Iterable<Move> list) {
        for (Move move : list) {
            moves.add(move);
        }
        return true;
    }

    /* (non-Javadoc)
	 * @see com.nullprogram.chess.IMoveList#add(com.nullprogram.chess.Move, com.nullprogram.chess.MoveType.MoveMode)
	 */
    @Override
	public final boolean add(final Move move, MoveType.MoveMode type)
    {
    	Position dest = move.getDest();
    	if (!board.inRange(dest)) return false;
    	
    	if (board.isEmpty(dest))
    	{
    		if (type.move() && !causesCheck(move)) add(move);
    		//Allows sliding moves to behave "normally" even when they're only supposed to capture
    		return true;
    	}
    	else
    	{
            Piece p = board.getPiece(move.getOrigin());
            //Enemy
    		if (board.getPiece(dest).getSide() != p.getSide())
    		{
    			if (type.captureEnemy())
    			{
    				if (!causesCheck(move)) add(move);
    				return true;
    			}
    			else return false;
    		}
    		//Friendly
    		else
    		{
    			if (type.captureFriendly())
    			{
    				if (!causesCheck(move)) add(move);
    				return true;
    			}
    			else return false;
    		}
    	}
    }

    /* (non-Javadoc)
	 * @see com.nullprogram.chess.IMoveList#addMove(com.nullprogram.chess.Move)
	 */
    @Override
	public final boolean addMove(final Move move) {
        if (board.isFree(move.getDest())) {
            if (!causesCheck(move)) {
                add(move);
                return true;
            }
            return true; // false only for a "blocking" move
        }
        return false;
    }

    /* (non-Javadoc)
	 * @see com.nullprogram.chess.IMoveList#addCapture(com.nullprogram.chess.Move)
	 */
    @Override
	public final boolean addCapture(final Move move) {
        Piece p = board.getPiece(move.getOrigin());
        if (board.isFree(move.getDest(), p.getSide())) {
            if (!causesCheck(move)) {
                add(move);
                return true;
            }
            return true; // false only for a "blocking" move
        }
        return false;
    }

    /* (non-Javadoc)
	 * @see com.nullprogram.chess.IMoveList#addCaptureOnly(com.nullprogram.chess.Move)
	 */
    @Override
	public final boolean addCaptureOnly(final Move move) {
        Piece p = board.getPiece(move.getOrigin());
        if (board.isEnemy(move.getDest(), p.getSide()) &&
            !causesCheck(move)) {

            add(move);
            return true;
        }
        return false;
    }

    /**
     * Determine if move will cause check for the same side.
     *
     * @param move move to be tested
     * @return     true if move causes check
     */
    private boolean causesCheck(final Move move) {
        if (!check) {
            return false;
        }
        Piece p = board.getPiece(move.getOrigin());
        boolean ret = false;
        
        board.move(move);
        
        //Check if we captured our king
        Piece captured = board.last().getCaptured();
        if (captured != null)
        {
        	if (captured.getSide() == p.getSide() && captured.getModel().isRoyal())
        	{
        		//Any captured king is a no
        		if (!board.getGameMode().checkMultiple()) ret = true;
    			//Accept if we have other kings left
        		else ret = board.findRoyal(p.getSide()).isEmpty();
        	}
        }
        //If we didn't capture our king or captured a "valid" one
        if (!ret) ret = board.check(p.getSide());
        
        board.undo();
        return ret;
    }

    /**
     * Return true if this list contains the position as a destination.
     *
     * @param pos destination position
     * @return    true if destination is present in list
     */
    public final boolean containsDest(final Position pos) {
        for (Move move : this) {
            if (pos.equals(move.getDest())) {
                return true;
            }
        }
        return false;
    }


    /**
     * Return true if this list contains a move that will capture the given position.
     *
     * @param pos destination position
     * @return    true if destination is present in list
     */
    public final boolean capturesPos(final Position pos) {
        for (Move move : this) {
            if (move.captures(pos)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the move containing the destination.
     *
     * @param dest destination position
     * @return     move containing given destination
     */
    public final Move getMoveByDest(final Position dest) {
        for (Move move : this) {
            if (dest.equals(move.getDest())) {
                return move;
            }
        }
        return null;
    }

    /**
     * Get all moves containing the destination.
     *
     * @param dest destination position
     * @return all moves containing given destination
     */
    public final List<Move> getAllMovesByDest(final Position dest) {
    	List<Move> list = new ArrayList<>();
        for (Move move : this) {
            if (dest.equals(move.getDest())) {
            	list.add(move);
            }
        }
        return list;
    }

    /**
     * Stack behavior: pop off the last element on return it.
     *
     * @return move popped off the stack
     */
    public final Move pop() {
        if (isEmpty()) {
            return null;
        }
        Move last = moves.get(moves.size() - 1);
        moves.remove(moves.size() - 1);
        return last;
    }

    /**
     * Stack behavior: peek at last element.
     *
     * @return move at the top of the stack
     */
    public final Move peek() {
        if (isEmpty()) {
            return null;
        }
        return moves.get(moves.size() - 1);
    }

    /**
     * Get the number of moves in this list.
     * @return the number of moves in this list
     */
    public final int size() {
        return moves.size();
    }

    /**
     * Determine if this move list is empty.
     * @return true if empty
     */
    public final boolean isEmpty() {
        return moves.isEmpty();
    }

    /**
     * Shuffle the order of the moves in this list.
     */
    public final void shuffle() {
        Collections.shuffle(moves);
    }

    @Override
    public final Iterator<Move> iterator() {
        return moves.iterator();
    }
}
