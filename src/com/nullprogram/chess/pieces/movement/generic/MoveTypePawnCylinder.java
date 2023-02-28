package com.nullprogram.chess.pieces.movement.generic;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.Move;
import com.nullprogram.chess.Position;
import com.nullprogram.chess.boards.Board;
import com.nullprogram.chess.pieces.Piece;
import com.nullprogram.chess.pieces.movement.IMoveList;
import com.nullprogram.chess.pieces.movement.MoveType;
import com.nullprogram.chess.util.Wrap;

public class MoveTypePawnCylinder extends MoveType {
	// TODO: check MoveTypePawn it's the same shit
	private int initialStep;
	private Wrap wrap;

	public MoveTypePawnCylinder(int initialStep, Wrap wrap) {
		super(MoveMode.MOVE_CAPTURE, DirectionMode.FORWARD);
		this.initialStep = initialStep;
		this.wrap = wrap;
	}

	@Override
	public IMoveList getMoves(Piece p, IMoveList list) {
		Position pos = p.getPosition();
		Board board = p.getBoard();
		int dir = direction(p);
		Position dest = pos.offset(0, 1 * dir);
		Move first = new Move(pos, dest);
		if (list.addMove(first) && initialStep > 0 && !p.moved()) {
			// initial steps
			for (int i = 0; i < initialStep; i++) {
				if (!list.addMove(new Move(pos, pos.offset(0, (i + 2) * dir)))) break;
			}
		}
		Move captureLeft = new Move(pos, wrap.wrap(board, pos.offset(-1, 1 * dir)));
		list.addCaptureOnly(captureLeft);
		Move captureRight = new Move(pos, wrap.wrap(board, pos.offset(1, 1 * dir)));
		list.addCaptureOnly(captureRight);

		if (initialStep > 0) {
			/* check for en passant */
			Move last = board.last();
			if (last != null) {
				Position lOrigin = last.getOrigin();
				Position lDest = last.getDest();

				// Same shit as MoveTypePawn
				if (lOrigin.x == lDest.x
						&& wasAdjacent(lDest.x, pos.x, board.getWidth())
						&& passedThrough(dir, pos, lDest)
						&& (lOrigin.y - pos.y) != dir
						&& inInitialRange(dir, lOrigin, lDest)
						&& board.getPiece(lDest).getModel().getName().equals("Cylinder Pawn")) {
					Position target;
					// To the left
					if (lDest.x < pos.x) target = wrap.wrap(board, pos.offset(-1, dir));
					// To the right
					else target =  wrap.wrap(board, pos.offset(1, dir));

					Move passant = new Move(pos, target);
					passant.setNext(new Move(lDest, null));
					list.addMove(passant);
				}
			}
		}
		return list;
	}
	
	private boolean wasAdjacent(int x1, int x2, int w) {
		if (Math.abs(x1 - x2) == 1) return true;
		if (wrap == Wrap.VERTICAL) return false;
		return (x1 + 1) % w == x2 || (x1 + w - 1) % w == x2;
	}

	/**
	 * Determine direction of this pawn's movement.
	 *
	 * @return direction for this pawn
	 */
	private int direction(Piece p) {
		return p.getSide().value();
	}

	/**
	 * Determine if the enemy's position is out of reach forever.
	 * 
	 * @param dir   direction of movement
	 * @param self  position of the pawn moving
	 * @param enemy position to check
	 * @return true if the enemy passed through capture and cannot be reached by
	 *         going forward
	 */
	private boolean passedThrough(int dir, Position self, Position enemy) {
		if (dir > 0) return self.y >= enemy.y;
		else return self.y <= enemy.y;
	}

	/**
	 * Determine if the move's y movement matches an initial step.
	 * 
	 * @param dir   direction of movement
	 * @param start start position of the move
	 * @param end   end position of the move
	 * @return true if the move's y movement matches an initial step
	 */
	private boolean inInitialRange(int dir, Position start, Position end) {
		int yS = start.y, yE = end.y;
		if (dir > 0) return yS >= yE + dir * 2 && yS <= yE + dir * (initialStep + 1);
		else return yS <= yE + dir * 2 && yS >= yE + dir * (initialStep + 1);
	}

	@Override
	public MoveType create(JsonObject json, MoveMode mode, DirectionMode directionMode, JsonDeserializationContext context) throws JsonParseException {
		JsonElement jStep = json.get("initialstep");
		int step = 1;
		if (jStep != null) {
			step = jStep.getAsInt();
		}
		return new MoveTypePawnCylinder(step, Wrap.fromJson(json));
	}

	@Override
	public String getTypeName() {
		return "PawnCylinder";
	}

}
