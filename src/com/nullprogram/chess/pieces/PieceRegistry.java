package com.nullprogram.chess.pieces;

import java.util.HashMap;
import java.util.Set;

import com.nullprogram.chess.Model;
import com.nullprogram.chess.resources.JSONLoader;

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
	
	public static void register(String name, Model m)
	{
		MAP.put(name, m);
	}
	
	public static void init()
	{
//		register("pawn", new Model("Pawn", 1.0, new MoveTypePawn()));
//		register("knight", new Model("Knight", 3.0, new MoveTypeKnight()));
//		register("rook", new Model("Rook", 5.0, new MoveTypeRook()));
//		register("bishop", new Model("Bishop", 3.0, new MoveTypeBishop()));
//		register("queen", new Model("Queen", 9.0, new MoveTypeBishop(), new MoveTypeRook()));
//		register("king", new Model("King", 1000.0, new MoveTypeKing()));
		JSONLoader.loadPieces();
	}
	
	public static Set<String> getModelID()
	{
		return MAP.keySet();
	}
}
