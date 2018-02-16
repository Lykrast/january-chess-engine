package com.nullprogram.chess.boards;

import com.nullprogram.chess.Board;
import com.nullprogram.chess.GameMode;
import com.nullprogram.chess.Model;
import com.nullprogram.chess.Piece;
import com.nullprogram.chess.Position;
import com.nullprogram.chess.pieces.PieceRegistry;

/**
 * The board for a standard game of chess.
 */
public class StandardBoard extends Board {

    /** Serialization identifier. */
    private static final long serialVersionUID = -484123716L;

    /** The standard board width. */
    static final int WIDTH = 8;

    /** The standard board height. */
    static final int HEIGHT = 8;

    /** Row of the white pawns. */
    static final int WHITE_PAWN_ROW = 1;

    /** Row of the black pawns. */
    static final int BLACK_PAWN_ROW = 6;

    /** White home row. */
    static final int WHITE_ROW = 0;

    /** Black home row. */
    static final int BLACK_ROW = 7;

    /** Queen side rook column. */
    static final int Q_ROOK = 0;

    /** Queen side knight column. */
    static final int Q_KNIGHT = 1;

    /** Queen side bishop column. */
    static final int Q_BISHOP = 2;

    /** Queen column. */
    static final int QUEEN = 3;

    /** King column. */
    static final int KING = 4;

    /** King side bishop column. */
    static final int K_BISHOP = 5;

    /** King side knight column. */
    static final int K_KNIGHT = 6;

    /** King side rook column. */
    static final int K_ROOK = 7;

    /**
     * The standard chess board.
     */
    public StandardBoard() {
    	super(new GameMode(8,8));
        setWidth(WIDTH);
        setHeight(HEIGHT);
        clear();
        Model pawn = PieceRegistry.get("pawn"), knight = PieceRegistry.get("knight"), rook = PieceRegistry.get("rook"),
        		bishop = PieceRegistry.get("bishop"), queen = PieceRegistry.get("queen"), king = PieceRegistry.get("king");
        
        for (int x = 0; x < WIDTH; x++) {
            setPiece(x, WHITE_PAWN_ROW, new Piece(Piece.Side.WHITE, pawn));
            setPiece(x, BLACK_PAWN_ROW, new Piece(Piece.Side.BLACK, pawn));
        }
        setPiece(Q_ROOK, WHITE_ROW, new Piece(Piece.Side.WHITE, rook));
        setPiece(K_ROOK, WHITE_ROW, new Piece(Piece.Side.WHITE, rook));
        setPiece(Q_ROOK, BLACK_ROW, new Piece(Piece.Side.BLACK, rook));
        setPiece(K_ROOK, BLACK_ROW, new Piece(Piece.Side.BLACK, rook));
        setPiece(Q_KNIGHT, WHITE_ROW, new Piece(Piece.Side.WHITE, knight));
        setPiece(K_KNIGHT, WHITE_ROW, new Piece(Piece.Side.WHITE, knight));
        setPiece(Q_KNIGHT, BLACK_ROW, new Piece(Piece.Side.BLACK, knight));
        setPiece(K_KNIGHT, BLACK_ROW, new Piece(Piece.Side.BLACK, knight));
        setPiece(Q_BISHOP, WHITE_ROW, new Piece(Piece.Side.WHITE, bishop));
        setPiece(K_BISHOP, WHITE_ROW, new Piece(Piece.Side.WHITE, bishop));
        setPiece(Q_BISHOP, BLACK_ROW, new Piece(Piece.Side.BLACK, bishop));
        setPiece(K_BISHOP, BLACK_ROW, new Piece(Piece.Side.BLACK, bishop));
        setPiece(QUEEN, WHITE_ROW, new Piece(Piece.Side.WHITE, queen));
        setPiece(QUEEN, BLACK_ROW, new Piece(Piece.Side.BLACK, queen));
        setPiece(KING, WHITE_ROW, new Piece(Piece.Side.WHITE, king));
        setPiece(KING, BLACK_ROW, new Piece(Piece.Side.BLACK, king));
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
                    p.getMoves(false).containsDest(kingPos)) {

                    return true;
                }
            }
        }
        return false;
    }
}
