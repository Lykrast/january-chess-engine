package com.nullprogram.chess.pieces;

import com.nullprogram.chess.Move;
import com.nullprogram.chess.MoveList;
import com.nullprogram.chess.Piece;
import com.nullprogram.chess.Position;

public class MoveUtil {
	private MoveUtil() {}
	
    /**
     * Determine bishop moves for given situation.
     *
     * @param p     the piece being tested
     * @param list  list to be appended to
     * @return      the modified list
     */
    public static final MoveList getBishopMoves(final Piece p, final MoveList list) {
        /* Scan each direction and stop looking when we run into something. */
        Position home = p.getPosition();
        int x = home.getX();
        int y = home.getY();
        while (x >= 0 && y >= 0) {
            x--;
            y--;
            Position pos = new Position(x, y);
            if (!list.addCapture(new Move(home, pos))) {
                break;
            }
            if (!p.getBoard().isFree(pos)) {
                break;
            }
        }
        x = home.getX();
        y = home.getY();
        while (x < p.getBoard().getWidth() &&
               y < p.getBoard().getHeight()) {

            x++;
            y++;
            Position pos = new Position(x, y);
            if (!list.addCapture(new Move(home, pos))) {
                break;
            }
            if (!p.getBoard().isFree(pos)) {
                break;
            }
        }
        x = home.getX();
        y = home.getY();
        while (x >= 0 && y < p.getBoard().getHeight()) {
            x--;
            y++;
            Position pos = new Position(x, y);
            if (!list.addCapture(new Move(home, pos))) {
                break;
            }
            if (!p.getBoard().isFree(pos)) {
                break;
            }
        }
        x = home.getX();
        y = home.getY();
        while (x < p.getBoard().getWidth() && y >= 0) {
            x++;
            y--;
            Position pos = new Position(x, y);
            if (!list.addCapture(new Move(home, pos))) {
                break;
            }
            if (!p.getBoard().isFree(pos)) {
                break;
            }
        }
        return list;
    }
    
    /**
     * Determine rook moves for given situation.
     *
     * @param p     the piece being tested
     * @param list  list to be appended to
     * @return      the modified list
     */
    public static MoveList getRookMoves(final Piece p, final MoveList list) {
        /* Scan each direction and stop looking when we run into something. */
        Position home = p.getPosition();
        int x = home.getX();
        int y = home.getY();
        while (x >= 0) {
            x--;
            Position pos = new Position(x, y);
            if (!list.addCapture(new Move(home, pos))) {
                break;
            }
            if (!p.getBoard().isFree(pos)) {
                break;
            }
        }
        x = home.getX();
        y = home.getY();
        while (x < p.getBoard().getWidth()) {
            x++;
            Position pos = new Position(x, y);
            if (!list.addCapture(new Move(home, pos))) {
                break;
            }
            if (!p.getBoard().isFree(pos)) {
                break;
            }
        }
        x = home.getX();
        y = home.getY();
        while (y >= 0) {
            y--;
            Position pos = new Position(x, y);
            if (!list.addCapture(new Move(home, pos))) {
                break;
            }
            if (!p.getBoard().isFree(pos)) {
                break;
            }
        }
        x = home.getX();
        y = home.getY();
        while (y < p.getBoard().getHeight()) {
            y++;
            Position pos = new Position(x, y);
            if (!list.addCapture(new Move(home, pos))) {
                break;
            }
            if (!p.getBoard().isFree(pos)) {
                break;
            }
        }
        return list;
    }

    /**
     * Determine knight moves for given situation.
     *
     * @param p     the piece being tested
     * @param list  list to be appended to
     * @return      the modified list
     */
    public static MoveList getKnightMoves(final Piece p, final MoveList list) {
        return getLeaperMoves(p, list, 1, 2);
    }

    /**
     * Determine leaper moves for given situation.
     *
     * @param p     the piece being tested
     * @param list  list to be appended to
     * @param near	short segment of movement
     * @param far	long segment of movement
     * @return      the modified list
     */
    public static MoveList getLeaperMoves(final Piece p, final MoveList list, int near, int far) {
        Position pos = p.getPosition();
        list.addCapture(new Move(pos, new Position(pos,  near,  far)));
        list.addCapture(new Move(pos, new Position(pos,  far,  near)));
        list.addCapture(new Move(pos, new Position(pos, -far,  near)));
        list.addCapture(new Move(pos, new Position(pos, -far, -near)));
        list.addCapture(new Move(pos, new Position(pos,  far, -near)));
        list.addCapture(new Move(pos, new Position(pos,  near, -far)));
        list.addCapture(new Move(pos, new Position(pos, -near, -far)));
        list.addCapture(new Move(pos, new Position(pos, -near,  far)));
        return list;
    }

}
