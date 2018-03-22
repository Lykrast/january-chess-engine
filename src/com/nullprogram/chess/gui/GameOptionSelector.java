package com.nullprogram.chess.gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.nullprogram.chess.boards.GameModeOption;
import com.nullprogram.chess.boards.GameOption;
import com.nullprogram.chess.boards.GameOptionGroup;

public class GameOptionSelector extends JPanel {
	private static final long serialVersionUID = -6805533429045201652L;
	private List<JComboBox<GameOption>> options;

	public GameOptionSelector(GameModeOption mode) {
		super();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		options = new ArrayList<>();
		
		for (GameOptionGroup group : mode.getOptionGroups())
		{
			add(new JLabel(group.toString()));
			
			JComboBox<GameOption> box = new JComboBox<>(group.getOptions());
			box.setMaximumSize(box.getPreferredSize());
			options.add(box);
			add(box);
		}
	}
	
	public List<GameOption> getSelectedOptions()
	{
		List<GameOption> list = new ArrayList<>();
		
		for (JComboBox<GameOption> box : options)
		{
			list.add(box.getItemAt(box.getSelectedIndex()));
		}
		
		return list;
	}

}
