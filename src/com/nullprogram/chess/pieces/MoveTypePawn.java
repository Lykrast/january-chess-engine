package com.nullprogram.chess.pieces;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.Board;
import com.nullprogram.chess.IMoveList;
import com.nullprogram.chess.Move;
import com.nullprogram.chess.MoveType;
import com.nullprogram.chess.Piece;
import com.nullprogram.chess.Piece.Side;
import com.nullprogram.chess.Position;

public class MoveTypePawn extends MoveType {
	//TODO: cleanup and make more generic
	private boolean doubleStep;

	public MoveTypePawn(boolean doubleStep) {
		super(MoveMode.MOVE_CAPTURE);
		this.doubleStep = doubleStep;
	}

	@Override
	public IMoveList getMoves(Piece p, IMoveList list) {
        Position pos = p.getPosition();
        Board board = p.getBoard();
        int dir = direction(p);
        Position dest = new Position(pos, 0, 1 * dir);
        Move first = new Move(pos, dest);
        if (list.addMove(first) && doubleStep && !p.moved()) {
            list.addMove(new Move(pos, new Position(pos, 0, 2 * dir)));
        }
        Move captureLeft = new Move(pos, new Position(pos, -1, 1 * dir));
        list.addCaptureOnly(captureLeft);
        Move captureRight = new Move(pos, new Position(pos,  1, 1 * dir));
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

	@Override
	public MoveType create(JsonObject json, MoveMode mode, JsonDeserializationContext context) throws JsonParseException {
		JsonElement jStep = json.get("doublestep");
		boolean step = false;
		if (jStep != null)
		{
			step = jStep.getAsBoolean();
		}
		return new MoveTypePawn(step);
	}

}
