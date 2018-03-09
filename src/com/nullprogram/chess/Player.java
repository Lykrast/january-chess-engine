package com.nullprogram.chess;

import com.nullprogram.chess.boards.Board;
import com.nullprogram.chess.pieces.Piece;

/**
 * A Player is a class that can take a turn.
 *
 * The game driver will call takeTurn() on a player when it is the
 * player's turn. The player returns the desired move.
 */
public interface Player {

    /**
     * Inform the player it is time to take its turn.
     *
     * @param board the current board
     * @param side  the player's side
     * @return the selected move for this player
     */
    Move takeTurn(Board board, Piece.Side side);
}
