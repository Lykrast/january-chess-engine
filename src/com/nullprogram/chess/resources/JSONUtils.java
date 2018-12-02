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
	 * @param json JsonObject to get the element from
	 * @param name name of the element to get
	 * @return found JsonElement
	 * @throws JsonParseException if no element was found
	 */
	public static JsonElement getMandatory(JsonObject json, String name) throws JsonParseException {
		JsonElement e = json.get(name);
		if (e == null) throw new JsonParseException("Missing mandatory value: " + name);
		
		return e;
	}
	
	/**
	 * Attempts to get an int from a Json Object, returning a default value if it isn't found.
	 * @param json JsonObject to get the int from
	 * @param name name of the int to get
	 * @param defaultValue value to return if the field isn't found
	 * @return the value of the named field if it's found, the provided default value otherwise
	 */
	public static int getDefaultInt(JsonObject json, String name, int defaultValue) {
		JsonElement elem = json.get(name);
		return elem == null ? defaultValue : elem.getAsInt();
	}
	
	/**
	 * Attempts to get a boolean from a Json Object, returning a default value if it isn't found.
	 * @param json JsonObject to get the int from
	 * @param name name of the int to get
	 * @param defaultValue value to return if the field isn't found
	 * @return the value of the named field if it's found, the provided default value otherwise
	 */
	public static boolean getDefaultBoolean(JsonObject json, String name, boolean defaultValue) {
		JsonElement elem = json.get(name);
		return elem == null ? defaultValue : elem.getAsBoolean();
	}

}
