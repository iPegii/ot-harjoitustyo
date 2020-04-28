/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package financetrackerapp.mongodb;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import financetrackerapp.domain.User;
import java.lang.reflect.Type;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bson.json.JsonParseException;

/**
 *
 * @author iPegii
 */


/**
 * This class is used to convert MongoDb's "_id" to "id",
 * so that Gson can convert Json to Java object
 */
public class UserType implements JsonDeserializer<User> {
   /* */ 
    @Override
    public User deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        
        JsonObject userJson = json.getAsJsonObject();
        String id = userJson.get("_id").getAsString();
        String username = userJson.get("username").getAsString();
        String name = userJson.get("name").getAsString();
        
        return new User(username, name, id);
    }
    
    
}
