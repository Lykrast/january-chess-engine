package com.nullprogram.chess.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class ColorTheme {
	private Color darkTile, lightTile;
	private String name;
	
	public ColorTheme(String name, int dark, int light) {
		darkTile = new Color(dark);
		lightTile = new Color(light);
		this.name = name;
	}

	public Color getDarkTileColor() {
		return darkTile;
	}

	public Color getLightTileColor() {
		return lightTile;
	}

	public String getName() {
		return name;
	}

	private static List<ColorTheme> themes;
	static {
		themes = new ArrayList<>();
		themes.add(new ColorTheme("Wood", 0xD18B47, 0xFFCE9E));
		themes.add(new ColorTheme("Metal", 0xC4C4C4, 0xE7E7E7));
		themes.add(new ColorTheme("Garden", 0x339933, 0xCCCC11));
		themes.add(new ColorTheme("Metro", 0x527362, 0xF1F2CB));
		themes.add(new ColorTheme("Ancient", 0x8F976D, 0xF2F2B8));
		themes.add(new ColorTheme("Lava", 0x605E5F, 0xA0676E));
	}
	
	public static List<ColorTheme> getThemes() {
		return themes;
	}
}