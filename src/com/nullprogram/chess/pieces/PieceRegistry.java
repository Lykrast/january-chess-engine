package com.nullprogram.chess.pieces;

import java.util.HashMap;
import java.util.Set;

import com.nullprogram.chess.Model;
import com.nullprogram.chess.resources.JSONLoader;

public class PieceRegistry {
	private static final HashMap<String, Model> MAP = new HashMap<>();
	
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
		JSONLoader.loadPieces();
	}
	
	public static Set<String> getModelID()
	{
		return MAP.keySet();
	}
	
	public static boolean exists(String id)
	{
		return MAP.containsKey(id);
	}
}
