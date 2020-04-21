
package financetrackerapp.domain;


public class Finance {
    
    private String date;
    private String event;
    private int price;
    private String id;
    private String userId;
    
    public Finance(String id, int price, String event, String date, String userId) {
        this.date = date;
        this.event = event;
        this.price = price;
        this.id = id;
        this.userId = userId;
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
    
    public String getId() {
        return id;
    }
    public String getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "Finance{" + "date=" + date + ", event=" + event + ", price=" + price + ", id=" + id + ", userId=" + userId + '}';
    }
    
}
