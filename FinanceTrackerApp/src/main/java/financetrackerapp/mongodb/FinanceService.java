/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package financetrackerapp.mongodb;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import static com.mongodb.client.model.Filters.eq;
import com.mongodb.client.model.ValidationOptions;
import financetrackerapp.domain.Finance;
import financetrackerapp.domain.User;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.bson.BsonType;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

/**
 *
 * @author iPegii
 */
public class FinanceService {
    
    private String apiKey;
    private String selectedDatabase;
    private MongoClient client;
    private MongoCollection<Document> finances;
    
    public FinanceService(String selectedDatabase) {
        Properties prop = new Properties();
        FileInputStream keys = null;
        try {
            keys = new FileInputStream("keys.properties");
            prop.load(keys);
        } catch (IOException e) {
            System.out.println("Error while reading api key");
        } catch (Exception e) {
            System.out.println("Error while trying to get api key");
        } finally {
            try {
                keys.close();
            } catch (IOException e)  {
                System.out.println("Error closing api key stream");  
            }
        }
        String apiKey = prop.getProperty("mongodb.uri");
        this.apiKey = apiKey;
        this.selectedDatabase = selectedDatabase;
        
    }
    
    public Finance create(int price, String event, String date, String userId) {
        connect();
        String id = new ObjectId().toString();
        Document financeDocument = new Document("_id", id);
        financeDocument.append("price", price)
            .append("event", event)
            .append("date", date)
            .append("user", userId);
        finances.insertOne(financeDocument);
        Finance finance = new Finance(id, price, event, date, userId);
        disconnect();
        return finance;
    }
    
    public List<Finance> getAll() {
        List<Finance> financeList = new ArrayList<>();
        connect();
        try (MongoCursor<Document> cursor = finances.find().iterator()) {
            Gson gson = new Gson();
            while (cursor.hasNext()) {
                String json = cursor.next().toJson();
                Finance finance = gson.fromJson(json, Finance.class);
                financeList.add(finance);
            }
        } catch (Exception e) {
            System.out.println("Error while retrieving documents from MongoDb");
        }
        disconnect();
        return financeList;
    }
    
    public void delete(ObjectId id, ObjectId userId) {
        connect();
           // finances.deleteOne(eq("_id", id), eq("user"));
        disconnect();
    }
    
    public void deleteAll() {
        connect();
        finances.deleteMany(new Document());
        disconnect();
    }
    
    public void connect() {
        MongoClient mongoClient = null;
        try {
            mongoClient = MongoClients.create(apiKey);
            MongoDatabase database = mongoClient.getDatabase(selectedDatabase);
            this.finances = database.getCollection("finances");
        } catch (Exception e) {
            System.out.println("Error while connecting to MongoDb: " + e.getMessage());
        }
        this.client = mongoClient;
    }
    
    public void disconnect() {
        client.close();
    }
    
    public Document getFinanceSchema() {
        /*
    Bson price = Filters.type("price", BsonType.STRING);
    Bson event = Filters.regex("event", BsonType.STRING);
    Bson date = Filters.type("password", BsonType.);
    Bson id = Filters.type("password", BsonType.STRING);
    

    Bson validator = Filters.and(username, email, password);

    ValidationOptions validationOptions = new ValidationOptions()
                                          .validator(validator);
    database.createCollection("accounts", new CreateCollectionOptions()
                                          .validationOptions(validationOptions));
*/
        return new Document();
    }

}