
package financetrackerapp.domain;


public class Finance {
    
    private String date;
    private String event;
    private double price;
    private String id;
    private String user;
    
    public Finance(String id, double price, String event, String date, String user) {
        this.date = date;
        this.event = event;
        this.price = price;
        this.id = id;
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
