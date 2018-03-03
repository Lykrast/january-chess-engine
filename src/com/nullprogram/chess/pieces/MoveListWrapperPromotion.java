package com.nullprogram.chess.pieces;

import com.nullprogram.chess.IMoveList;
import com.nullprogram.chess.Move;
import com.nullprogram.chess.MoveListWrapper;
import com.nullprogram.chess.Piece;
import com.nullprogram.chess.MoveType.MoveMode;
import com.nullprogram.chess.Piece.Side;

public class MoveListWrapperPromotion extends MoveListWrapper {
	private String[] promoted;
	private int start, end;

	public MoveListWrapperPromotion(Piece piece, IMoveList list, String[] promoted, int rows) {
		super(piece, list);
		if (promoted != null) this.promoted = promoted;
		else this.promoted = piece.getBoard().getGameMode().getPromotions(); //Use the gamemode's list
		if (piece.getSide() == Side.BLACK)
		{
			start = 0;
			end = rows - 1;
		}
		else
		{
			end = piece.getBoard().getHeight() - 1;
			start = end - rows + 1;
		}
	}

	@Override
	protected Move modify(Move move) {
		//Unused here
		return null;
	}
	
	private boolean eligible(Move move)
	{
		//If we can't promote, don't bother to check
		if (promoted == null) return false;
		int y = move.getDest().getY();
        return y >= start && y <= end;
	}
	
	private Move promote(Move move, String target)
	{
		Move copy = move.copy();
        //Add at the end of the current move
        Move innermost = copy.getLast();
        
        innermost.setNext(new Move(copy.getDest(), null)); // remove the piece
        Move upgrade = new Move(null, copy.getDest());
        upgrade.setReplacement(target); // put the replacement
        upgrade.setReplacementSide(piece.getSide());
        innermost.getNext().setNext(upgrade);
        
        return copy;
	}

	@Override
	public boolean add(Move move) {
		if (!eligible(move)) return list.add(move);
		
		boolean added = false;
		for (String s : promoted) {
			if (list.add(promote(move, s))) added = true;
		}
		//true if at least one move made it through
		return added;
	}

	@Override
	public boolean add(Move move, MoveMode type) {
		if (!eligible(move)) return list.add(move, type);
		
		boolean added = false;
		for (String s : promoted) {
			if (list.add(promote(move, s), type)) added = true;
		}
		//true if at least one move made it through
		return added;
	}

	@Override
	public boolean addMove(Move move) {
		if (!eligible(move)) return list.addMove(move);
		
		boolean added = false;
		for (String s : promoted) {
			if (list.addMove(promote(move, s))) added = true;
		}
		//true if at least one move made it through
		return added;
	}

	@Override
	public boolean addCapture(Move move) {
		if (!eligible(move)) return list.addCapture(move);
		
		boolean added = false;
		for (String s : promoted) {
			if (list.addCapture(promote(move, s))) added = true;
		}
		//true if at least one move made it through
		return added;
	}

	@Override
	public boolean addCaptureOnly(Move move) {
		if (!eligible(move)) return list.addCaptureOnly(move);
		
		boolean added = false;
		for (String s : promoted) {
			if (list.addCaptureOnly(promote(move, s))) added = true;
		}
		//true if at least one move made it through
		return added;
	}

}
