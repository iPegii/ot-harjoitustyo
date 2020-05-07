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
import financetrackerapp.domain.User;
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
public class UserService {
    
    private String apiKey;
    private String selectedDatabase;
    private MongoClient client;
    
    
 /**
 * This class is used to communicate between MongoDb and the app,
 * more precisely to communicate with users-collection
 */
    public UserService(String selectedDatabase) {
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
 * Method creates user to MongoDb
 * 
 * 
 * @return Created user
 */
    public User create(String username, String name, String passwordHash) {
        connect();
        MongoDatabase database = client.getDatabase(selectedDatabase);
        MongoCollection<Document> users = database.getCollection("users");
        
        String id =  new ObjectId().toString();
        Document userDocument = new Document("_id", id);
        userDocument.append("username", username)
            .append("name", name)
            .append("password", passwordHash);
        users.insertOne(userDocument);
        disconnect();
        User user = new User(username, name, id, passwordHash);
        return user;
    }
    
    public List<User> getAll() {
        List<User> userList = new ArrayList<>();
        connect();
        MongoDatabase database = client.getDatabase(selectedDatabase);
        MongoCollection<Document> users = database.getCollection("users");
        try (MongoCursor<Document> cursor = users.find().iterator()) {
            // UserType used to handle MongoDb "_id"
            Gson gson = new GsonBuilder().registerTypeAdapter(User.class, new UserType()).create();
            while (cursor.hasNext()) {
                String json = cursor.next().toJson();
                User user = gson.fromJson(json, User.class);
                userList.add(user);
            }
        } catch (Exception e) {
            System.out.println("Error while retrieving documents from MongoDb");
        }
        disconnect();
        return userList;
    }
    
    public void connect() {
        MongoClient mongoClient = null;
        try {
            java.util.logging.Logger.getLogger("org.mongodb.driver").setLevel(Level.SEVERE); 
            mongoClient = MongoClients.create(apiKey);
        } catch (Exception e) {
            System.out.println("Error while connecting to MongoDb");
        }
        this.client = mongoClient;
    }
    
    public Document updateUser(String id, String username, String newName, String passwordHash) {
        connect();
        MongoDatabase database = client.getDatabase(selectedDatabase);
        MongoCollection<Document> users = database.getCollection("users");
        Bson filter = and(eq("_id", id), eq("password", passwordHash));
        Document userDocument = new Document("_id", id);
        userDocument.append("username", username)
            .append("name", newName)
            .append("password", passwordHash);
            
        Document result = users.findOneAndReplace(filter, userDocument);
        disconnect();
        return result;
    }
    
    public void disconnect() {
        client.close();
    }
    
    public void deleteAll() {
        connect();
        MongoDatabase database = client.getDatabase(selectedDatabase);
        MongoCollection<Document> users = database.getCollection("users");
        users.deleteMany(new Document());
        disconnect();
    }
    
}
