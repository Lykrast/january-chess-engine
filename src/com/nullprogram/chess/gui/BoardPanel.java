package com.nullprogram.chess.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Line2D;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import com.nullprogram.chess.GameEvent;
import com.nullprogram.chess.GameListener;
import com.nullprogram.chess.Move;
import com.nullprogram.chess.Player;
import com.nullprogram.chess.Position;
import com.nullprogram.chess.boards.Board;
import com.nullprogram.chess.boards.BoardLine;
import com.nullprogram.chess.pieces.Piece;
import com.nullprogram.chess.pieces.movement.MoveList;
import com.nullprogram.chess.pieces.movement.MoveListCapture;

/**
 * Displays a board and exposes local players.
 *
 * This swing element displays a game board and can also behave as a
 * player as needed.
 */
public class BoardPanel extends JComponent implements MouseListener, Player, GameListener {

	/** This class's Logger. */
	private static final Logger LOG = Logger.getLogger("com.nullprogram.chess.gui.BoardPanel");

	/** Size of a tile in working coordinates. */
	private static final double TILE_SIZE = 200.0;

	private static final int NOTATION_SIZE = 30;

	/** Shape provided for drawing background tiles. */
	private static final Shape TILE = new Rectangle2D.Double(0, 0, TILE_SIZE, TILE_SIZE);

	/** Padding between the highlight and tile border. */
	static final int HIGHLIGHT_PADDING = 15;
	static final int ATTACK_PADDING = 30;

	/** Thickness of highlighting. */
	static final Stroke HIGHLIGHT_STROKE = new BasicStroke(12);
	static final Stroke HIGHLIGHT_STROKE_ATTACK = new BasicStroke(6);
	static final Stroke STROKE_LINES_S = new BasicStroke(4);
	static final Stroke STROKE_LINES_L = new BasicStroke(16);

	/** Shape for drawing the highlights. */
	private static final Shape HIGHLIGHT = new RoundRectangle2D.Double(HIGHLIGHT_PADDING, HIGHLIGHT_PADDING,
			TILE_SIZE - HIGHLIGHT_PADDING * 2, TILE_SIZE - HIGHLIGHT_PADDING * 2, HIGHLIGHT_PADDING * 4,
			HIGHLIGHT_PADDING * 4);

//    private static final Shape HIGHLIGHT_WHITE =
//            new RoundRectangle2D.Double(ATTACK_PADDING, ATTACK_PADDING,
//                                        TILE_SIZE - ATTACK_PADDING * 2,
//                                        TILE_SIZE - ATTACK_PADDING * 2,
//                                        HIGHLIGHT_PADDING * 4,
//                                        HIGHLIGHT_PADDING * 4);
//    private static final Shape HIGHLIGHT_BLACK =
//            new RoundRectangle2D.Double(ATTACK_PADDING + HIGHLIGHT_PADDING, ATTACK_PADDING + HIGHLIGHT_PADDING,
//                                        TILE_SIZE - (ATTACK_PADDING + HIGHLIGHT_PADDING) * 2,
//                                        TILE_SIZE - (ATTACK_PADDING + HIGHLIGHT_PADDING) * 2,
//                                        HIGHLIGHT_PADDING * 4,
//                                        HIGHLIGHT_PADDING * 4);

	private static final Shape HIGHLIGHT_WHITE_1, HIGHLIGHT_WHITE_2;
	private static final Shape HIGHLIGHT_BLACK_1, HIGHLIGHT_BLACK_2;

	// Make the attacked shape brackets
	static {
		double len = (TILE_SIZE - 2 * ATTACK_PADDING) / 3;
		double no = ATTACK_PADDING;
		double op = TILE_SIZE - ATTACK_PADDING;
		HIGHLIGHT_WHITE_1 = new QuadCurve2D.Double(no, no + len, no, no, no + len, no);
		HIGHLIGHT_WHITE_2 = new QuadCurve2D.Double(op, op - len, op, op, op - len, op);
		HIGHLIGHT_BLACK_1 = new QuadCurve2D.Double(op, no + len, op, no, op - len, no);
		HIGHLIGHT_BLACK_2 = new QuadCurve2D.Double(no, op - len, no, op, no + len, op);

	}

	/** Version for object serialization. */
	private static final long serialVersionUID = 1L;

	/** The board being displayed. */
	private Board board;
	
	/** Compiled cosmetic lines to draw */
	private Shape[] lines;

	/** Indicate flipped status. */
	private boolean flipped = true;

	/** The currently selected tile. */
	private Position selected = null;

	/** The list of moves for the selected tile. */
	private MoveList moves = null;

	/** The color for the dark tiles on the board. */
	private Color colorDark = new Color(0xD1, 0x8B, 0x47);

	/** The color for the light tiles on the board. */
	private Color colorLight = new Color(0xFF, 0xCE, 0x9E);

	private Font font = new Font(Font.DIALOG, Font.PLAIN, NOTATION_SIZE);

	/** Border color for a selected tile. */
	static final Color SELECTED = new Color(0x00, 0xFF, 0xFF);

	/** Border color for a highlighted movement tile. */
	static final Color MOVEMENT = new Color(0x7F, 0x00, 0x00);
	static final Color MOVEMENT_SPECIAL = new Color(0x7F, 0x00, 0x7f);

	/** Last move highlight color. */
	static final Color LAST = new Color(0x00, 0x7F, 0xFF);
	static final Color LAST_SPECIAL = new Color(0x7F, 0x5F, 0xBF);
	static final Color LAST_CAPTURE = new Color(0xFF, 0x00, 0x00);

	static final Color ATTACKED_WHITE = new Color(0xFF, 0xFF, 0xFF);
	static final Color ATTACKED_BLACK = new Color(0x1C, 0x1C, 0x1C);

	/** Minimum size of a tile, in pixels. */
	static final int MIN_SIZE = 25;

	/** Preferred size of a tile, in pixels. */
	static final int PREF_SIZE = 75;

	/** The current interaction mode. */
	private Mode mode = Mode.WAIT;

	/** Current player making a move, when interactive. */
	private Piece.Side side;

	/** Latch to hold down the Game thread while the user makes a selection. */
	private CountDownLatch latch;

	/** The move selected by the player. */
	private Move selectedMove;

	/** Whether to show attacks or not */
	boolean showAttacks = false;

	/** The interaction modes. */
	private enum Mode {
		/** Don't interact with the player. */
		WAIT,
		/** Interact with the player. */
		PLAYER;
	}

	/**
	 * Hidden constructor.
	 */
	protected BoardPanel() {
	}

	/**
	 * Create a new display for given board.
	 *
	 * @param displayBoard the board to be displayed
	 */
	public BoardPanel(final Board displayBoard) {
		board = displayBoard;
		compileLines();
		updateSize();
		addMouseListener(this);
	}

	/**
	 * Set the preferred board size.
	 */
	private void updateSize() {
		setPreferredSize(new Dimension(PREF_SIZE * board.getWidth(), PREF_SIZE * board.getHeight()));
		setMinimumSize(new Dimension(MIN_SIZE * board.getWidth(), MIN_SIZE * board.getHeight()));
	}

	@Override
	public final Dimension getPreferredSize() {
		return new Dimension(PREF_SIZE * board.getWidth(), PREF_SIZE * board.getHeight());
	}

	/**
	 * Change the board to be displayed.
	 *
	 * @param b the new board
	 */
	public final void setBoard(final Board b) {
		board = b;
		compileLines();
		updateSize();
		repaint();
	}
	
	private void compileLines() {
		BoardLine[] raw = board.getLines();
		lines = new Shape[raw.length];
		for (int i = 0; i < raw.length; i++) {
			BoardLine l = raw[i];
			lines[i] = new Line2D.Double(l.sx * TILE_SIZE, l.sy * TILE_SIZE, l.ex * TILE_SIZE, l.ey * TILE_SIZE);
		}
	}

	/**
	 * Change the board to be displayed.
	 *
	 * @return display's board
	 */
	public final Board getBoard() {
		return board;
	}

	/**
	 * Return the transform between working space and drawing space.
	 *
	 * @return display transform
	 */
	public final AffineTransform getTransform() {
		AffineTransform at = new AffineTransform();
		at.scale(getWidth() / (TILE_SIZE * board.getWidth()), getHeight() / (TILE_SIZE * board.getHeight()));
		return at;
	}

	public void applyTheme(ColorTheme theme) {
		colorDark = theme.getDarkTileColor();
		colorLight = theme.getLightTileColor();
		invalidate();
		repaint();
	}

	/**
	 * Standard painting method.
	 *
	 * @param graphics the drawing surface
	 */
	@Override
	public final void paintComponent(final Graphics graphics) {
		Graphics2D g = (Graphics2D) graphics;
		int h = board.getHeight();
		int w = board.getWidth();
		g.transform(getTransform());
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setFont(font);

		/* Temp AffineTransform for the method */
		AffineTransform at = new AffineTransform();

		/* Draw the background */
		int notLineX = w * (int) TILE_SIZE - 25;
		int notLineXBigY = notLineX - 15;
		int notColY = h * (int) TILE_SIZE - 10;
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				if ((x + y) % 2 == 0) g.setColor(colorLight);
				else g.setColor(colorDark);
				at.setToTranslation(x * TILE_SIZE, y * TILE_SIZE);
				g.fill(at.createTransformedShape(TILE));

				// Notation
				if (x == w - 1) {
					if ((x + y) % 2 == 0) g.setColor(colorDark);
					else g.setColor(colorLight);
					g.drawString(Integer.toString(h - y), (h - y) >= 10 ? notLineXBigY : notLineX, y * (int) TILE_SIZE + 35);
				}
				if (y == h - 1) {
					if ((x + y) % 2 == 0) g.setColor(colorDark);
					else g.setColor(colorLight);
					g.drawString(Character.toString((char) ('a' + x)), x * (int) TILE_SIZE + 10, notColY);
				}
			}
		}
		
		//Draw cosmetic lines
		at.setToTranslation(0, 0);
		for (Shape line : lines) {
			g.setStroke(STROKE_LINES_L);
			g.setColor(colorDark);
			g.draw(line);
		}
		for (Shape line : lines) {
			g.setStroke(STROKE_LINES_S);
			g.setColor(colorLight);
			g.draw(line);
		}

		/* Place the pieces */
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				Piece p = board.getPiece(new Position(x, y));
				if (p != null) {
					Image tile = p.getImage();
					int yy = y;
					if (flipped) {
						yy = board.getHeight() - 1 - y;
					}
					at.setToTranslation(x * TILE_SIZE, yy * TILE_SIZE);
					g.drawImage(tile, at, null);
				}
			}
		}

		if (showAttacks) {
			for (int y = 0; y < h; y++) {
				for (int x = 0; x < w; x++) {
					Piece p = board.getPiece(new Position(x, y));
					if (p != null) {
						Shape shape1, shape2;
						if (p.getSide() == Piece.Side.WHITE) {
							g.setColor(ATTACKED_WHITE);
							shape1 = HIGHLIGHT_WHITE_1;
							shape2 = HIGHLIGHT_WHITE_2;
						}
						else {
							g.setColor(ATTACKED_BLACK);
							shape1 = HIGHLIGHT_BLACK_1;
							shape2 = HIGHLIGHT_BLACK_2;
						}
						MoveListCapture tmpMoves = p.getCapturingMoves();
						for (Move move : tmpMoves) {
							highlightAttack(g, move.getDest(), shape1, shape2);
						}
					}
				}
			}
		}

		/* Draw last move */
		Move last = board.last();
		if (last != null) {
			if (last.isSpecial()) g.setColor(LAST_SPECIAL);
			else g.setColor(LAST);
			highlight(g, last.getOrigin());
			highlight(g, last.getDest());

			// Highlight unusual captures
			g.setColor(LAST_CAPTURE);
			Move current = last;
			while (current.getNext() != null) {
				current = current.getNext();
				Position p = current.getCaptureDest();
				if (current.getCaptured() != null && p != null && !last.getOrigin().equals(p)
						&& !last.getDest().equals(p))
					highlight(g, current.getCaptureDest());
			}
		}

		/* Draw selected square */
		if (selected != null) {
			g.setColor(SELECTED);
			highlight(g, selected);

			/* Draw piece moves */
			if (moves != null) {
				for (Move move : moves) {
					if (move.isSpecial()) g.setColor(MOVEMENT_SPECIAL);
					else g.setColor(MOVEMENT);
					highlight(g, move.getDest());
				}
			}
		}
	}

	/**
	 * Highlight the given tile on the board using the current color.
	 *
	 * @param g   the drawing surface
	 * @param pos position to highlight
	 */
	private void highlight(final Graphics2D g, final Position pos) {
		int x = pos.getX();
		int y = pos.getY();
		if (flipped) {
			y = board.getHeight() - 1 - y;
		}
		g.setStroke(HIGHLIGHT_STROKE);
		AffineTransform at = new AffineTransform();
		at.translate(x * TILE_SIZE, y * TILE_SIZE);
		g.draw(at.createTransformedShape(HIGHLIGHT));
	}

	private void highlightAttack(final Graphics2D g, final Position pos, final Shape shape1, final Shape shape2) {
		int x = pos.getX();
		int y = pos.getY();
		if (flipped) {
			y = board.getHeight() - 1 - y;
		}
		g.setStroke(HIGHLIGHT_STROKE_ATTACK);
		AffineTransform at = new AffineTransform();
		at.translate(x * TILE_SIZE, y * TILE_SIZE);
		g.draw(at.createTransformedShape(shape1));
		g.draw(at.createTransformedShape(shape2));
	}

	@Override
	public final void mouseReleased(final MouseEvent e) {
		switch (e.getButton()) {
			case MouseEvent.BUTTON1:
				leftClick(e);
				break;
			default:
				/* do nothing */
				break;
		}
		repaint();
	}

	/**
	 * Handle the event when the left button is clicked.
	 *
	 * @param e the mouse event
	 */
	private void leftClick(final MouseEvent e) {
		if (mode == Mode.WAIT) { return; }

		Position pos = getPixelPosition(e.getPoint());
		if (!board.inRange(pos)) {
			/* Click was outside the board, somehow. */
			return;
		}
		if (pos != null) {
			if (pos.equals(selected)) {
				/* Deselect */
				selected = null;
				moves = null;
			}
			else if (moves != null && moves.containsDest(pos)) {
				/* Move selected piece */
				mode = Mode.WAIT;
				// Move move = moves.getMoveByDest(pos);

				// Allowing multiple choices
				Move[] candidates = cutDuplicates(moves.getAllMovesByDest(pos)).toArray(new Move[0]);
				Move move = null;
				if (candidates.length == 1) move = candidates[0];
				else {
					move = (Move) JOptionPane.showInputDialog(this, "Choose which move to perform.",
							"Conflicting moves", JOptionPane.INFORMATION_MESSAGE, null, candidates, candidates[0]);
				}

				if (move != null) {
					selected = null;
					moves = null;
					selectedMove = move;
					latch.countDown();
				}
				else mode = Mode.PLAYER;
			}
			else {
				/* Select this position */
				Piece p = board.getPiece(pos);
				if (p != null && p.getSide() == side) {
					selected = pos;
					moves = p.getMoves(true);
				}
			}
		}
	}

	/**
	 * Makes a new list that contains all unique moves in the given list.
	 */
	private static List<Move> cutDuplicates(List<Move> list) {
		List<Move> current = new ArrayList<>();

		for (Move move : list) {
			if (!current.contains(move)) current.add(move);
		}

		return current;
	}

	/**
	 * Determine which tile a pixel point belongs to.
	 *
	 * @param p the point
	 * @return the position on the board
	 */
	private Position getPixelPosition(final Point2D p) {
		Point2D pout = null;
		try {
			pout = getTransform().inverseTransform(p, null);
		}
		catch (java.awt.geom.NoninvertibleTransformException t) {
			/* This will never happen. */
			return null;
		}
		int x = (int) (pout.getX() / TILE_SIZE);
		int y = (int) (pout.getY() / TILE_SIZE);
		if (flipped) {
			y = board.getHeight() - 1 - y;
		}
		return new Position(x, y);
	}

	/**
	 * Resets the selection.
	 */
	public void resetSelection() {
		selected = null;
		moves = null;
	}

	@Override
	public final Move takeTurn(final Board turnBoard, final Piece.Side currentSide) {
		latch = new CountDownLatch(1);
		board = turnBoard;
		side = currentSide;
		repaint();
		mode = Mode.PLAYER;
		try {
			latch.await();
		}
		catch (InterruptedException e) {
			LOG.warning("BoardPanel interrupted during turn.");
		}
		return selectedMove;
	}

	@Override
	public final void gameEvent(final GameEvent e) {
		board = e.getGame().getBoard();
		if (e.getType() != GameEvent.STATUS) {
			repaint();
		}
	}

	/**
	 * Return the desired aspect ratio of the board.
	 *
	 * @return desired aspect ratio
	 */
	public final double getRatio() {
		return board.getWidth() / (1.0 * board.getHeight());
	}

	/**
	 * Set whether or not the board should be displayed flipped.
	 * 
	 * @param value the new flipped state
	 */
	public final void setFlipped(final boolean value) {
		flipped = value;
	}

	@Override
	public void mouseExited(final MouseEvent e) {
		/* Do nothing */
	}

	@Override
	public void mouseEntered(final MouseEvent e) {
		/* Do nothing */
	}

	@Override
	public void mouseClicked(final MouseEvent e) {
		/* Do nothing */
	}

	@Override
	public void mousePressed(final MouseEvent e) {
		/* Do nothing */
	}
}
