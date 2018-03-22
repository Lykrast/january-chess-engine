package com.nullprogram.chess.gui;

import java.util.Arrays;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.nullprogram.chess.boards.GameMode;
import com.nullprogram.chess.boards.GameModeOption;
import com.nullprogram.chess.boards.GameModeRegistry;

/**
 * Creates a panel for selecting a player type.
 */
public class BoardSelector extends JPanel implements ListSelectionListener {

    /** Version for object serialization. */
    private static final long serialVersionUID = 1L;
    
    private JList<GameMode> modes = new JList<>();
    private GameOptionSelector optionSelector;

    /** Vertical padding around this panel. */
    static final int V_PADDING = 15;

    /** Horizontal padding around this panel. */
    static final int H_PADDING = 10;

    /**
     * Creates a player selector panel.
     */
    public BoardSelector() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        
        JPanel insidePanel = new JPanel();
        insidePanel.setLayout(new BoxLayout(insidePanel, BoxLayout.Y_AXIS));
        
        JLabel label = new JLabel("Game type:");
        insidePanel.add(label);
        
        Collection<GameMode> modeList = GameModeRegistry.getGameModes();
        GameMode[] modeArray = modeList.toArray(new GameMode[modeList.size()]);
        Arrays.sort(modeArray);
        modes = new JList<>(modeArray);
        modes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        modes.setSelectedIndex(0);
        modes.addListSelectionListener(this);
        
        JScrollPane scroll = new JScrollPane(modes);
        insidePanel.add(scroll);
        
        add(insidePanel);

        setBorder(BorderFactory.createEmptyBorder(H_PADDING, V_PADDING,
                  H_PADDING, V_PADDING));
    }

    /**
     * Get the board selected by this dialog.
     *
     * @return the board type
     */
    public final GameMode getBoard() {
    	GameMode selected = modes.getSelectedValue();
    	
    	if (optionSelector != null && selected instanceof GameModeOption)
    	{
    		((GameModeOption)selected).setSelected(optionSelector.getSelectedOptions());
    	}
    	
    	return selected;
    }

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting()) return;
		
		if (modes.getSelectedValue() instanceof GameModeOption)
		{
			if (optionSelector != null) remove(optionSelector);
			optionSelector = new GameOptionSelector((GameModeOption) modes.getSelectedValue());
			add(optionSelector);
			revalidate();
		}
		else if (optionSelector != null)
		{
			remove(optionSelector);
			revalidate();
		}
	}
}
