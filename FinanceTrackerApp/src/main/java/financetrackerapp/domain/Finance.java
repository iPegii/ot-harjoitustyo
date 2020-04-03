
package financetrackerapp.domain;

public class Finance {
    
    private String date;
    private String event;
    private int price;
    private String username;
    
    public Finance(String username, int price, String event, String date) {
        this.date = date;
        this.event = event;
        this.price = price;
        this.username = username;
    }
    
    
    public String getDate() {
        return date;
    }
    
    public String getEvent() {
        return event;
    }
    
    public Integer getPrice() {
        return price;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String toString() {
        return username + ";" + price + ";" + event + ";" + date;
    }
    
}
