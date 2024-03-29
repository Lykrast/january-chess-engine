package com.nullprogram.chess.pieces;

import java.awt.Image;

import com.nullprogram.chess.pieces.Piece.Side;
import com.nullprogram.chess.pieces.movement.IMoveList;
import com.nullprogram.chess.pieces.movement.IMoveType;
import com.nullprogram.chess.pieces.movement.MoveList;
import com.nullprogram.chess.pieces.movement.MoveListCapture;
import com.nullprogram.chess.resources.ImageServer;
import com.nullprogram.chess.util.Capturability;

public class Model {
	/** Name of this piece. */
	private String name;
	private String icon;

	private boolean royal;
	private Capturability capturability;
	private boolean immobilizer;
	private boolean shielder;
	
	private AIHints hints;

	private double value;
	private IMoveType[] moves;

	public Model(String name, String icon, double value, boolean royal, Capturability capturability, boolean immobilizer, boolean shielder, AIHints hints, IMoveType... moves) {
		this.name = name;
		this.icon = icon;
		this.value = value;
		this.moves = moves;
		this.royal = royal;
		this.capturability = capturability;
		this.immobilizer = immobilizer;
		this.shielder = shielder;
		this.hints = hints;
	}

	public double getValue() {
		return value;
	}
	
	public AIHints getHints() {
		return hints;
	}

	public String getName() {
		return name;
	}

	/**
	 * If the piece is considered royal and cannot be put in check.
	 */
	public boolean isRoyal() {
		return royal;
	}
	
	/**
	 * Can this model be captured by a given attacking piece.
	 */
	public boolean canBeCapturedBy(Piece capturer) {
		return capturability.canBeCapturedBy(capturer);
	}
	
	//TODO: more general Immobilizer support
	/**
	 * Does this immobilize nearby enemy pieces like Ultima's Immobilizer.
	 */
	public boolean isImmobilizer() {
		return immobilizer;
	}
	
	//TODO: more general Shielder support
	/**
	 * Does this protects nearby friendly pieces like Fugue's Shield.
	 */
	public boolean isShielder() {
		return shielder;
	}

	/**
	 * Get the moves for this piece.
	 *
	 * @param checkCheck check for check
	 * @return list of moves
	 */
	public MoveList getMoves(final Piece p, boolean checkCheck) {
		IMoveList list = new MoveList(p.getBoard(), checkCheck);
		if (!p.getBoard().isImmobilized(p.getPosition(), p.getSide())) {
			for (IMoveType m : moves) list = m.getMoves(p, list);
		}
		return (MoveList) list;
	}

	/**
	 * Get all moves for this piece that could capture something.
	 *
	 * @return list of moves
	 */
	public MoveListCapture getCapturingMoves(final Piece p) {
		IMoveList list = new MoveListCapture(p.getBoard());
		if (!p.getBoard().isImmobilized(p.getPosition(), p.getSide())) {
			for (IMoveType m : moves) list = m.getMoves(p, list);
		}
		return (MoveListCapture) list;
	}

	/**
	 * Get the image that represents this piece.
	 *
	 * This method currently uses reflection.
	 *
	 * @return image for this piece
	 */
	public final Image getImage(Side s) {
		return ImageServer.getPieceTile(icon, s);
	}

	@Override
	public String toString() {
		return name;
	}

}
