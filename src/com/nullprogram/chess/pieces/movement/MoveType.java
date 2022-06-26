package com.nullprogram.chess.pieces.movement;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.pieces.Piece;
import com.nullprogram.chess.pieces.movement.condition.*;
import com.nullprogram.chess.pieces.movement.generic.*;
import com.nullprogram.chess.pieces.movement.modifier.*;
import com.nullprogram.chess.resources.IMoveTypeDeserializer;
import com.nullprogram.chess.resources.MoveTypeDeserializer;

/**
 * A concrete MoveType with a direction and a capture mode.
 * 
 * @author Lykrast
 */
public abstract class MoveType implements IMoveTypeDeserializer, IMoveType {
	private MoveMode moveMode;
	private DirectionMode directionMode;

	protected MoveType(MoveMode moveMode, DirectionMode directionMode) {
		this.moveMode = moveMode;
		this.directionMode = directionMode;
	}

	protected MoveMode getMoveMode() {
		return moveMode;
	}

	protected DirectionMode getDirectionMode() {
		return directionMode;
	}

	/**
	 * Creates a MoveType according to the given JsonElement following the (already
	 * deserialized) MoveMode and DirectionMode.
	 * 
	 * @param json          Json to deserialize
	 * @param moveMode      MoveMode to follow
	 * @param directionMode DirectionMode to follow
	 * @param context       TODO
	 * @return a MoveType created with the given MoveMode
	 */
	protected abstract MoveType create(JsonObject json, MoveMode moveMode, DirectionMode directionMode,
			JsonDeserializationContext context) throws JsonParseException;

	@Override
	public IMoveType create(JsonObject json, JsonDeserializationContext context) throws JsonParseException {
		JsonObject obj = json.getAsJsonObject();
		return create(obj, MoveMode.fromJson(obj.get("mode")), DirectionMode.fromJson(obj.get("direction")), context);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nullprogram.chess.IMoveType#getMoves(com.nullprogram.chess.Piece,
	 * com.nullprogram.chess.IMoveList)
	 */
	@Override
	public abstract IMoveList getMoves(final Piece p, final IMoveList list);

	/**
	 * Registers all deserializers for Json loading.
	 */
	public static void registerDeserializers() {
		// Generics
		MoveTypeDeserializer.registerDeserializer(new MoveTypeLeaper(MoveMode.MOVE_CAPTURE, DirectionMode.ALL, 0, 0));
		MoveTypeDeserializer.registerDeserializer(new MoveTypeLeaperNarrow(MoveMode.MOVE_CAPTURE, DirectionMode.ALL, 0, 0));
		MoveTypeDeserializer.registerDeserializer(new MoveTypeLeaperWide(MoveMode.MOVE_CAPTURE, DirectionMode.ALL, 0, 0));
		MoveTypeDeserializer.registerDeserializer(new MoveTypeLeaperLame(MoveMode.MOVE_CAPTURE, DirectionMode.ALL, 0, 0));
		MoveTypeDeserializer.registerDeserializer(new MoveTypeLeaperOrthogonal(MoveMode.MOVE_CAPTURE, DirectionMode.ALL, 0));
		MoveTypeDeserializer.registerDeserializer(new MoveTypeLeaperDiagonal(MoveMode.MOVE_CAPTURE, DirectionMode.ALL, 0));
		MoveTypeDeserializer.registerDeserializer(new MoveTypeRider(MoveMode.MOVE_CAPTURE, DirectionMode.ALL, 0, 0, 0));
		MoveTypeDeserializer.registerDeserializer(new MoveTypeRiderCircular(MoveMode.MOVE_CAPTURE, 0, 0));
		MoveTypeDeserializer.registerDeserializer(new MoveTypeRiderBent(MoveMode.MOVE_CAPTURE, DirectionMode.ALL, 0, 0, 0, 0, 0));
		MoveTypeDeserializer.registerDeserializer(new MoveTypeRiderCrooked(MoveMode.MOVE_CAPTURE, DirectionMode.ALL, 0, 0));
		MoveTypeDeserializer.registerDeserializer(new MoveTypeTeleport(MoveMode.MOVE_CAPTURE, DirectionMode.ALL));

		MoveTypeDeserializer.registerDeserializer(new MoveTypeLeaperCylinder(MoveMode.MOVE_CAPTURE, DirectionMode.ALL, 0, 0, null));
		MoveTypeDeserializer.registerDeserializer(new MoveTypeLeaperNarrowCylinder(MoveMode.MOVE_CAPTURE, DirectionMode.ALL, 0, 0, null));
		MoveTypeDeserializer.registerDeserializer(new MoveTypeLeaperWideCylinder(MoveMode.MOVE_CAPTURE, DirectionMode.ALL, 0, 0, null));
		MoveTypeDeserializer.registerDeserializer(new MoveTypeLeaperOrthogonalCylinder(MoveMode.MOVE_CAPTURE, DirectionMode.ALL, 0, null));
		MoveTypeDeserializer.registerDeserializer(new MoveTypeLeaperDiagonalCylinder(MoveMode.MOVE_CAPTURE, DirectionMode.ALL, 0, null));
		MoveTypeDeserializer.registerDeserializer(new MoveTypeRookCylinder(MoveMode.MOVE_CAPTURE, DirectionMode.ALL, 0, null));
		MoveTypeDeserializer.registerDeserializer(new MoveTypeBishopCylinder(MoveMode.MOVE_CAPTURE, DirectionMode.ALL, 0, null));

		// Modifiers
		MoveTypeDeserializer.registerDeserializer(new MoveModifierPromotionSingle(null, "", null));
		MoveTypeDeserializer.registerDeserializer(new MoveModifierPromotion(null, null));
		MoveTypeDeserializer.registerDeserializer(new MoveModifierRestriction(null, null));
		MoveTypeDeserializer.registerDeserializer(new MoveModifierCaptureRoyal(null));
		MoveTypeDeserializer.registerDeserializer(new MoveModifierCaptureNonRoyal(null));
		MoveTypeDeserializer.registerDeserializer(new MoveModifierCoordinator(null));
		MoveTypeDeserializer.registerDeserializer(new MoveModifierCapricorn(null));
		MoveTypeDeserializer.registerDeserializer(new MoveModifierSwap(null));
		MoveTypeDeserializer.registerDeserializer(new MoveModifierMagnetic(null, null, null, false));
		MoveTypeDeserializer.registerDeserializer(new MoveModifierSuicide(null));
		MoveTypeDeserializer.registerDeserializer(new MoveModifierIgui(null));

		// Conditions
		MoveTypeDeserializer.registerDeserializer(new MoveConditionVirgin(null, null));
		MoveTypeDeserializer.registerDeserializer(new MoveConditionRestriction(null, null, null));

		// Presets
		MoveTypeDeserializer.registerDeserializer(new MoveTypeWazir(MoveMode.MOVE_CAPTURE, DirectionMode.ALL));
		MoveTypeDeserializer.registerDeserializer(new MoveTypeFerz(MoveMode.MOVE_CAPTURE, DirectionMode.ALL));
		MoveTypeDeserializer.registerDeserializer(new MoveTypeDabbaba(MoveMode.MOVE_CAPTURE, DirectionMode.ALL));
		MoveTypeDeserializer.registerDeserializer(new MoveTypeAlfil(MoveMode.MOVE_CAPTURE, DirectionMode.ALL));

		MoveTypeDeserializer.registerDeserializer(new MoveTypeKnight(MoveMode.MOVE_CAPTURE, DirectionMode.ALL));
		MoveTypeDeserializer.registerDeserializer(new MoveTypeKing(MoveMode.MOVE_CAPTURE, DirectionMode.ALL));
		MoveTypeDeserializer.registerDeserializer(new MoveTypeRook(MoveMode.MOVE_CAPTURE, DirectionMode.ALL, 0));
		MoveTypeDeserializer.registerDeserializer(new MoveTypeBishop(MoveMode.MOVE_CAPTURE, DirectionMode.ALL, 0));

		// Specialized
		MoveTypeDeserializer.registerDeserializer(new MoveTypePawn(1));
		MoveTypeDeserializer.registerDeserializer(new MoveTypeCastle(-1, -1));
		MoveTypeDeserializer.registerDeserializer(new MoveTypeCannon(DirectionMode.ALL));
		MoveTypeDeserializer.registerDeserializer(new MoveTypeVao(DirectionMode.ALL));
		MoveTypeDeserializer.registerDeserializer(new MoveTypeGrasshopper(MoveMode.MOVE_CAPTURE, DirectionMode.ALL));
		MoveTypeDeserializer.registerDeserializer(new MoveTypeLocust(DirectionMode.ALL));
		MoveTypeDeserializer.registerDeserializer(new MoveTypeEdgehog(MoveMode.MOVE_CAPTURE, DirectionMode.ALL));
		MoveTypeDeserializer.registerDeserializer(new MoveTypeBishopReflecting(MoveMode.MOVE_CAPTURE, DirectionMode.ALL));
		MoveTypeDeserializer.registerDeserializer(new MoveTypeAdvancer(DirectionMode.ALL));
		MoveTypeDeserializer.registerDeserializer(new MoveTypeWithdrawer(DirectionMode.ALL));
		MoveTypeDeserializer.registerDeserializer(new MoveTypeCheckers(MoveMode.MOVE_CAPTURE, DirectionMode.ALL, true));
		MoveTypeDeserializer.registerDeserializer(new MoveTypeLongLeaper(DirectionMode.ALL));
		MoveTypeDeserializer.registerDeserializer(new MoveTypePawnCylinder(1, null));
	}

	/**
	 * Tell if the move can capture and move without capturing or not.
	 * 
	 * @author Lykrast
	 */
	public static enum MoveMode {
		MOVE_CAPTURE("MOVE_CAPTURE", true, true, false), MOVE("MOVE", true, false, false),
		CAPTURE("CAPTURE", false, true, false), MOVE_CAPTURE_FRIENDLY("MOVE_CAPTURE_FRIENDLY", true, false, true),
		MOVE_CAPTURE_BOTH("MOVE_CAPTURE_BOTH", true, true, true),
		CAPTURE_FRIENDLY("CAPTURE_FRIENDLY", false, false, true), CAPTURE_BOTH("CAPTURE_BOTH", false, true, true);

		private static final MoveMode[] ALL = MoveMode.values();
		private static final String ERROR = "Invalid move mode : %s - must be " + MOVE_CAPTURE.name + ", " + MOVE.name
				+ ", " + CAPTURE.name + ", " + MOVE_CAPTURE_FRIENDLY.name + ", " + MOVE_CAPTURE_BOTH.name + ", "
				+ CAPTURE_FRIENDLY.name + " or " + CAPTURE_BOTH.name;

		private boolean move, captureEnemy, captureFriendly;
		private String name;

		private MoveMode(String name, boolean move, boolean captureEnemy, boolean captureFriendly) {
			this.move = move;
			this.captureEnemy = captureEnemy;
			this.captureFriendly = captureFriendly;
			this.name = name;
		}

		/**
		 * Does this MoveMode allows moving without capturing.
		 */
		public boolean move() {
			return move;
		}

		/**
		 * Does this MoveMode allows capturing an enemy piece by replacement.
		 */
		public boolean captureEnemy() {
			return captureEnemy;
		}

		/**
		 * Does this MoveMode allows capturing a friendly piece by replacement.
		 */
		public boolean captureFriendly() {
			return captureFriendly;
		}

		public static MoveMode fromJson(JsonElement json) throws JsonParseException {
			if (json != null) {
				String s = json.getAsString();
				for (MoveMode m : ALL) {
					if (m.name.equalsIgnoreCase(s)) return m;
				}

				throw new JsonParseException(String.format(ERROR, s));
			}
			else return MOVE_CAPTURE;
		}
	}

	/**
	 * A simple object to tell what direction the move can go towards.
	 * 
	 * @author Lykrast
	 */
	public static class DirectionMode {
		private boolean forward, back, left, right;
		private static final String STR_ALL = "ALL", STR_HORIZONTAL = "HORIZONTAL", STR_VERTICAL = "VERTICAL",
				STR_FORWARD = "FORWARD", STR_BACK = "BACK", STR_LEFT = "LEFT", STR_RIGHT = "RIGHT";
		// A few constants to save up a lil bit of memory when common ones are used
		// Probably not worth it
		public static final DirectionMode ALL = new DirectionMode(true, true, true, true),
				HORIZONTAL = new DirectionMode(false, false, true, true),
				VERTICAL = new DirectionMode(true, true, false, false),
				FORWARD = new DirectionMode(true, false, false, false),
				BACK = new DirectionMode(false, true, false, false),
				LEFT = new DirectionMode(false, false, true, false),
				RIGHT = new DirectionMode(false, false, false, true);
		private static final String ERROR = "Invalid direction : %s - must be " + STR_ALL + ", " + STR_HORIZONTAL + ", "
				+ STR_VERTICAL + ", " + STR_FORWARD + ", " + STR_BACK + ", " + STR_LEFT + " or " + STR_RIGHT;

		private DirectionMode(boolean forward, boolean back, boolean left, boolean right) {
			this.forward = forward;
			this.back = back;
			this.left = left;
			this.right = right;
		}

		public boolean forward() {
			return forward;
		}

		public boolean back() {
			return back;
		}

		public boolean left() {
			return left;
		}

		public boolean right() {
			return right;
		}

		@Override
		public String toString() {
			String str = "DirectionMode[";
			if (forward) str += STR_FORWARD + "/";
			if (back) str += STR_BACK + "/";
			if (left) str += STR_LEFT + "/";
			if (right) str += STR_RIGHT + "/";
			str = str.substring(0, str.lastIndexOf("/")) + "]";

			return str;
		}

		public static DirectionMode fromJson(JsonElement json) throws JsonParseException {
			if (json == null) return ALL;

			// Single direction
			if (json.isJsonPrimitive()) {
				String sJson = json.getAsString();
				if (sJson.equals(STR_ALL)) return ALL;
				else if (sJson.equals(STR_HORIZONTAL)) return HORIZONTAL;
				else if (sJson.equals(STR_VERTICAL)) return VERTICAL;
				else if (sJson.equals(STR_FORWARD)) return FORWARD;
				else if (sJson.equals(STR_BACK)) return BACK;
				else if (sJson.equals(STR_LEFT)) return LEFT;
				else if (sJson.equals(STR_RIGHT)) return RIGHT;
				else throw new JsonParseException(String.format(ERROR, sJson));
			}

			// Multiple directions
			JsonArray array = json.getAsJsonArray();
			DirectionMode mode = new DirectionMode(false, false, false, false);
			for (JsonElement e : array) {
				String sJson = e.getAsString();
				if (sJson.equals(STR_ALL)) return ALL;
				else if (sJson.equals(STR_HORIZONTAL)) {
					mode.left = true;
					mode.right = true;
				}
				else if (sJson.equals(STR_VERTICAL)) {
					mode.forward = true;
					mode.back = true;
				}
				else if (sJson.equals(STR_FORWARD)) mode.forward = true;
				else if (sJson.equals(STR_BACK)) mode.back = true;
				else if (sJson.equals(STR_LEFT)) mode.left = true;
				else if (sJson.equals(STR_RIGHT)) mode.right = true;
				else throw new JsonParseException(String.format(ERROR, sJson));
			}

			return mode;
		}
	}

}
