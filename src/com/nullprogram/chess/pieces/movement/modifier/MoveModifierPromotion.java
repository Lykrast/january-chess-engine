package com.nullprogram.chess.pieces.movement.modifier;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.Move;
import com.nullprogram.chess.pieces.Piece;
import com.nullprogram.chess.pieces.movement.IMoveList;
import com.nullprogram.chess.pieces.movement.IMoveType;
import com.nullprogram.chess.pieces.movement.MoveListWrapper;
import com.nullprogram.chess.pieces.movement.MoveModifier;
import com.nullprogram.chess.pieces.movement.MoveType.MoveMode;
import com.nullprogram.chess.util.AreaSided;

public class MoveModifierPromotion extends MoveModifier {
	private String[] promoted;
	private AreaSided area;

	public MoveModifierPromotion(IMoveType[] moves, AreaSided area, String... promoted) {
		super(moves);
		this.promoted = promoted;
		this.area = area;
	}

	@Override
	protected MoveListWrapper createWrapper(Piece p, IMoveList list) {
		return new Wrapper(p, list, promoted, area);
	}

	@Override
	protected MoveModifier create(JsonObject json, IMoveType[] moves, JsonDeserializationContext context) throws JsonParseException {
		JsonElement tmp = json.get("promoted");
		String[] promoted = null;
		if (tmp != null) {
			JsonArray array = tmp.getAsJsonArray();
			List<String> list = new ArrayList<>();
			for (JsonElement elem : array) {
				list.add(elem.getAsString());
			}
			promoted = list.toArray(new String[list.size()]);
		}

		return new MoveModifierPromotion(moves, AreaSided.fromJson(json, true), promoted);
	}

	@Override
	public String getTypeName() {
		return "ModPromotion";
	}

	public static class Wrapper extends MoveListWrapper {
		private String[] promoted;
		private AreaSided area;

		public Wrapper(Piece piece, IMoveList list, String[] promoted, AreaSided area) {
			super(piece, list);
			if (promoted != null) this.promoted = promoted;
			else this.promoted = piece.getBoard().getGameMode().getPromotions(piece.getSide()); // Use the gamemode's list
			this.area = area;
		}

		@Override
		protected Move modify(Move move) {
			// Unused here
			return null;
		}

		private boolean eligible(Move move) {
			// If we can't promote, don't bother to check
			if (promoted == null) return false;
			return area.inside(move.getDest(), piece.getBoard(), piece.getSide());
		}

		private Move promote(Move move, String target) {
			Move copy = move.copy();
			// Add at the end of the current move
			Move innermost = copy.getLast();

			innermost.setNext(new Move(copy.getDest(), null)); // remove the piece
			Move upgrade = new Move(null, copy.getDest());
			upgrade.setReplacement(target); // put the replacement
			upgrade.setReplacementSide(piece.getSide());
			innermost.getNext().setNext(upgrade);

			return copy;
		}

		@Override
		public boolean add(Move move) {
			if (!eligible(move)) return list.add(move);

			boolean added = false;
			for (String s : promoted) {
				if (list.add(promote(move, s))) added = true;
			}
			// true if at least one move made it through
			return added;
		}

		@Override
		public boolean add(Move move, MoveMode type) {
			if (!eligible(move)) return list.add(move, type);

			boolean added = false;
			for (String s : promoted) {
				if (list.add(promote(move, s), type)) added = true;
			}
			// true if at least one move made it through
			return added;
		}

		@Override
		public boolean addMove(Move move) {
			if (!eligible(move)) return list.addMove(move);

			boolean added = false;
			for (String s : promoted) {
				if (list.addMove(promote(move, s))) added = true;
			}
			// true if at least one move made it through
			return added;
		}

		@Override
		public boolean addCapture(Move move) {
			if (!eligible(move)) return list.addCapture(move);

			boolean added = false;
			for (String s : promoted) {
				if (list.addCapture(promote(move, s))) added = true;
			}
			// true if at least one move made it through
			return added;
		}

		@Override
		public boolean addCaptureOnly(Move move) {
			if (!eligible(move)) return list.addCaptureOnly(move);

			boolean added = false;
			for (String s : promoted) {
				if (list.addCaptureOnly(promote(move, s))) added = true;
			}
			// true if at least one move made it through
			return added;
		}

	}
}
