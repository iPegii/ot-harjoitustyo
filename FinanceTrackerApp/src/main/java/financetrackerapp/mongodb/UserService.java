/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package financetrackerapp.mongodb;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.*;
import financetrackerapp.domain.User;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import org.bson.Document;
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
     * @param selectedDatabase is the database in which the collections are created
 */
    public UserService(String selectedDatabase) {
        Properties prop = new Properties();
        FileInputStream keys = null;
        try {
            keys = new FileInputStream("keys.properties");
            prop.load(keys);
        } catch (IOException e) {
            System.out.println("Error while reading api key: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error while trying to get api key: " + e.getMessage());
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
     * @param username username of the user
     * @param name name of the user
     * @param passwordHash hash of user's password
 * @return Created user
 */
    public User create(String username, String name, String passwordHash) {
        connect();
        MongoDatabase database = client.getDatabase(selectedDatabase);
        MongoCollection<Document> users = database.getCollection("users");
        java.util.logging.Logger.getLogger("org.mongodb.driver").setLevel(Level.SEVERE);
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
    
    
/**
 * Method retrieves all the users from MongoDB
 * 
 * @return retrieved list of User-objects
 */
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
            System.out.println("Error while retrieving documents from MongoDb: " + e.getMessage());
        }
        disconnect();
        return userList;
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
    
    public void deleteAll() {
        connect();
        MongoDatabase database = client.getDatabase(selectedDatabase);
        MongoCollection<Document> users = database.getCollection("users");
        users.deleteMany(new Document());
        disconnect();
    }
    
}
