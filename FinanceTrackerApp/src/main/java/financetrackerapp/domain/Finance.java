
package financetrackerapp.domain;


public class Finance {
    
    private String id;
    private double price;
    private String event;
    private String date;
    private String user;
    
    public Finance(String id, double price, String event, String date, String user) {
        this.id = id;
        this.price = price;
        this.event = event;
        this.date = date;
        this.user = user;
    }
    
    
    public String getDate() {
        return date;
    }
    
    public String getEvent() {
        return event;
    }
    
    public double getPrice() {
        return price;
    }
    
    public String getId() {
        return id;
    }
    public String getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "Finance{" + "date=" + date + ", event=" + event + ", price=" + price + ", id=" + id + ", user=" + user + '}';
    }
    
}
