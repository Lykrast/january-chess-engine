package com.nullprogram.chess.gui;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Color;

import javax.swing.Icon;

public class ColorThemeIcon implements Icon {
	private static final int CELL_SIZE = 16, PADDING = 1;
	private ColorTheme theme;
	
	public ColorThemeIcon(ColorTheme theme) {
		this.theme = theme;
	}

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		g.setColor(Color.BLACK);
		g.fillRect(x, y, getIconWidth(), getIconHeight());
		g.setColor(theme.getDarkTileColor());
		g.fillRect(x + PADDING, y + PADDING, CELL_SIZE, CELL_SIZE);
		g.setColor(theme.getLightTileColor());
		g.fillRect(x + CELL_SIZE + PADDING, y + PADDING, CELL_SIZE, CELL_SIZE);
	}

	@Override
	public int getIconWidth() {
		return CELL_SIZE * 2 + PADDING * 2;
	}

	@Override
	public int getIconHeight() {
		return CELL_SIZE + PADDING * 2;
	}

}
