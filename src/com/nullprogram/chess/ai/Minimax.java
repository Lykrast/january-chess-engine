package com.nullprogram.chess.ai;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import com.nullprogram.chess.Game;
import com.nullprogram.chess.Move;
import com.nullprogram.chess.Player;
import com.nullprogram.chess.Position;
import com.nullprogram.chess.boards.Board;
import com.nullprogram.chess.pieces.Model;
import com.nullprogram.chess.pieces.Piece;
import com.nullprogram.chess.pieces.Piece.Side;
import com.nullprogram.chess.pieces.PieceRegistry;
import com.nullprogram.chess.pieces.movement.MoveList;
import com.nullprogram.chess.pieces.movement.MoveType;
import com.nullprogram.chess.pieces.movement.MoveType.DirectionMode;
import com.nullprogram.chess.pieces.movement.MoveType.MoveMode;
import com.nullprogram.chess.pieces.movement.generic.MoveTypeBishop;
import com.nullprogram.chess.pieces.movement.generic.MoveTypeRook;

/**
 * Minimax Chess AI player.
 *
 * This employs the dumb minimax algorithm to search the game tree for
 * moves. The board is currently only evaluated only by the pieces
 * present, not their positions.
 */
public class Minimax implements Player {

	/** This class's Logger. */
	private static final Logger LOG = Logger.getLogger("com.nullprogram.chess.ai.Minimax");

	/** The number of threads to use. */
	private static final int NTHREADS = Runtime.getRuntime().availableProcessors();

	/** Local friendly game controller. */
	private final Game game;

	/** Best move, the selected move. */
	private volatile Move bestMove;

	/** Thread manager. */
	private final Executor executor = Executors.newFixedThreadPool(NTHREADS);

	/** Values of each piece. */
	private Map<Model, Double> values;

	/** Divisor for milliseconds. */
	static final double MILLI = 1000.0;

	/** Maximum depth (configured). */
	private int maxDepth;

	/** Material score weight (configured). */
	private double wMaterial;

	/** King safety score weight (configured). */
	private double wSafety;

	/** Mobility score weight (configured). */
	private double wMobility;

	/** Random score weight (configured). */
	private double wRandom;

	// Tempo score
	private double wTempo;

	// Score given to a winning position
	private static final double END_VALUE = 1000000;
	private static final double END_DEPTH = END_VALUE / 10;

	/**
	 * Create the default Minimax.
	 *
	 * @param active the game this AI is being seated at
	 */
	public Minimax(final Game active) {
		this(active, "default");
	}

	/**
	 * Create a new AI from a given properties name.
	 *
	 * @param active the game this AI is being seated at
	 * @param name   name of configuration to use
	 */
	public Minimax(final Game active, final String name) {
		this(active, getConfig(name));
	}

	/**
	 * Create a new AI for the given board.
	 *
	 * @param active the game this AI is being seated at
	 * @param props  properties for this player
	 */
	public Minimax(final Game active, final Properties props) {
		game = active;
		values = new HashMap<Model, Double>();

		/* Piece values */
		for (String id : PieceRegistry.getModelID()) {
			Model m = PieceRegistry.get(id);
			double value = m.getValue();
//        	try {
//        		value = Double.parseDouble(config.getProperty(id));
//        	}
//        	catch (Exception e) {}
			values.put(m, value);
		}
		
		maxDepth = (int) Double.parseDouble(props.getProperty("depth"));
		wMaterial = Double.parseDouble(props.getProperty("material"));
		wSafety = Double.parseDouble(props.getProperty("safety"));
		wMobility = Double.parseDouble(props.getProperty("mobility"));
		wTempo = Double.parseDouble(props.getProperty("tempo"));
		wRandom = Double.parseDouble(props.getProperty("random"));
	}

	/**
	 * Get the configuration.
	 *
	 * @param name name of the configuration to load
	 * @return the configuration
	 */
	public static Properties getConfig(final String name) {
		Properties props;
		if ("default".equals(name)) {
			props = new Properties();
		}
		else {
			props = new Properties(getConfig("default"));
		}

		String filename = name + ".properties";
		InputStream in = Minimax.class.getResourceAsStream(filename);
		try {
			props.load(in);
		}
		catch (java.io.IOException e) {
			LOG.warning("Failed to load AI config: " + name + ": " + e);
		}
		finally {
			try {
				in.close();
			}
			catch (IOException e) {
				LOG.info("failed to close stream: " + e.getMessage());
			}
		}
		return props;
	}

	@Override
	public final Move takeTurn(final Board board, final Side side) {
		/* Gather up every move. */
		MoveList moves = board.allMoves(side, true);
		moves.shuffle();

		/* Initialize the shared structures. */
		if (game != null) {
			game.setProgress(0);
			game.setStatus("Thinking ...");
		}
		// long startTime = System.currentTimeMillis();

		/* Spin off threads to evaluate each move's tree. */
		CompletionService<Move> service = new ExecutorCompletionService<Move>(executor);
		int submitted = 0;
		bestMove = null;
		for (final Move move : moves) {
			final Board callboard = board.copy();
			service.submit(new Callable<Move>() {
				@Override
				public Move call() {
					callboard.move(move);
					double beta = Double.POSITIVE_INFINITY;
					if (bestMove != null) {
						beta = -bestMove.score;
					}
					double v = search(callboard, maxDepth - 1, side.opposite(), Double.NEGATIVE_INFINITY, beta);
					move.score = -v;
					return move;
				}
			});
			submitted++;
		}

		/* Gather up results and pick the best move. */
		for (int i = 0; i < submitted; i++) {
			try {
				Move m = service.take().get();
				if (bestMove == null || m.score > bestMove.score) {
					bestMove = m;
				}
			}
			catch (ExecutionException e) {
				LOG.warning("move went unevaluated: " + e.getMessage());
				e.printStackTrace();
			}
			catch (InterruptedException e) {
				LOG.warning("move went unevaluated: " + e.getMessage());
			}
			if (game != null) {
				game.setProgress(i / (1.0f * (submitted - 1)));
			}
		}

		// long time = (System.currentTimeMillis() - startTime);
		// LOG.info("AI took " + (time / MILLI) + " seconds (" + NTHREADS + " threads, "
		// + maxDepth + " plies)");
		//System.out.println(side + ": " + bestMove + " = " + bestMove.getScore());
		return bestMove;
	}

	/**
	 * Recursive move searching.
	 *
	 * @param b     board to search
	 * @param depth current depth
	 * @param s     side for current move
	 * @param alpha lower bound to check
	 * @param beta  upper bound to check
	 * @return best valuation found at lowest depth
	 */
	private double search(final Board b, final int depth, final Side s, final double alpha, final double beta) {
		if (b.isRepeatedDraw()) {
			return 0;
		}
		else if (b.aiCheckmate(s)) {
			//If we have no move left, then this is very bad for us.
			return -(END_VALUE + END_DEPTH * depth);
		}
		if (depth == 0) {
			return valuate(b, s);
		}
		Side opps = s.opposite(); // opposite side
		double best = alpha;
		MoveList list = b.allMoves(s, true);
		for (Move move : list) {
			// I shouldn't have to do that but I'm lazy
			boolean prevRepeat = b.isRepeatedDraw();
			b.move(move);
			best = Math.max(best, -search(b, depth - 1, opps, -beta, -best));
			b.undo();
			b.forceRepeatedDraw(prevRepeat);
			/* alpha-beta prune */
			if (beta <= best) { return best; }
		}
		return best;
	}

	/**
	 * Determine value of this board for the given side
	 *
	 * @param b board to be valuated
	 * @return valuation of this board
	 */
	private double valuate(final Board b, Side side) {
		double material = materialValue(b, side);
		double kingSafety = kingInsafetyValue(b, side);
		double mobility = mobilityValue(b, side);
		double random = randomValue();
		return material * wMaterial + kingSafety * wSafety + mobility * wMobility + random * wRandom + wTempo;
	}

	/**
	 * Add up the material value of the board for the given side.
	 *
	 * @param b board to be evaluated
	 * @return material value of the board
	 */
	private double materialValue(final Board b, Side side) {
		double value = 0;
		for (int y = 0; y < b.getHeight(); y++) {
			for (int x = 0; x < b.getWidth(); x++) {
				Position pos = new Position(x, y);
				Piece p = b.getPiece(pos);
				if (p != null) {
					// White is +1, Black, -1, so this is the total for white
					value += (values.get(p.getModel()) + p.getModel().getHints().evaluate(b, pos, p.getSide())) * p.getSide().value();
				}
			}
		}
		// Invert if we are black
		return value * side.value();
	}

	/**
	 * Determine how vulnerable are the kings.
	 *
	 * @param b board to be evaluated
	 * @return king insafety score
	 */
	private double kingInsafetyValue(final Board b, Side side) {
		double insafetySelf = b.getGameMode().hasRoyal(side) ? kingInsafetyValueFor(b, side) : 0;
		double insafetyOpponent = b.getGameMode().hasRoyal(side.opposite()) ? kingInsafetyValueFor(b, side.opposite()) : 0;
		return insafetyOpponent - insafetySelf;
	}

	private MoveType safetyRook = new MoveTypeRook(MoveMode.MOVE_CAPTURE, DirectionMode.ALL, -1),
			safetyBishop = new MoveTypeBishop(MoveMode.MOVE_CAPTURE, DirectionMode.ALL, -1);

	/**
	 * Helper function: determine safety of all kings of a given side.
	 *
	 * @param b board to be evaluated
	 * @param s side of king to be checked
	 * @return king insafety score
	 */
	private double kingInsafetyValueFor(final Board b, final Side s) {

		List<Position> list = b.findRoyal(s);
		if (list.isEmpty()) {
			/* Weird, but may happen during evaluation. */
			return END_VALUE;
		}
		double value = 0.0;
		for (Position p : list) value += kingInsafetyValue(b, p);
		return value;
	}

	/**
	 * Helper function: determine safety of a single king.
	 *
	 * @param b board to be evaluated
	 * @param p position of the king to evaluate
	 * @return king insafety score
	 */
	private double kingInsafetyValue(final Board b, final Position p) {
		MoveList list = new MoveList(b, false);
		/* Take advantage of the Rook and Bishop code. */
		safetyRook.getMoves(b.getPiece(p), list);
		safetyBishop.getMoves(b.getPiece(p), list);
		return list.size();
	}

	/**
	 * Mobility score for this board for given side.
	 *
	 * @param b board to be evaluated
	 * @return score for this board
	 */
	private double mobilityValue(final Board b, Side side) {
		return b.allMoves(side, false).size() - b.allMoves(side.opposite(), false).size();
	}

	/**
	 * Random score. This is used to break monotony.
	 *
	 * @return score evaluated
	 */
	private double randomValue() {
		return Math.random();
	}
}
