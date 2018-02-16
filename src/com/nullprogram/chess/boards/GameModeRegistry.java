package com.nullprogram.chess.boards;

import java.util.HashMap;
import java.util.Set;

import com.nullprogram.chess.GameMode;
import com.nullprogram.chess.pieces.PieceRegistry;
import com.nullprogram.chess.resources.JSONLoader;

public class GameModeRegistry {
	private static final HashMap<String, GameMode> MAP = new HashMap<>();
	
	static
	{
		PieceRegistry.init();
		init();
	}
	
	private GameModeRegistry() {};
	
	public static GameMode get(String id)
	{
		return MAP.get(id);
	}
	
	public static void register(String name, GameMode m)
	{
		MAP.put(name, m);
	}
	
	public static void init()
	{
		JSONLoader.loadGameModes();
	}
	
	public static Set<String> getModeID()
	{
		return MAP.keySet();
	}
}
