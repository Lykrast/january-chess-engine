package com.nullprogram.chess.pieces.movement.generic;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.Move;
import com.nullprogram.chess.Position;
import com.nullprogram.chess.pieces.Piece;
import com.nullprogram.chess.pieces.movement.IMoveList;
import com.nullprogram.chess.pieces.movement.MoveList;
import com.nullprogram.chess.pieces.movement.MoveType;
import com.nullprogram.chess.resources.JSONUtils;

public class MoveTypeCastle extends MoveType {
	private int ldist, rdist;

    public MoveTypeCastle(int ldist, int rdist) {
		super(MoveType.MoveMode.MOVE, DirectionMode.HORIZONTAL);
		this.ldist = ldist;
		this.rdist = rdist;
	}

	/** List of enemy moves (cached). */
    private MoveList enemy;

    /** Cache the check check. */
    private Boolean inCheck;

	@Override
	public IMoveList getMoves(Piece p, IMoveList list) {
        /* check for castling */
        enemy = null;
        inCheck = null;
        if (list.checksCheck() && !p.moved()) {
            Move left = castle(-1, p, ldist);
            if (left != null) {
                list.add(left);
            }
            Move right = castle(1, p, rdist);
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
    private Move castle(final int dir, Piece p, int dist) {
        if (dist == -1) dist = p.getBoard().getWidth() / 2 - 2;
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
            Position kpos = pos.offset(dir * dist, 0);
            Move kingDest = new Move(pos, kpos);
            Position rpos = pos.offset(dir * dist - dir, 0);
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
                enemyMoves(p).capturesPos(pos)) {

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
        enemy = p.getBoard().allMoves(p.getSide().opposite(), false);
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
	public MoveType create(JsonObject elem, MoveMode mode, DirectionMode directionMode, JsonDeserializationContext context) throws JsonParseException {
		return new MoveTypeCastle(JSONUtils.getDefaultInt(elem, "leftdistance", -1), JSONUtils.getDefaultInt(elem, "rightdistance", -1));
	}

	@Override
	public String getTypeName() {
		return "Castle";
	}

}
