package com.nullprogram.chess.ai;

import java.util.logging.Logger;

import com.nullprogram.chess.Game;
import com.nullprogram.chess.Move;
import com.nullprogram.chess.Player;
import com.nullprogram.chess.boards.Board;
import com.nullprogram.chess.pieces.Piece.Side;

public class MCTS implements Player {
	//Thanks for https://blog.theofekfoundation.org/artificial-intelligence/2016/06/27/what-is-the-monte-carlo-tree-search/
	private static final Logger LOG = Logger.getLogger("com.nullprogram.chess.ai.Minimax");
	
	private MCTSNode root;
	private Game game;
	private int time;
	
	public MCTS(Game game) {
		this.game = game;
		time = 5*1000;
	}

	@Override
	public Move takeTurn(Board board, Side side) {
		//If no root node, that means it's our first turn
		if (root == null) root = new MCTSNode(board, side);
		else {
			//We need to find the root that corresponds to what our opponent did
			root = root.findCorrespondingChild(board.last());
		}
		
		if (game != null) {
			game.setProgress(0);
			game.setStatus("Thinking ...");
		}
		
		simulate();
		root = root.mostTriedChild();
		return root.getMove();
	}
	
	private void simulate() {
		long start = -System.currentTimeMillis();
		long cur = 0;
		while ((cur = System.currentTimeMillis() + start) < time) {
			root.chooseChild();
			//TODO certainty from https://github.com/The-Ofek-Foundation/UltimateTicTacToe/blob/91f2ffaee81c518cb2ff61e630998b986438dce8/script.js#L905
			if (game != null) {
				game.setProgress(cur / (float)time);
			}
		}
		//LOG.info("Total tries: " + root.totalTries());
	}

}
