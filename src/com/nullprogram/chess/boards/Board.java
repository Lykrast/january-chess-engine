package com.nullprogram.chess.boards;

import java.util.ArrayList;
import java.util.List;

import com.nullprogram.chess.Move;
import com.nullprogram.chess.Position;
import com.nullprogram.chess.pieces.Piece;
import com.nullprogram.chess.pieces.PieceFactory;
import com.nullprogram.chess.pieces.movement.MoveList;

/**
 * Board data structure.
 *
 * After the initial setup, the board <i>must</i> only be modified
 * through move transaction. This allows undo() and copy(), which many
 * other things depends on, to work properly.
 */
public class Board {

	/** The internal board array. */
	private Piece[][] board;

	/** The size of this game board. */
	private int boardWidth, boardHeight;

	/** Moves taken in this game so far. */
	private final MoveList moves = new MoveList(this);

	private static final int FOLD_REPETITION = 4;
	private final RepetitionChecker repetition = new RepetitionChecker();
	private boolean repeated = false;

	private GameMode gameMode;

	public Board() {
		this(GameModeRegistry.get("empty"));
	}

	public Board(GameMode mode) {
		gameMode = mode;
		gameMode.initialize(this);
	}

	public GameMode getGameMode() {
		return gameMode;
	}

	/**
	 * Create a new Piece array, effectively clearing the board.
	 */
	public final void clear() {
		board = new Piece[boardWidth][boardHeight];
	}

	public boolean isRepeatedDraw() {
		return repeated;
	}

	// That's part of a duct tape
	public void forceRepeatedDraw(boolean force) {
		repeated = force;
	}

	/**
	 * Determine if board is in a state of checkmate.
	 *
	 * @param side side to be checked
	 * @return true if board is in a state of checkmate
	 */
	public final boolean checkmate(final Piece.Side side) {
		// Can't checkmate if you didn't start with a king
		if (!getGameMode().hasRoyal(side)) return false;

		// Loosing all your kings in some way is a checkmate
		return findRoyal(side).isEmpty() || (!hasMoves(side) && check(side));
	}
	
	//A very limited checkmate check for AI evaluation
	//Can actually catch stalemates but that's good enough for me
	public boolean aiCheckmate(Piece.Side side) {
		if (!hasMoves(side)) return true;
		return getGameMode().hasRoyal(side) && findRoyal(side).isEmpty();
	}

	/**
	 * Determine if board is in a state of stalemate.
	 *
	 * @param side side to be checked
	 * @return true if board is in a state of stalemate
	 */
	public final boolean stalemate(final Piece.Side side) {
		if (!getGameMode().hasRoyal(side)) return moveCount(side) == 0;

		return !hasMoves(side) && !check(side);
	}

	public int moveCount(final Piece.Side side) {
		int count = 0;
		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				Piece p = getPiece(x, y);
				if ((p != null) && (p.getSide() == side)) {
					count += p.getMoves(true).size();
				}
			}
		}
		return count;
	}
	
	public boolean hasMoves(Piece.Side side) {
		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				Piece p = getPiece(x, y);
				if (p != null && p.getSide() == side && !p.getMoves(true).isEmpty()) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Determine if board is in a state of check.
	 *
	 * @param side side to check for check
	 * @return true if board is in a state of check
	 */
	public final Boolean check(final Piece.Side side) {
		Piece.Side attacker = side.opposite();
		List<Position> kings = findRoyal(side);
		if (kings.isEmpty()) {
			/* no king on board, but can happen in AI evaluation */
			return false;
		}
		boolean checkMultiple = getGameMode().checkMultiple();
		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {
				Piece p = getPiece(x,y);
				if ((p != null) && (p.getSide() == attacker)) {
					MoveList moves = p.getMoves(false);

					// All kings must be checkmated
					if (checkMultiple) {
						boolean check = true;
						for (Position k : kings) if (!moves.capturesPos(k)) {
							check = false;
							break;
						}
						// See if we went through the entire loop without breaking
						if (check) return true;
					}
					// Only one king must be checkmated
					else {
						for (Position k : kings) if (moves.capturesPos(k)) return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Determine if board is in a state of check.
	 *
	 * @return true if board is in a state of check
	 */
	public final Boolean check() {
		return check(Piece.Side.WHITE) || check(Piece.Side.BLACK);
	}

	/**
	 * Determine if board is in checkmate.
	 *
	 * @return true if board is in checkmate
	 */
	public final Boolean checkmate() {
		return checkmate(Piece.Side.WHITE) || checkmate(Piece.Side.BLACK);
	}

	/**
	 * Determine if board is in stalemate.
	 *
	 * @return true if board is in stalemate
	 */
	public final Boolean stalemate() {
		return stalemate(Piece.Side.WHITE) || stalemate(Piece.Side.BLACK);
	}

	/**
	 * Find all royal pieces belonging to the given side.
	 * 
	 * @param side whose side to check
	 * @return a list of all positions that contain a royal piece for the given side
	 */
	public final List<Position> findRoyal(final Piece.Side side) {
		List<Position> list = new ArrayList<>();
		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				Position pos = new Position(x, y);
				Piece p = getPiece(pos);
				if (p != null && p.getModel().isRoyal() && p.getSide() == side) list.add(pos);
			}
		}
		return list;
	}

	/**
	 * Set the width of the board.
	 *
	 * @param width the new width
	 */
	protected final void setWidth(final int width) {
		boardWidth = width;
	}

	/**
	 * Get the width of the board.
	 *
	 * @return the width of the board
	 */
	public final int getWidth() {
		return boardWidth;
	}

	/**
	 * Set the height of the board.
	 *
	 * @param height the new height
	 */
	protected final void setHeight(final int height) {
		boardHeight = height;
	}

	/**
	 * Get the height of the board.
	 *
	 * @return the height of the board
	 */
	public final int getHeight() {
		return boardHeight;
	}

	/**
	 * Put the given Piece at the given position on the board.
	 *
	 * @param x horizontal part of the position
	 * @param y vertical part of the position
	 * @param p the piece object to be placed
	 */
	public final void setPiece(final int x, final int y, final Piece p) {
		setPiece(new Position(x, y), p);
	}

	/**
	 * Put the given Piece at the given Position on the board.
	 *
	 * @param pos the position on the board
	 * @param p   the piece object to be placed
	 */
	public final void setPiece(final Position pos, final Piece p) {
		board[pos.getX()][pos.getY()] = p;
		if (p != null) {
			p.setPosition(pos);
			p.setBoard(this);
		}
	}

	/**
	 * Get the Piece at the given Position.
	 *
	 * @param pos the position on the board
	 * @return the Piece at the position
	 */
	public final Piece getPiece(final Position pos) {
		return board[pos.getX()][pos.getY()];
	}
	public final Piece getPiece(int x, int y) {
		return board[x][y];
	}

	/**
	 * Perform the given move action.
	 *
	 * @param move the move
	 */
	public final void move(final Move move) {
		moves.add(move);
		execMove(move);
	}

	/**
	 * Swaps the pieces on 2 board positions.
	 * 
	 * @param a first position to swap
	 * @param b second position to swap
	 */
	private void swap(Position a, Position b) {
		Piece pieceA = getPiece(a);
		Piece pieceB = getPiece(b);
		setPiece(a, pieceB);
		if (pieceB != null) pieceB.setPosition(a);
		setPiece(b, pieceA);
		if (pieceA != null) pieceA.setPosition(b);
	}

	/**
	 * Actually execute the move.
	 *
	 * @param move the move
	 */
	private void execMove(final Move move) {
		if (move == null) {
			// End of a chain, check for repetition
			if (repetition.push(board) >= FOLD_REPETITION) repeated = true;
			return;
		}
		Position a = move.getOrigin();
		Position b = move.getDest();
		if (a != null && b != null) {
			if (move.isSwap()) {
				swap(a, b);
			}
			else {
				move.setCaptured(getPiece(b));
				setPiece(b, getPiece(a));
				setPiece(a, null);
				getPiece(b).setPosition(b);
			}
			getPiece(b).incMoved();
		}
		else if (a != null && b == null) {
			move.setCaptured(getPiece(a));
			setPiece(a, null);
		}
		else {
			setPiece(b, PieceFactory.create(move.getReplacement(), move.getReplacementSide()));
		}
		execMove(move.getNext());
	}

	/**
	 * Undo the last move.
	 */
	public final void undo() {
		execUndo(moves.pop());
	}

	/**
	 * Actually perform the undo action.
	 *
	 * @param move the move
	 */
	private void execUndo(final Move move) {
		if (move == null) {
			// End of a chain, undo repetition
			repetition.pop();
			// So ok undo moves are for AI simulation and check checking, but I don't know
			// how to make it not screw up once it goes back to normal
			// So I'm letting the simulators rewind that value on their own
			// repeated = false;
			return;
		}
		execUndo(move.getNext()); // undo in reverse
		Position a = move.getOrigin();
		Position b = move.getDest();
		if (a != null && b != null) {
			if (move.isSwap()) {
				swap(a, b);
			}
			else {
				setPiece(a, getPiece(b));
				setPiece(b, move.getCaptured());
				getPiece(a).setPosition(a);
			}
			getPiece(a).decMoved();
		}
		else if (a != null && b == null) {
			setPiece(a, move.getCaptured());
		}
		else {
			setPiece(b, null);
		}
	}

	/**
	 * Return the last move made.
	 *
	 * @return the previous move
	 */
	public final Move last() {
		return moves.peek();
	}

	/**
	 * Return true if position has no piece on it.
	 *
	 * @param pos position to be tested
	 * @return emptiness of position
	 */
	public final Boolean isEmpty(final Position pos) {
		return getPiece(pos) == null;
	}

	/**
	 * Return true if position has no piece of same side on it.
	 *
	 * @param pos  position to be tested
	 * @param side side of the piece wanting to move
	 * @return emptiness of position
	 */
	public final Boolean isEmpty(final Position pos, final Piece.Side side) {
		Piece p = getPiece(pos);
		if (p == null) { return true; }
		return p.getSide() != side;
	}

	/**
	 * Return true if position is on the board.
	 *
	 * @param pos position to be tested
	 * @return validity of position
	 */
	public final Boolean inRange(final Position pos) {
		return (pos.getX() >= 0) && (pos.getY() >= 0) && (pos.getX() < boardWidth) && (pos.getY() < boardHeight);
	}

	/**
	 * Return true if position is in range <i>and</i> empty.
	 *
	 * @param pos position to be tested
	 * @return validity of position
	 */
	public final Boolean isFree(final Position pos) {
		return inRange(pos) && isEmpty(pos);
	}

	/**
	 * Return true if position is in range and empty of given side.
	 *
	 * @param pos  position to be tested
	 * @param side side of the piece wanting to move
	 * @return validity of position
	 */
	public final Boolean isFree(final Position pos, final Piece.Side side) {
		return inRange(pos) && isEmpty(pos, side);
	}

	/**
	 * Return true if position is in range and contains an enemy piece.
	 *
	 * @param pos  position to be tested
	 * @param side side of the piece wanting to move
	 * @return validity of position
	 */
	public final Boolean isEnemy(final Position pos, final Piece.Side side) {
		if (!inRange(pos)) return false;
		Piece p = getPiece(pos);
		if (p == null) return false;
		return p.getSide() != side;
	}

	/**
	 * Copy this board.
	 *
	 * @return deep copy of the board.
	 */
	public final Board copy() {
		// Board fresh = BoardFactory.create(this.getClass());
		Board fresh = new Board(this.getGameMode());
		for (Move move : moves) {
			fresh.move(move.copy());
		}
		return fresh;
	}

	/**
	 * Generate a list of all moves for the given side.
	 *
	 * @param side  side to get moves for
	 * @param check check for check
	 * @return list of all moves
	 */
	public final MoveList allMoves(final Piece.Side side, final boolean check) {
		MoveList list = new MoveList(this, false);
		for (int y = 0; y < boardHeight; y++) {
			for (int x = 0; x < boardWidth; x++) {
				Piece p = board[x][y];
				if (p != null && p.getSide() == side) {
					list.addAll(p.getMoves(check));
				}
			}
		}
		return list;
	}

	/**
	 * Return the number of moves taken on this board.
	 *
	 * @return number of moves taken on this board
	 */
	public final int moveCount() {
		return moves.size();
	}
}
