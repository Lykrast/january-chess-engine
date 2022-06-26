package com.nullprogram.chess.boards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.nullprogram.chess.pieces.Piece;
import com.nullprogram.chess.pieces.Piece.Side;

public class GameModeOption extends GameMode {
	private GameOptionGroup[] options;
	private List<GameOption> selectedOptions;
	private String[] promotionsSelectedW, promotionsSelectedB;

	public GameModeOption(String name, String description, int width, int height, PiecePlacement[] placements, String[] promotionsW, String[] promotionsB, boolean checkMultiple, BoardLine[] lines, GameOptionGroup[] options) {
		super(name, description, width, height, placements, promotionsW, promotionsB, checkMultiple, lines);
		this.options = options;
		setSelected(new ArrayList<GameOption>());
	}

	public void setSelected(List<GameOption> list) {
		selectedOptions = list;
		List<String> listWhite = new ArrayList<>(), listBlack = new ArrayList<>();
		listWhite.addAll(Arrays.asList(promotionsW));
		listBlack.addAll(Arrays.asList(promotionsB));
		
		for (GameOption o : list)
		{
			listWhite.addAll(Arrays.asList(o.getPromotions(Side.WHITE)));
			listBlack.addAll(Arrays.asList(o.getPromotions(Side.BLACK)));
		}
		
		//Remove duplicates
		listWhite = listWhite.stream().distinct().collect(Collectors.toList());
		listBlack = listBlack.stream().distinct().collect(Collectors.toList());
		
		promotionsSelectedW = listWhite.toArray(new String[listWhite.size()]);
		promotionsSelectedB = listBlack.toArray(new String[listBlack.size()]);
	}
	
	public GameOptionGroup[] getOptionGroups() {
		return options;
	}
	
	@Override
	public void placePieces(Board b)
	{
		for (PiecePlacement p : placements) p.place(b);
		for (GameOption o : selectedOptions) o.placePieces(b);
	}
	
	@Override
	public String[] getPromotions(Piece.Side side) {
		return side == Piece.Side.WHITE ? promotionsSelectedW : promotionsSelectedB;
	}
}
