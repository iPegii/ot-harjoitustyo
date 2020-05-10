/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package financetrackerapp.mongodb;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.*;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import com.mongodb.client.result.DeleteResult;
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


/**
 * This class is used to communicate between MongoDb - finances-collection and
 * the app.
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
    
 /**
 * Method creates finance to MongoDb, finances are always linked to user by id.
 * 
 * 
     * @param price finance price
     * @param event finance event
     * @param date finance date
     * @param userId user who created the finance
 * @return Created finance
 */
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
            // FinanceType used to handle MongoDb "_id"
            Gson gson = new GsonBuilder().registerTypeAdapter(Finance.class, new FinanceType()).create();
            while (cursor.hasNext()) {
                String json = cursor.next().toJson();
                Finance finance = gson.fromJson(json, Finance.class);
                financeList.add(finance);
            }
        } catch (Exception e) {
            System.out.println("Error while retrieving documents from MongoDb: " + e.getMessage());
        }
        disconnect();
        return financeList;
    }
    
    public Boolean deleteFinance(String id, String userId) {
        connect();
        MongoDatabase database = client.getDatabase(selectedDatabase);
        MongoCollection<Document> finances = database.getCollection("finances");
        DeleteResult result = finances.deleteOne(eq("_id", id));
        disconnect();
        return result.wasAcknowledged();
    }
    
    public void deleteAll() {
        connect();
        MongoDatabase database = client.getDatabase(selectedDatabase);
        MongoCollection<Document> finances = database.getCollection("finances");
        finances.deleteMany(new Document());
        disconnect();
    }
    
    public Document updateFinance(String id, Double newPrice, String newEvent, String newDate, String user) {
        connect();
        MongoDatabase database = client.getDatabase(selectedDatabase);
        MongoCollection<Document> finances = database.getCollection("finances");
        Bson filter = and(eq("_id", id), eq("user", user));
        Document financeDocument = new Document("_id", id);
        financeDocument.append("price", newPrice)
            .append("event", newEvent)
            .append("date", newDate)
            .append("user", user);
        Document result = finances.findOneAndReplace(filter, financeDocument);
        disconnect();
        return result;
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
}