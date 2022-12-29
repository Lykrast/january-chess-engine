package com.nullprogram.chess.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.nullprogram.chess.Move;
import com.nullprogram.chess.boards.Board;
import com.nullprogram.chess.pieces.Piece.Side;
import com.nullprogram.chess.pieces.movement.MoveList;

public class MCTSNode {
	private static final Random RAND = new Random();
	
	private Board board;
	private Move move;
	private Side side;
	private MCTSNode parent;
	private int hits, misses, totalTrials;
	
	private MCTSNode[] children;
	
	public MCTSNode(Board board, Side side) {
		this(board, side, null, null);
	}
	
	private MCTSNode(Board board, Side side, Move move, MCTSNode parent) {
		this.board = board;
		this.side = side;
		this.move = move;
		this.parent = parent;
	}
	
	private void getChildren() {
		MoveList moves = board.allMoves(side, true);
		children = new MCTSNode[moves.size()];
		
		int i = 0;
		for (Move m : moves) {
			children[i] = new MCTSNode(board.copy().move(m), side.opposite(), m, this);
			i++;
		}
	}
	
	//Propagates to a leaf node and runs a simulation on it.
	public void chooseChild() {
		if (children == null) getChildren();
		
		if (children.length == 0) runSimulation();
		else {
			//Get all the unexplored children (if any)
			List<MCTSNode> unexplored = new ArrayList<>();
			for (MCTSNode child : children) {
				if (child.totalTrials == 0) unexplored.add(child);
			}
			
			//If there are any unexplored children, pick one at random and run with it.
			if (!unexplored.isEmpty()) unexplored.get(RAND.nextInt(unexplored.size())).runSimulation();
			else {
				//Find the best child and recursively call this function
				MCTSNode bestChild = children[0];
				double bestPotential = bestChild.childPotential();
				for (int i = 1; i < children.length; i++) {
					double potential = children[i].childPotential();
					if (potential > bestPotential) {
						bestPotential = potential;
						bestChild = children[i];
					}
				}
				bestChild.chooseChild();
			}
		}
	}
	
	public Move getMove() {
		return move;
	}
	
	public int totalTries() {
		return totalTrials;
	}
	
	// https://github.com/The-Ofek-Foundation/UltimateTicTacToe/blob/91f2ffaee81c518cb2ff61e630998b986438dce8/script.js#L946
	public MCTSNode mostTriedChild() {
		if (children == null) return null;
		
		int most = children[0].totalTrials;
		MCTSNode mostChild = children[0];
		for (int i = 1; i < children.length; i++) {
			if (children[i].totalTrials > most) {
				most = children[i].totalTrials;
				mostChild = children[i];
			}
		}
		
		return mostChild;
	}
	
	//Find the children that uses the given move
	public MCTSNode findCorrespondingChild(Move m) {
		if (children == null) return null;
		for (MCTSNode child : children) {
			if (child.move.equals(m)) return child;
		}
		throw new IllegalArgumentException("No children found with the move " + m);
	}
	
	private int simulate() {
		Board simBoard = board.copy();
		Side simSide = side;
		//This is the status right after a move, so we're right after an opponent move here
		SimulationStatus status = gameStatus(board, side.opposite());
		
		//Keep playing until game is over
		while (status == SimulationStatus.PLAYING) {
			MoveList moves = simBoard.allMoves(simSide, true);
			moves.shuffle();
			simBoard.move(moves.peek());
			status = gameStatus(simBoard, simSide);
			simSide = simSide.opposite();
		}
		//Current implementation of status can only draw or win (because you can't lose by making a move)
		switch (status) {
			default:
			case DRAW:
				return 0;
			case WIN:
				//If we just played, simSide will be opposite to our true side, so we won
				if (simSide != side) return 1;
				else return -1;
		}
	}
	
	//Apparently we need to find this empirically
	private static final double POTENTIAL_CONSTANT = Math.sqrt(2);
	
	private double childPotential() {
		return ((misses - hits) / (double)totalTrials) + POTENTIAL_CONSTANT * Math.sqrt(Math.log(parent.totalTrials) / totalTrials);
	}
	
	//Run the simulation at the current board state, and then back propagate the results to the root of the tree.
	private void runSimulation() {
		backPropagate(simulate());
	}
	
	private void backPropagate(int simulation) {
		if (simulation > 0) hits++;
		else if (simulation < 0) misses++;
		totalTrials++;
		
		if (parent != null) parent.backPropagate(-simulation);
	}


	//This should probably be in the board, but let's worry about that later
	private static SimulationStatus gameStatus(Board board, Side side) {
		side = side.opposite();
		if (board.checkmate(side)) return SimulationStatus.WIN;
		if (board.isRepeatedDraw() || board.stalemate(side)) return SimulationStatus.DRAW;
		else return SimulationStatus.PLAYING;
	}
	
	private static enum SimulationStatus {
		PLAYING, DRAW, WIN, LOSS;
	}

}
