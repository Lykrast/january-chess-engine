package com.nullprogram.chess.gui;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 * Creates a panel for selecting a player type.
 */
public class BoardSelector extends JPanel {

    /** Version for object serialization. */
    private static final long serialVersionUID = 1L;
    
    private static final String[] VARIANTS = {"Standard", "Gothic", "Wildebeest"};

    private final JRadioButton[] buttons = new JRadioButton[VARIANTS.length];

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

        ButtonGroup group = new ButtonGroup();
        for (int i=0;i<VARIANTS.length;i++)
        {
        	buttons[i] = new JRadioButton(VARIANTS[i]);
            group.add(buttons[i]);
            add(buttons[i]);
        }
        buttons[0].setSelected(true);

        setBorder(BorderFactory.createEmptyBorder(H_PADDING, V_PADDING,
                  H_PADDING, V_PADDING));
    }

    /**
     * Get the board selected by this dialog.
     *
     * @return the board type
     */
    public final String getBoard() {
    	for (JRadioButton b : buttons)
    		if (b.isSelected()) return b.getText().toLowerCase();
    	
    	throw new AssertionError("Unknown board selected!");
    }
}
