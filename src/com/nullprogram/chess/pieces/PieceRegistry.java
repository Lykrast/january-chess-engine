package com.nullprogram.chess.pieces;

import java.util.HashMap;

import com.nullprogram.chess.Model;

public class PieceRegistry {
	private static final HashMap<String, Model> MAP = new HashMap<>();
	static
	{
		init();
	}
	
	private PieceRegistry() {};
	
	public static Model get(String id)
	{
		return MAP.get(id);
	}
	
	public static void init()
	{
		MAP.put("pawn", new ModelPawn());
		MAP.put("knight", new ModelKnight());
		MAP.put("rook", new ModelRook());
		MAP.put("bishop", new ModelBishop());
		MAP.put("queen", new ModelQueen());
		MAP.put("king", new ModelKing());
	}
}
