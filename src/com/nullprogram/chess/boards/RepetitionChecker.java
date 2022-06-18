package com.nullprogram.chess.boards;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import com.nullprogram.chess.pieces.Piece;

/**
 * Stores simplified previous board positions to call threefold repetition.
 */
public class RepetitionChecker {
	private Deque<HashedBoard> stack;
	private Map<HashedBoard, Integer> count;
	
	public RepetitionChecker() {
		stack = new ArrayDeque<>();
		count = new HashMap<>();
	}
	
	public int push(Piece[][] board) {
		HashedBoard hash = new HashedBoard(board);
		stack.push(hash);
		int newcount = count.getOrDefault(hash, 0) + 1;
		count.put(hash, newcount);
		return newcount;
	}
	
	public void pop() {
		HashedBoard popped = stack.pop();
		int newcount = count.get(popped) - 1;
		if (newcount <= 0) count.remove(popped);
		else count.put(popped, newcount);
	}

}
