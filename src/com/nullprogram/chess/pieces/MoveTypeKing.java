package com.nullprogram.chess.pieces;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.Move;
import com.nullprogram.chess.MoveList;
import com.nullprogram.chess.MoveType;
import com.nullprogram.chess.Piece;
import com.nullprogram.chess.Position;

public class MoveTypeKing extends MoveType {

    /** List of enemy moves (cached). */
    private MoveList enemy;

    /** Cache the check check. */
    private Boolean inCheck;

	@Override
	public MoveList getMoves(Piece p, MoveList list) {
        Position pos = p.getPosition();
        for (int y = -1; y <= 1; y++) {
            for (int x = -1; x <= 1; x++) {
                if (x != 0 || y != 0) {
                    list.addCapture(new Move(pos, new Position(pos, x, y)));
                }
            }
        }

        /* check for castling */
        enemy = null;
        inCheck = null;
        if (list.checksCheck() && !p.moved()) {
            Move left = castle(-1, p);
            if (left != null) {
                list.add(left);
            }
            Move right = castle(1, p);
            if (right != null) {
                list.add(right);
            }
        }
        return list;
	}

    /**
     * Try to create a castle move in the given direction.
     *
     * @param dir direction to check
     * @return the move, or null
     */
    private Move castle(final int dir, Piece p) {
        int dist = p.getBoard().getWidth() / 2 - 2;
        Position pos = p.getPosition();

        int max;
        if (dir < 0) {
            max = 0;
        } else {
            max = p.getBoard().getWidth() - 1;
        }

        Position rookPos = new Position(max, pos.getY());
        Piece rook = p.getBoard().getPiece(rookPos);
        if (rook == null || rook.moved()) {
            return null;
        }

        if (emptyRow(p.getPosition(), dir, max, p) && !inCheck(p)) {
            /* generate the move */
            Position kpos = new Position(pos, dir * dist, 0);
            Move kingDest = new Move(pos, kpos);
            Position rpos = new Position(pos, dir * dist - dir, 0);
            Move rookDest = new Move(rookPos, rpos);
            kingDest.setNext(rookDest);
            return kingDest;
        }
        return null;
    }

    /**
     * Check for an empty, unthreatened castling row.
     *
     * @param start the starting position
     * @param dir direction to check
     * @param max maximum column for the board
     * @return true if row is safe
     */
    private boolean emptyRow(final Position start, final int dir, final int max, Piece p) {
        for (int i = start.getX() + dir; i != max; i += dir) {
            Position pos = new Position(i, start.getY());
            if (p.getBoard().getPiece(pos) != null ||
                enemyMoves(p).containsDest(pos)) {

                return false;
            }
        }
        return true;
    }

    /**
     * Cache of enemy moves.
     *
     * @return list of enemy moves
     */
    private MoveList enemyMoves(Piece p) {
        if (enemy != null) {
            return enemy;
        }
        enemy = p.getBoard().allMoves(Piece.opposite(p.getSide()), false);
        return enemy;
    }

    /**
     * Cache of check check.
     *
     * @return true if king is in check
     */
    private boolean inCheck(Piece p) {
        if (inCheck != null) {
            return inCheck;
        }
        inCheck = p.getBoard().check(p.getSide());
        return inCheck;
    }

	@Override
	public MoveType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		return new MoveTypeKing();
	}

}
