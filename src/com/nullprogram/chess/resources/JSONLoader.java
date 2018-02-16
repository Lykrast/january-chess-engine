package com.nullprogram.chess.resources;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nullprogram.chess.GameMode;
import com.nullprogram.chess.Model;
import com.nullprogram.chess.MoveType;
import com.nullprogram.chess.boards.GameModeRegistry;
import com.nullprogram.chess.boards.PiecePlacement;
import com.nullprogram.chess.pieces.PieceRegistry;

public class JSONLoader {
	private static final String PATH_PIECES = "./resources/pieces";
	private static final String PATH_GAMEMODES = "./resources/gamemodes";
	
	private JSONLoader() {}
	
	public static void loadPieces() {
		MoveType.registerDeserializers();
		Gson g = new GsonBuilder()
				.registerTypeAdapter(Model.class, ModelDeserializer.INSTANCE)
				.registerTypeAdapter(MoveType.class, MoveTypeDeserializer.INSTANCE)
				.create();
		
		File folder = new File(PATH_PIECES);
		File[] pieces = folder.listFiles();
		
		for (File f : pieces)
		{
			if (f.getName().endsWith(".json"))
			{
				try {
					FileReader reader = new FileReader(f);
					Model m = g.fromJson(reader, Model.class);
					String name = f.getName().toLowerCase();
					name = name.substring(0, name.lastIndexOf(".json"));
					PieceRegistry.register(name, m);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void loadGameModes() {
		MoveType.registerDeserializers();
		Gson g = new GsonBuilder()
				.registerTypeAdapter(GameMode.class, GameModeDeserializer.INSTANCE)
				.registerTypeAdapter(PiecePlacement.class, PiecePlacementDeserializer.INSTANCE)
				.create();
		
		File folder = new File(PATH_GAMEMODES);
		File[] pieces = folder.listFiles();
		
		for (File f : pieces)
		{
			if (f.getName().endsWith(".json"))
			{
				try {
					FileReader reader = new FileReader(f);
					GameMode m = g.fromJson(reader, GameMode.class);
					String name = f.getName().toLowerCase();
					name = name.substring(0, name.lastIndexOf(".json"));
					GameModeRegistry.register(name, m);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}		
	}
}
