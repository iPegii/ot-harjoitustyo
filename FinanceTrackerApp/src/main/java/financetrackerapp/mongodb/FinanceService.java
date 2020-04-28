/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package financetrackerapp.mongodb;

import com.google.gson.Gson;
import com.mongodb.client.*;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;
import financetrackerapp.domain.Finance;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
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
                if (keys != null) {
                    keys.close();
                }
            } catch (IOException e)  {
                System.out.println("Error closing api key stream");  
            }
        }
        this.apiKey = prop.getProperty("mongodb.uri");
        this.selectedDatabase = selectedDatabase;
        
    }
    
    public Finance create(double price, String event, String date, String userId) {
        connect();
        java.util.logging.Logger.getLogger("org.mongodb.driver").setLevel(Level.SEVERE);  
        MongoDatabase database = client.getDatabase(selectedDatabase);
        MongoCollection<Document> finances = database.getCollection("finances");
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
        MongoDatabase database = client.getDatabase(selectedDatabase);
        MongoCollection<Document> finances = database.getCollection("finances");
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
        MongoDatabase database = client.getDatabase(selectedDatabase);
        MongoCollection<Document> finances = database.getCollection("finances");
        finances.deleteMany(new Document());
        disconnect();
    }
    
    public void updateFinance(String id, String newPrice, String newEvent, String newDate, String user) {
        Bson filter = and(eq("_id", id), eq("user", user));
        
        
        MongoDatabase database = client.getDatabase(selectedDatabase);
        MongoCollection<Document> finances = database.getCollection("finances");
        
        finances.updateOne(filter, and(set("price", newPrice), set("event", newEvent), set("date", newDate)));
        
    }
    
    public void connect() {
        MongoClient mongoClient = null;
        java.util.logging.Logger.getLogger("org.mongodb.driver").setLevel(Level.SEVERE);
        try {
            mongoClient = MongoClients.create(apiKey);
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
