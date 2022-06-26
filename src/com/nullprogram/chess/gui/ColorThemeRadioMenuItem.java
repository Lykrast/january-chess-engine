package com.nullprogram.chess.gui;

import javax.swing.JRadioButtonMenuItem;

public class ColorThemeRadioMenuItem extends JRadioButtonMenuItem {
	private static final long serialVersionUID = -6594599282634061526L;
	private ColorTheme theme;

	public ColorThemeRadioMenuItem(ColorTheme theme) {
		super(theme.getName(), new ColorThemeIcon(theme));
		this.theme = theme;
	}

	public ColorTheme getTheme() {
		return theme;
	}
}
