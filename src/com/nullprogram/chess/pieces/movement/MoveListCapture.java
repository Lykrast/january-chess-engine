package com.nullprogram.chess.pieces.movement;

import java.io.Serializable;
import java.util.ArrayList;
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
public class MoveListCapture implements Iterable<Move>, Serializable, IMoveList {

    /** Versioning for object serialization. */
	private static final long serialVersionUID = 4094244408264595675L;

	/** The board used to verify positions before adding them. */
    private Board board;

    /** The actual list of moves. */
    private final List<Move> moves = new ArrayList<Move>();

    /**
     * Create a new move list relative to a board.
     *
     * @param verifyBoard the board to be used
     */
    public MoveListCapture(final Board verifyBoard) {
        board = verifyBoard;
    }
    
    /* (non-Javadoc)
	 * @see com.nullprogram.chess.IMoveList#checksCheck()
	 */
    @Override
	public boolean checksCheck()
    {
    	return false;
    }

    /* (non-Javadoc)
	 * @see com.nullprogram.chess.IMoveList#add(com.nullprogram.chess.Move)
	 */
    @Override
	public final boolean add(final Move move) {
        moves.add(move);
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
    	
    	//Add the move to the list if it could capture something
    	if (type.captureEnemy()) add(move);
    	//Simulate the return from a normal MoveList
    	if (board.isEmpty(dest)) return true;
    	else
    	{
            Piece p = board.getPiece(move.getOrigin());
            //Enemy
    		if (board.getPiece(dest).getSide() != p.getSide())
    		{
    			if (type.captureEnemy()) return true;
    			else return false;
    		}
    		//Friendly
    		else
    		{
    			if (type.captureFriendly()) return true;
    			else return false;
    		}
    	}
    }

    /* (non-Javadoc)
	 * @see com.nullprogram.chess.IMoveList#addMove(com.nullprogram.chess.Move)
	 */
    @Override
	public final boolean addMove(final Move move) {
        return board.isFree(move.getDest());
    }

    /* (non-Javadoc)
	 * @see com.nullprogram.chess.IMoveList#addCapture(com.nullprogram.chess.Move)
	 */
    @Override
	public final boolean addCapture(final Move move) {
        Piece p = board.getPiece(move.getOrigin());
        add(move);
        return board.isFree(move.getDest(), p.getSide());
    }

    /* (non-Javadoc)
	 * @see com.nullprogram.chess.IMoveList#addCaptureOnly(com.nullprogram.chess.Move)
	 */
    @Override
	public final boolean addCaptureOnly(final Move move) {
        Piece p = board.getPiece(move.getOrigin());
        add(move);
        return board.isEnemy(move.getDest(), p.getSide());
    }

    /**
     * Determine if this move list is empty.
     * @return true if empty
     */
    public final boolean isEmpty() {
        return moves.isEmpty();
    }

    @Override
    public final Iterator<Move> iterator() {
        return moves.iterator();
    }
}
