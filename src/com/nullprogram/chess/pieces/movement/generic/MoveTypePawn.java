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

public class MoveTypePawn extends MoveType {
	//TODO: cleanup and make more generic
	private int initialStep;

	public MoveTypePawn(int initialStep) {
		super(MoveMode.MOVE_CAPTURE, DirectionMode.FORWARD);
		this.initialStep = initialStep;
	}

	@Override
	public IMoveList getMoves(Piece p, IMoveList list) {
        Position pos = p.getPosition();
        Board board = p.getBoard();
        int dir = direction(p);
        Position dest = pos.offset(0, 1 * dir);
        Move first = new Move(pos, dest);
        if (list.addMove(first) && initialStep > 0 && !p.moved()) {
        	//initial steps
        	for (int i=0; i<initialStep; i++)
        	{
                if (!list.addMove(new Move(pos, pos.offset(0, (i+2) * dir)))) break;
        	}
        }
        Move captureLeft = new Move(pos, pos.offset(-1, 1 * dir));
        list.addCaptureOnly(captureLeft);
        Move captureRight = new Move(pos, pos.offset(1, 1 * dir));
        list.addCaptureOnly(captureRight);

        if (initialStep > 0)
        {
            /* check for en passant */
            Move last = board.last();
            if (last != null) {
                Position lOrigin = last.getOrigin();
                Position lDest = last.getDest();
                
                //Conditions on last move:
                // Must be made in the same file as it started
                // Must be exactly 1 file away
                // Must have slipped through the attacking range
                // Must not have been initially capturable
                // Must be within the range of the initial step specified here
                // Must have been made by a Pawn
                //TODO: not hardcode that name
                if (lOrigin.x == lDest.x
                		&& Math.abs(lDest.x - pos.x) == 1
                		&& passedThrough(dir, pos, lDest)
                		&& (lOrigin.y - pos.y) != dir
                		&& inInitialRange(dir, lOrigin, lDest)
                		&& board.getPiece(lDest).getModel().getName().equals("Pawn"))
                {
                	Position target;
                	//To the left
                	if (lDest.x < pos.x) target = pos.offset(-1, dir);
                	//To the right
                	else target = pos.offset(1, dir);
                	
                    Move passant = new Move(pos, target);
                    passant.setNext(new Move(lDest, null));
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
        return p.getSide().value();
    }
    
    /**
     * Determine if the enemy's position is out of reach forever.
     * 
     * @param dir direction of movement
     * @param self position of the pawn moving
     * @param enemy position to check
     * @return true if the enemy passed through capture and cannot be reached by going forward
     */
    private boolean passedThrough(int dir, Position self, Position enemy) {
    	if (dir > 0) return self.y >= enemy.y;
    	else return self.y <= enemy.y;
    }
    
    /**
     * Determine if the move's y movement matches an initial step.
     * 
     * @param dir direction of movement
     * @param start start position of the move
     * @param end end position of the move
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
		if (jStep != null)
		{
			step = jStep.getAsInt();
		}
		return new MoveTypePawn(step);
	}

	@Override
	public String getTypeName() {
		return "Pawn";
	}

}
