package com.nullprogram.chess.boards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.nullprogram.chess.pieces.Piece;
import com.nullprogram.chess.pieces.Piece.Side;

public class GameModeOption extends GameMode {
	private GameOptionGroup[] options;
	private List<GameOption> selectedOptions;
	private String[] promotionsSelectedW, promotionsSelectedB;

	public GameModeOption(String name, int width, int height, PiecePlacement[] placements, String[] promotionsW, String[] promotionsB, boolean checkMultiple, GameOptionGroup[] options) {
		super(name, width, height, placements, promotionsW, promotionsB, checkMultiple);
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
