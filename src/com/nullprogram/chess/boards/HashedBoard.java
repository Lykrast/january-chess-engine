package com.nullprogram.chess.boards;

import java.util.Arrays;

import com.nullprogram.chess.pieces.Piece;

public class HashedBoard {
	private HashedPiece[][] board;
    
    public HashedBoard(Piece[][] inboard) {
    	int width = inboard.length;
    	int height = inboard[0].length;
    	board = new HashedPiece[width][height];
    	for (int x = 0; x < width; x++) {
    		for (int y = 0; y < height; y++) {
    			board[x][y] = hashPiece(inboard[x][y]);
    		}
    	}
    }
    
    private HashedPiece hashPiece(Piece p) {
    	if (p == null) return null;
    	else return new HashedPiece(p);
    }
    
    @Override
	public int hashCode() {
		return Arrays.deepHashCode(board);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		HashedBoard other = (HashedBoard) obj;
		if (!Arrays.deepEquals(board, other.board)) return false;
		return true;
	}
	
	private static class HashedPiece {
		private int side;
		private String model;
		
		private HashedPiece(Piece p) {
			side = p.getSide().ordinal();
			model = p.getModel().getName();
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((model == null) ? 0 : model.hashCode());
			result = prime * result + side;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) return true;
			if (obj == null) return false;
			if (getClass() != obj.getClass()) return false;
			HashedPiece other = (HashedPiece) obj;
			if (model == null) {
				if (other.model != null) return false;
			}
			else if (!model.equals(other.model)) return false;
			if (side != other.side) return false;
			return true;
		}
	}

}
