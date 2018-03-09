package com.nullprogram.chess.pieces.movement;

import com.nullprogram.chess.Move;

public interface IMoveList {

	boolean checksCheck();

	/**
	 * Add a move without verifying it.
	 * @param move move to be added
	 * @return true
	 */
	boolean add(Move move);

	/**
	 * Add move to list following the given MoveMode.
	 *
	 * @param move move to be added
	 * @return     true if position was added, or, for CAPTURE, if the move would have been added if it was another mode
	 */
	boolean add(Move move, MoveType.MoveMode type);

	/**
	 * Add move to list if piece can legally move there (no capture).
	 *
	 * @param move move to be added
	 * @return     true if position was added
	 */
	boolean addMove(Move move);

	/**
	 * Add move to list if piece can move <i>or</i> capture at destination.
	 *
	 * @param move position to be added
	 * @return     true if position was added
	 */
	boolean addCapture(Move move);

	/**
	 * Add move to list only if the piece will perform a capture.
	 *
	 * @param move position to be added
	 * @return     true if position was added
	 */
	boolean addCaptureOnly(Move move);

}