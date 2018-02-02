package com.nullprogram.chess.pieces;

import java.util.HashMap;
import java.util.Set;

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
		MAP.put("pawn", new Model("Pawn", 1.0, new MoveTypePawn()));
		MAP.put("knight", new Model("Knight", 3.0, new MoveTypeKnight()));
		MAP.put("rook", new Model("Rook", 5.0, new MoveTypeRook()));
		MAP.put("bishop", new Model("Bishop", 3.0, new MoveTypeBishop()));
		MAP.put("queen", new Model("Queen", 9.0, new MoveTypeBishop(), new MoveTypeRook()));
		MAP.put("king", new Model("King", 1000.0, new MoveTypeKing()));
	}
	
	public static Set<String> getModelID()
	{
		return MAP.keySet();
	}
}
