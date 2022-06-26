package com.nullprogram.chess.resources;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.boards.BoardLine;
import com.nullprogram.chess.boards.GameMode;
import com.nullprogram.chess.boards.GameModeRegistry;
import com.nullprogram.chess.boards.GameOption;
import com.nullprogram.chess.boards.GameOptionGroup;
import com.nullprogram.chess.boards.PiecePlacement;
import com.nullprogram.chess.pieces.Model;
import com.nullprogram.chess.pieces.PieceRegistry;
import com.nullprogram.chess.pieces.movement.IMoveType;
import com.nullprogram.chess.pieces.movement.MoveType;

public class JSONLoader {
	private static final String PATH_PIECES = "./resources/pieces";
	private static final String PATH_GAMEMODES = "./resources/gamemodes";
	private static final int PATH_LENGTH_PIECES = PATH_PIECES.length() + 1;
	private static final int PATH_LENGTH_GAMEMODES = PATH_GAMEMODES.length() + 1;

    /** This class's Logger. */
    private static final Logger LOG = Logger.getLogger("com.nullprogram.chess.resources.JSONLoader");
	
	private JSONLoader() {}
	
	private static int errorCount;
	private static StringBuilder errorBuilder;
	
	public static void loadPieces() {
		MoveType.registerDeserializers();
		Gson g = new GsonBuilder()
				.registerTypeAdapter(Model.class, ModelDeserializer.INSTANCE)
				.registerTypeAdapter(IMoveType.class, MoveTypeDeserializer.INSTANCE)
				.create();
		
		File folder = new File(PATH_PIECES);
		errorCount = 0;
		errorBuilder = new StringBuilder();
		loadPiecesFolder(g, folder);
		if (errorCount > 0) {
			errorBuilder.insert(0, errorCount + " pieces could not be loaded.");
			JOptionPane.showMessageDialog(null, errorBuilder.toString(), "Piece loading error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private static void loadPiecesFolder(Gson g, File folder) {
		File[] pieces = folder.listFiles();

		for (File f : pieces) {
			if (f.isDirectory()) loadPiecesFolder(g, f);
			else if (f.getName().endsWith(".json")) {
				try {
					try (FileReader reader = new FileReader(f)) {
						Model m = g.fromJson(reader, Model.class);
						String name = f.getName().toLowerCase();
						name = name.substring(0, name.lastIndexOf(".json"));
						PieceRegistry.register(name, m);
					}
					catch (JsonParseException e) {
						String message = "Failed to parse piece " + f.toString().substring(PATH_LENGTH_PIECES) + ": " + e;
						LOG.severe(message);
						errorCount++;
						errorBuilder.append(System.lineSeparator());
						errorBuilder.append(message);
					}
				}
				catch (IOException e) {
					String message = "Failed to load piece file " + f.toString().substring(PATH_LENGTH_PIECES) + ": " + e;
					LOG.severe(message);
					errorCount++;
					errorBuilder.append(System.lineSeparator());
					errorBuilder.append(message);
				}
			}
		}
	}
	
	public static void loadGameModes() {
		MoveType.registerDeserializers();
		Gson g = new GsonBuilder()
				.registerTypeAdapter(GameMode.class, GameModeDeserializer.INSTANCE)
				.registerTypeAdapter(PiecePlacement.class, PiecePlacementDeserializer.INSTANCE)
				.registerTypeAdapter(GameOption.class, GameOptionDeserializer.INSTANCE)
				.registerTypeAdapter(GameOptionGroup.class, GameOptionGroupDeserializer.INSTANCE)
				.registerTypeAdapter(BoardLine.class, BoardLineDeserializer.INSTANCE)
				.create();
		
		File folder = new File(PATH_GAMEMODES);
		errorCount = 0;
		errorBuilder = new StringBuilder();
		loadGameModesFolder(g, folder);
		if (errorCount > 0) {
			errorBuilder.insert(0, errorCount + " gamemodes could not be loaded.");
			JOptionPane.showMessageDialog(null, errorBuilder.toString(), "Gamemode loading error", JOptionPane.ERROR_MESSAGE);
		}
		errorBuilder = null;
	}
	
	private static void loadGameModesFolder(Gson g, File folder) {
		File[] pieces = folder.listFiles();

		for (File f : pieces) {
			if (f.isDirectory()) loadGameModesFolder(g, f);
			if (f.getName().endsWith(".json")) {
				try {
					try (FileReader reader = new FileReader(f)) {
						GameMode m = g.fromJson(reader, GameMode.class);
						String name = f.getName().toLowerCase();
						name = name.substring(0, name.lastIndexOf(".json"));
						GameModeRegistry.register(name, m);
					}
					catch (JsonParseException e) {
						String message = "Failed to parse gamemode " + f.toString().substring(PATH_LENGTH_GAMEMODES) + ": " + e;
						LOG.severe(message);
						errorCount++;
						errorBuilder.append(System.lineSeparator());
						errorBuilder.append(message);
					}
				}
				catch (IOException e) {
					String message = "Failed to load gamemode file " + f.toString().substring(PATH_LENGTH_GAMEMODES) + ": " + e;
					LOG.severe(message);
					errorCount++;
					errorBuilder.append(System.lineSeparator());
					errorBuilder.append(message);
				}
			}
		}
	}
}
