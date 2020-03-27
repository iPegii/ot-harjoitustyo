
package domain;

public class Finance {
    
    private String date;
    private String event;
    private int price;
    
    public Finance(int price, String event, String date) {
        this.date = date;
        this.event = event;
        this.price = price;
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
    
    public String toString() {
        return "price: " + price + ", event: " + event + ", date: " + date;
    }
    
}
