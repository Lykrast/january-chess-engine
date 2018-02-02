package com.nullprogram.chess.boards;

/**
 * Board for the game of Gothic Chess.
 */
public class BoardWildebeest extends StandardBoard {

    /** Serialization identifier. */
	private static final long serialVersionUID = -2062757962221555906L;

	/** The standard board width. */
    static final int WIDTH = 11;

    /** The standard board height. */
    static final int HEIGHT = 10;

    /** Row of the white pawns. */
    static final int WHITE_PAWN_ROW = 1;

    /** Row of the black pawns. */
    static final int BLACK_PAWN_ROW = 8;

    /** White home row. */
    static final int WHITE_ROW = 0;

    /** Black home row. */
    static final int BLACK_ROW = 9;

    /**
     * The Gothic Chess board.
     */
    public BoardWildebeest() {
        setWidth(WIDTH);
        setHeight(HEIGHT);
        clear();
//        for (int x = 0; x < WIDTH; x++) {
//            setPiece(x, WHITE_PAWN_ROW, new Pawn(Piece.Side.WHITE));
//            setPiece(x, BLACK_PAWN_ROW, new Pawn(Piece.Side.BLACK));
//        }
//        setPiece(0, WHITE_ROW, new Rook(Piece.Side.WHITE));
//        setPiece(1, WHITE_ROW, new Knight(Piece.Side.WHITE));
//        setPiece(2, WHITE_ROW, new Bishop(Piece.Side.WHITE));
//        setPiece(3, WHITE_ROW, new Bishop(Piece.Side.WHITE));
//        setPiece(4, WHITE_ROW, new Queen(Piece.Side.WHITE));
//        setPiece(5, WHITE_ROW, new King(Piece.Side.WHITE));
//        setPiece(6, WHITE_ROW, new Wildebeest(Piece.Side.WHITE));
//        setPiece(7, WHITE_ROW, new Camel(Piece.Side.WHITE));
//        setPiece(8, WHITE_ROW, new Camel(Piece.Side.WHITE));
//        setPiece(9, WHITE_ROW, new Knight(Piece.Side.WHITE));
//        setPiece(10, WHITE_ROW, new Rook(Piece.Side.WHITE));
//
//        setPiece(0, BLACK_ROW, new Rook(Piece.Side.BLACK));
//        setPiece(1, BLACK_ROW, new Knight(Piece.Side.BLACK));
//        setPiece(2, BLACK_ROW, new Camel(Piece.Side.BLACK));
//        setPiece(3, BLACK_ROW, new Camel(Piece.Side.BLACK));
//        setPiece(4, BLACK_ROW, new Wildebeest(Piece.Side.BLACK));
//        setPiece(5, BLACK_ROW, new King(Piece.Side.BLACK));
//        setPiece(6, BLACK_ROW, new Queen(Piece.Side.BLACK));
//        setPiece(7, BLACK_ROW, new Bishop(Piece.Side.BLACK));
//        setPiece(8, BLACK_ROW, new Bishop(Piece.Side.BLACK));
//        setPiece(9, BLACK_ROW, new Knight(Piece.Side.BLACK));        
//        setPiece(10, BLACK_ROW, new Rook(Piece.Side.BLACK));
    }
}
