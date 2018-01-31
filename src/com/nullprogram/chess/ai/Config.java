package com.nullprogram.chess.ai;

import java.util.HashMap;
import java.util.Properties;

import com.nullprogram.chess.Piece;

/**
 * Represents a particular configuration of an AI player.
 */
public class Config extends HashMap<String, Double> {

    /** List of all currently existing properties. */
//    static final String[] PLIST = {
//        "depth", "Pawn", "Knight", "Bishop", "Rook", "Queen", "King",
//        "Chancellor", "Archbishop", "material", "safety", "mobility"
//    };
	static final String[] PLIST = new String[Piece.PIECES.length+4];
	static
	{
		PLIST[0] = "depth";
		PLIST[1] = "material";
		PLIST[2] = "safety";
		PLIST[3] = "mobility";
		for (int i=0;i<Piece.PIECES.length;i++) PLIST[i+4] = Piece.PIECES[i];
	}

    /** Version for object serialization. */
    private static final long serialVersionUID = 1L;

    /**
     * Create a new configuration.
     */
    public Config() {
        super();
    }

    /**
     * Create a config from a set of properties.
     *
     * @param props the proprties to derive from
     */
    public Config(final Properties props) {
        for (String prop : PLIST) {
            put(prop, Double.parseDouble(props.getProperty(prop)));
        }
    }

    /**
     * Create a properties object from tihs config.
     *
     * @return the properteies that matches this config
     */
    public final Properties getProperties() {
        Properties props = new Properties();
        for (String prop : PLIST) {
            props.setProperty(prop, "" + get(prop));
        }
        return props;
    }

    @Override
    public final String toString() {
        StringBuilder str = new StringBuilder();
        for (String prop : PLIST) {
            str.append(prop + "=" + get(prop) + ",");
        }
        return str.toString();
    }
}
