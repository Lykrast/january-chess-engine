package com.nullprogram.chess.boards;

public class GameOptionGroup {
	private String name;
	private GameOption[] options;
	
	public GameOptionGroup(String name, GameOption[] options) {
		this.name = name;
		this.options = options;
	}

	public String getName() {
		return name;
	}

	public GameOption[] getOptions() {
		return options;
	}
	
	@Override
	public String toString() {
		return name;
	}

}
