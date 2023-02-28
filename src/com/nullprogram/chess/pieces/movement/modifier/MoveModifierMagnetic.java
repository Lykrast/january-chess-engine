package com.nullprogram.chess.pieces.movement.modifier;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.Direction;
import com.nullprogram.chess.Move;
import com.nullprogram.chess.Position;
import com.nullprogram.chess.boards.Board;
import com.nullprogram.chess.pieces.Piece;
import com.nullprogram.chess.pieces.movement.IMoveList;
import com.nullprogram.chess.pieces.movement.IMoveType;
import com.nullprogram.chess.pieces.movement.MoveListWrapper;
import com.nullprogram.chess.pieces.movement.MoveModifier;
import com.nullprogram.chess.resources.JSONUtils;

public class MoveModifierMagnetic extends MoveModifier {
	private Behavior friendly, enemy;
	private boolean affectRoyal;

	public MoveModifierMagnetic(IMoveType[] moves, Behavior friendly, Behavior enemy, boolean affectRoyal) {
		super(moves);

		this.friendly = friendly;
		this.enemy = enemy;
		this.affectRoyal = affectRoyal;
	}

	@Override
	protected MoveListWrapper createWrapper(Piece p, IMoveList list) {
		return new Wrapper(p, list, friendly, enemy, affectRoyal);
	}

	@Override
	protected MoveModifier create(JsonObject json, IMoveType[] moves, JsonDeserializationContext context) throws JsonParseException {
		return new MoveModifierMagnetic(moves, Behavior.fromJson(json.get("friendly")), Behavior.fromJson(json.get("enemy")), JSONUtils.getDefaultBoolean(json, "affectroyal", true));
	}

	@Override
	public String getTypeName() {
		return "ModMagnetic";
	}

	public static class Wrapper extends MoveListWrapper {
		private Behavior friendly, enemy;
		private boolean affectRoyal;

		public Wrapper(Piece piece, IMoveList list, Behavior friendly, Behavior enemy, boolean affectRoyal) {
			super(piece, list);
			this.friendly = friendly;
			this.enemy = enemy;
			this.affectRoyal = affectRoyal;
		}

		@Override
		protected Move modify(Move move) {
			// Add at the end of the current move
			Move innermost = move.getLast();

			Board board = piece.getBoard();

			// Effect
			Position dest = move.destination;
			for (Direction dir : Direction.ORTHOGONAL) {
				// Find the first piece in the direction
				Position dirpos = dir.getPosition();
				Position progress = dest.offset(dirpos);
				while (isFree(progress)) {
					progress = progress.offset(dirpos);
				}

				// Check if it's a valid target
				if (!isValid(progress)) continue;

				Piece target = board.getPiece(progress);
				Behavior toApply = target.getSide() == piece.getSide() ? friendly : enemy;
				innermost = move(innermost, dest, progress, dir, toApply);
			}

			return move;
		}

		/**
		 * Check if the position is empty or contains the moving piece (as it doesn't
		 * actually move while calculating the move).
		 */
		private boolean isFree(Position target) {
			Board b = piece.getBoard();
			return b.inRange(target) && (b.isEmpty(target) || b.getPiece(target) == piece);
		}

		/**
		 * Say if at the position is a valid target (in range and is non royal if the
		 * modifier doesn't allow royal).
		 */
		private boolean isValid(Position target) {
			Board b = piece.getBoard();
			if (!b.inRange(target)) return false;
			Piece targetP = b.getPiece(target);
			return targetP != null && targetP != piece && (affectRoyal || !targetP.getModel().isRoyal());
		}

		/**
		 * Append the displacement if possible. Returns either the move or the added
		 * move that is now the deepest in the sequence.
		 */
		private Move move(Move move, Position start, Position target, Direction dir, Behavior b) {
			if (b == Behavior.NONE) return move;

			Position end = target;
			Position dirpos = dir.getPosition();

			// Find where the place will end up
			if (b == Behavior.ATTRACT) end = start.offset(dirpos);
			else if (b == Behavior.REPEL) {
				Board board = piece.getBoard();
				// Find furthest free space
				end = end.offset(dirpos);
				while (board.isFree(end)) {
					end = end.offset(dirpos);
				}
				end = end.offset(dir.opposite().getPosition());
			}

			// Don't move a target that wouldn't move
			if (target.equals(end)) return move;

			Move displacement = new Move(target, end);
			move.setNext(displacement);
			return displacement;
		}

	}

	public static enum Behavior {
		NONE, REPEL, ATTRACT;

		public static Behavior fromJson(JsonElement json) throws JsonParseException {
			if (json != null) {
				String sSym = json.getAsString();
				if (sSym.equalsIgnoreCase("NONE")) return NONE;
				else if (sSym.equalsIgnoreCase("REPEL")) return REPEL;
				else if (sSym.equalsIgnoreCase("ATTRACT")) return ATTRACT;
				else throw new JsonParseException("Invalid magnetic behavior value : " + sSym + " - must be NONE, REPEL or ATTRACT");
			}
			else return NONE;
		}
	}
}
