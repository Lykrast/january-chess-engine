package com.nullprogram.chess.resources;

import java.awt.Image;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

/**
 * Serves cached images of requested size.
 *
 * This will cache the recent requests so it's not hitting the disk
 * every time the display needs an image.
 */
public final class ImageServer {
	private static final String PATH = "./resources/images/";
	public static final String WHITE = "-WHITE", BLACK = "-BLACK";
	
	private static final String U_WHITE = "Unknown-WHITE", U_BLACK = "Unknown-BLACK";

    /** This class's Logger. */
    private static final Logger LOG =
        Logger.getLogger("com.nullprogram.chess.pieces.ImageServer");

    /** The image cache. */
    private static Map<String, Image> cache =
        new HashMap<String, Image>();

    /**
     * Hidden constructor.
     */
    private ImageServer() {
    }

    /**
     * Return named image scaled to given size.
     *
     * @param name name of the image
     * @return     the requested image
     */
    public static Image getTile(final String name) {
        Image cached = cache.get(name);
        if (cached != null) {
            return cached;
        }

        String file = name + ".png";
        try {
            //Image i = ImageIO.read(ImageServer.class.getResource(file));
            Image i = ImageIO.read(new File(PATH + file));
            cache.put(name, i);
            return i;
        } catch (java.io.IOException e) {
            String message = "Failed to read image: " + file + ": " + e;
            LOG.severe(message);
            return handleUnknown(name);
        } catch (IllegalArgumentException e) {
            String message = "Failed to find image: " + file + ": " + e;
            LOG.severe(message);
            return handleUnknown(name);
        }
    }
    
    /**
     * Handle the error case of a image that couldn't load. Attemps to put the appropriate Unknown image in the cache, or crash if it can't.
     */
    private static Image handleUnknown(final String original)
    {
    	if (original.equals(U_WHITE) || original.equals(U_BLACK))
    	{
            LOG.severe("Failed to load default image icon.");
            System.exit(1);
    	}
    	
    	if (original.endsWith(WHITE))
    	{
            LOG.severe("Loading white Unknown icon.");
    		Image i = getTile(U_WHITE);
            cache.put(original, i);
            return i;
    	}
    	if (original.endsWith(BLACK))
    	{
            LOG.severe("Loading black Unknown icon.");
    		Image i = getTile(U_BLACK);
            cache.put(original, i);
            return i;
    	}

        LOG.severe("No replacement image found.");
        System.exit(1);
    	return null;
    }
}
