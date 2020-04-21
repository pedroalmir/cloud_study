/**
 * 
 */
package com.pedroalmir.ssnetwork.util;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * @author Pedro Almir
 */
public class GsonDateAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {
	
	private final DateFormat dateFormat;

    public GsonDateAdapter() {
      dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
      
      //This is the key line which converts the date to UTC which cannot be accessed with the default serializer
      dateFormat.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
    }

	@Override
	public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		try {
			return dateFormat.parse(json.getAsString());
		} catch (ParseException e) {
			throw new JsonParseException(e);
		}
	}

	@Override
	public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
		return new JsonPrimitive(dateFormat.format(src));
	}

}
