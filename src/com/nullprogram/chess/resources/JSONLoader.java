package com.nullprogram.chess.resources;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.GameMode;
import com.nullprogram.chess.IMoveType;
import com.nullprogram.chess.Model;
import com.nullprogram.chess.MoveType;
import com.nullprogram.chess.boards.GameModeRegistry;
import com.nullprogram.chess.boards.PiecePlacement;
import com.nullprogram.chess.pieces.PieceRegistry;

public class JSONLoader {
	private static final String PATH_PIECES = "./resources/pieces";
	private static final String PATH_GAMEMODES = "./resources/gamemodes";

    /** This class's Logger. */
    private static final Logger LOG =
        Logger.getLogger("com.nullprogram.chess.resources.JSONLoader");
	
	private JSONLoader() {}
	
	public static void loadPieces() {
		MoveType.registerDeserializers();
		Gson g = new GsonBuilder()
				.registerTypeAdapter(Model.class, ModelDeserializer.INSTANCE)
				.registerTypeAdapter(IMoveType.class, MoveTypeDeserializer.INSTANCE)
				.create();
		
		File folder = new File(PATH_PIECES);
		loadPiecesFolder(g, folder);
	}
	
	private static void loadPiecesFolder(Gson g, File folder)
	{
		File[] pieces = folder.listFiles();
		
		for (File f : pieces)
		{
			if (f.isDirectory()) loadPiecesFolder(g, f);
			else if (f.getName().endsWith(".json"))
			{
				try {
					try (FileReader reader = new FileReader(f)) {
						Model m = g.fromJson(reader, Model.class);
						String name = f.getName().toLowerCase();
						name = name.substring(0, name.lastIndexOf(".json"));
						PieceRegistry.register(name, m);
					}
					catch (JsonParseException e) {
			            String message = "Failed to parse piece: " + f + ": " + e;
			            LOG.severe(message);
					}
				} catch (IOException e) {
		            String message = "Failed to load piece file: " + f + ": " + e;
		            LOG.severe(message);
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
		loadGameModesFolder(g, folder);		
	}
	
	private static void loadGameModesFolder(Gson g, File folder)
	{
		File[] pieces = folder.listFiles();
		
		for (File f : pieces)
		{
			if (f.isDirectory()) loadGameModesFolder(g, f);
			if (f.getName().endsWith(".json"))
			{
				try {
					try (FileReader reader = new FileReader(f)) {
						GameMode m = g.fromJson(reader, GameMode.class);
						String name = f.getName().toLowerCase();
						name = name.substring(0, name.lastIndexOf(".json"));
						GameModeRegistry.register(name, m);
					}
					catch (JsonParseException e) {
			            String message = "Failed to parse gamemode: " + f + ": " + e;
			            LOG.severe(message);
					}
				} catch (IOException e) {
		            String message = "Failed to load gamemode file: " + f + ": " + e;
		            LOG.severe(message);
				}
			}
		}	
	}
}
