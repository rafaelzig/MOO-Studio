package uk.co.blogspot.rafaelzig.core.parsing;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * Class utilised by GSON to offer custom serialisation and deserialisation of
 * interfaces at runtime.
 *
 * @author Rafael da Silva Costa - BSc Computer Science 3rd Year
 *
 * @param <T>
 *            : Type of object being serialised / deserialised.
 */
public class InterfaceAdapter<T> implements JsonSerializer<T>, JsonDeserializer<T>
{
	/**
	 * Package name of class being instantiated.
	 */
	private final String	packageName;

	/**
	 * Constructs a new instance of InterfaceAdapter with the specified package
	 * name.
	 *
	 * @param packageName
	 *            : Package name of class being instantiated.
	 */
	public InterfaceAdapter(String packageName)
	{
		super();
		this.packageName = packageName;
	}

	@Override
	public T deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException
	{
		final JsonObject obj = json.getAsJsonObject();
		final String type = obj.get("type").getAsString();
		final JsonElement element = obj.get("properties");

		try
		{
			return context.deserialize(element, Class.forName(packageName + "." + type));
		}
		catch (final ClassNotFoundException cnfe)
		{
			throw new JsonParseException("Unknown element type: " + type, cnfe);
		}
	}

	@Override
	public JsonElement serialize(T tmp, Type type, JsonSerializationContext context)
	{
		final JsonObject json = new JsonObject();
		json.add("type", new JsonPrimitive(tmp.getClass().getSimpleName()));
		json.add("properties", context.serialize(tmp, tmp.getClass()));
		return json;
	}
}