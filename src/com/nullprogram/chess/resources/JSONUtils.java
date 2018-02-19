package com.nullprogram.chess.resources;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

/**
 * Utility class for the various Json parsing that happens.
 * @author Lykrast
 */
public class JSONUtils {

	private JSONUtils() {}
	
	/**
	 * Get an Element from a Json Object, throwing an exception if it's not found.
	 * <br>
	 * Used by parsers to error when they don't have the values they want, as it is more explicit than NullPointerExceptions.
	 * @param json JsonObject to get the element from.
	 * @param name name of the element to get
	 * @return found JsonElement
	 * @throws JsonParseException if no element was found
	 */
	public static JsonElement getMandatory(JsonObject json, String name) throws JsonParseException {
		JsonElement e = json.get(name);
		if (e == null) throw new JsonParseException("Missing mandatory value: " + name);
		
		return e;
	}

}
