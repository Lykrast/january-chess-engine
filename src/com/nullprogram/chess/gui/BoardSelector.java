package com.nullprogram.chess.gui;

import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import com.nullprogram.chess.GameMode;
import com.nullprogram.chess.boards.GameModeRegistry;

/**
 * Creates a panel for selecting a player type.
 */
public class BoardSelector extends JPanel {

    /** Version for object serialization. */
    private static final long serialVersionUID = 1L;
    
    private JList<GameMode> modes = new JList<>();

    /** Vertical padding around this panel. */
    static final int V_PADDING = 15;

    /** Horizontal padding around this panel. */
    static final int H_PADDING = 10;

    /**
     * Creates a player selector panel.
     */
    public BoardSelector() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JLabel label = new JLabel("Game type:");
        add(label);
        
        Collection<GameMode> modeList = GameModeRegistry.getGameModes();
        modes = new JList<>(modeList.toArray(new GameMode[modeList.size()]));
        modes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        modes.setSelectedIndex(0);
        
        JScrollPane scroll = new JScrollPane(modes);
        add(scroll);

        setBorder(BorderFactory.createEmptyBorder(H_PADDING, V_PADDING,
                  H_PADDING, V_PADDING));
    }

    /**
     * Get the board selected by this dialog.
     *
     * @return the board type
     */
    public final GameMode getBoard() {
    	return modes.getSelectedValue();
    }
}
