package com.nullprogram.chess.pieces.movement.generic;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.Move;
import com.nullprogram.chess.Position;
import com.nullprogram.chess.boards.Board;
import com.nullprogram.chess.pieces.Piece;
import com.nullprogram.chess.pieces.movement.IMoveList;
import com.nullprogram.chess.pieces.movement.MoveType;
import com.nullprogram.chess.resources.JSONUtils;
import com.nullprogram.chess.util.Wrap;

public class MoveTypeLeaperCylinder extends MoveType {
	private int near, far;
	private Wrap wrap;

	public MoveTypeLeaperCylinder(MoveMode mode, DirectionMode directionMode, int near, int far, Wrap wrap) {
		super(mode, directionMode);
		this.near = near;
		this.far = far;
		this.wrap = wrap;
	}

	@Override
	public IMoveList getMoves(Piece p, IMoveList list) {
		Position pos = p.getPosition();
		Board b = p.getBoard();
		int forward = p.getSide().value();
		DirectionMode dir = getDirectionMode();

		if (dir.forward() || dir.right()) {
			list.add(new Move(pos, wrap.wrap(b, pos.offset(near, forward * far))), getMoveMode());
			list.add(new Move(pos, wrap.wrap(b, pos.offset(far, forward * near))), getMoveMode());
		}
		if (dir.forward() || dir.left()) {
			list.add(new Move(pos, wrap.wrap(b, pos.offset(-far, forward * near))), getMoveMode());
			list.add(new Move(pos, wrap.wrap(b, pos.offset(-near, forward * far))), getMoveMode());
		}
		if (dir.back() || dir.right()) {
			list.add(new Move(pos, wrap.wrap(b, pos.offset(far, -forward * near))), getMoveMode());
			list.add(new Move(pos, wrap.wrap(b, pos.offset(near, -forward * far))), getMoveMode());
		}
		if (dir.back() || dir.left()) {
			list.add(new Move(pos, wrap.wrap(b, pos.offset(-far, -forward * near))), getMoveMode());
			list.add(new Move(pos, wrap.wrap(b, pos.offset(-near, -forward * far))), getMoveMode());
		}
		return list;
	}

	@Override
	public MoveType create(JsonObject json, MoveMode mode, DirectionMode directionMode, JsonDeserializationContext context) throws JsonParseException {
		return new MoveTypeLeaperCylinder(mode, directionMode, JSONUtils.getMandatory(json, "near").getAsInt(), JSONUtils.getMandatory(json, "far").getAsInt(), Wrap.fromJson(json));
	}

	@Override
	public String getTypeName() {
		return "LeaperCylinder";
	}

}
