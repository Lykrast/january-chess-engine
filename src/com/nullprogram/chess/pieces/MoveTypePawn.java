package com.nullprogram.chess.pieces;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.Board;
import com.nullprogram.chess.Move;
import com.nullprogram.chess.MoveList;
import com.nullprogram.chess.MoveType;
import com.nullprogram.chess.Piece;
import com.nullprogram.chess.Piece.Side;
import com.nullprogram.chess.Position;
import com.nullprogram.chess.resources.JSONUtils;

public class MoveTypePawn extends MoveType {
	//TODO: cleanup and make more generic
	private String promotion;
	private boolean doubleStep;

	public MoveTypePawn(String promotion, boolean doubleStep) {
		super(MoveMode.MOVE_CAPTURE);
		this.promotion = promotion;
		this.doubleStep = doubleStep;
	}

	@Override
	public MoveList getMoves(Piece p, MoveList list) {
        Position pos = p.getPosition();
        Board board = p.getBoard();
        int dir = direction(p);
        Position dest = new Position(pos, 0, 1 * dir);
        Move first = new Move(pos, dest);
        addUpgrade(first, p);
        if (list.addMove(first) && doubleStep && !p.moved()) {
            list.addMove(new Move(pos, new Position(pos, 0, 2 * dir)));
        }
        Move captureLeft = new Move(pos, new Position(pos, -1, 1 * dir));
        addUpgrade(captureLeft, p);
        list.addCaptureOnly(captureLeft);
        Move captureRight = new Move(pos, new Position(pos,  1, 1 * dir));
        addUpgrade(captureRight, p);
        list.addCaptureOnly(captureRight);

        if (doubleStep)
        {
            /* check for en passant */
            Move last = board.last();
            if (last != null) {
                Position left = new Position(pos, -1, 0);
                Position right = new Position(pos, 1, 0);
                Position lOrigin = last.getOrigin();
                Position lDest = last.getDest();
                if (left.equals(lDest) &&
                    (lOrigin.getX() == lDest.getX()) &&
                    (lOrigin.getY() == lDest.getY() + dir * 2) &&
                    (board.getPiece(left).getModel().getName().equals("Pawn"))) {

                    /* en passant to the left */
                    Move passant = new Move(pos, new Position(pos, -1, dir));
                    passant.setNext(new Move(left, null));
                    list.addMove(passant);
                } else if (right.equals(lDest) &&
                           (lOrigin.getX() == lDest.getX()) &&
                           (lOrigin.getY() == lDest.getY() + dir * 2) &&
                           (board.getPiece(right).getModel().getName().equals("Pawn"))) {

                    /* en passant to the right */
                    Move passant = new Move(pos, new Position(pos, 1, dir));
                    passant.setNext(new Move(right, null));
                    list.addMove(passant);
                }
            }
        }
        return list;
	}

    /**
     * Add the upgrade actions to the given move if needed.
     *
     * @param move the move to be modified
     */
    private void addUpgrade(final Move move, Piece p) {
        if (move.getDest().getY() != upgradeRow(p)) {
            return;
        }
        move.setNext(new Move(move.getDest(), null)); // remove the pawn
        Move upgrade = new Move(null, move.getDest());
        upgrade.setReplacement(promotion);
        upgrade.setReplacementSide(p.getSide());
        move.getNext().setNext(upgrade);              // add a queen
    }

    /**
     * Determine direction of this pawn's movement.
     *
     * @return direction for this pawn
     */
    private int direction(Piece p) {
        if (p.getSide() == Side.WHITE) {
            return 1;
        } else {
            return -1;
        }
    }

    /**
     * Determine upgrade row.
     *
     * @return the upgrade row index.
     */
    private int upgradeRow(Piece p) {
        if (p.getSide() == Side.BLACK) {
            return 0;
        } else {
            return p.getBoard().getHeight() - 1;
        }
    }

	@Override
	public MoveType create(JsonObject json, MoveMode mode) throws JsonParseException {
		JsonElement jStep = json.get("doublestep");
		boolean step = false;
		if (jStep != null)
		{
			step = jStep.getAsBoolean();
		}
		return new MoveTypePawn(JSONUtils.getMandatory(json, "promotion").getAsString(), step);
	}

}
