package com.nullprogram.chess.boards;

/**
 * A line to be drawn on the board, like the river and citadels in Xiangqi.
 */
public class BoardLine {
	public final int sx, sy, ex, ey;

	public BoardLine(int sx, int sy, int ex, int ey) {
		this.sx = sx;
		this.sy = sy;
		this.ex = ex;
		this.ey = ey;
	}
}
