package com.nullprogram.chess.boards;

import com.nullprogram.chess.Board;
import com.nullprogram.chess.GameMode;
import com.nullprogram.chess.Piece;
import com.nullprogram.chess.Position;

/**
 * The board for a standard game of chess.
 */
public class StandardBoard extends Board {

    /** Serialization identifier. */
    private static final long serialVersionUID = -484123716L;

    /**
     * The standard chess board.
     */
    public StandardBoard() {
    	super(GameModeRegistry.get("empty"));
    }
    
    public StandardBoard(GameMode mode)
    {
    	super(mode);
    }

    @Override
    public final Boolean checkmate(final Piece.Side side) {
        return check(side) && (moveCount(side) == 0);
    }

    @Override
    public final Boolean stalemate(final Piece.Side side) {
        return (!check(side)) && (moveCount(side) == 0);
    }

    /**
     * Number of moves available to this player.
     *
     * @param side side to be tested
     * @return     number of moves right now
     */
    public final int moveCount(final Piece.Side side) {
        int count = 0;
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                Piece p = getPiece(new Position(x, y));
                if ((p != null) && (p.getSide() == side)) {
                    count += p.getMoves(true).size();
                }
            }
        }
        return count;
    }

    @Override
    public final Boolean check(final Piece.Side side) {
        Piece.Side attacker;
        if (side == Piece.Side.WHITE) {
            attacker = Piece.Side.BLACK;
        } else {
            attacker = Piece.Side.WHITE;
        }
        Position kingPos = findKing(side);
        if (kingPos == null) {
            /* no king on board, but can happen in AI evaluation */
            return false;
        }
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                Piece p = getPiece(new Position(x, y));
                if ((p != null) &&
                    (p.getSide() == attacker) &&
                    p.getMoves(false).capturesPos(kingPos)) {

                    return true;
                }
            }
        }
        return false;
    }
}
