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
import financetrackerapp.domain.Finance;
import java.lang.reflect.Type;
import org.bson.json.JsonParseException;

/**
 *
 * @author iPegii
 */



/**
 * This class is used to convert MongoDb's "_id" to "id",
 * so that Gson can convert Json to Java object
 */
public class FinanceType implements JsonDeserializer<Finance> {
    /* */
    @Override
    public Finance deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        
        JsonObject financeJson = json.getAsJsonObject();
        String id = financeJson.get("_id").getAsString();
        Double price = Double.valueOf(financeJson.get("price").getAsString());
        String event = financeJson.get("event").getAsString();
        String date = financeJson.get("date").getAsString();
        String user = financeJson.get("user").getAsString();
        Finance f = new Finance(id, price, event, date, user);
        f.getFormattedPrice();
        return f;
    }
    
    
}
