
package financetrackerapp.domain;


public class Finance {
    
    private String date;
    private String event;
    private int price;
    private String _id;
    private String user;
    
    public Finance(String id, int price, String event, String date, String user) {
        this.date = date;
        this.event = event;
        this.price = price;
        this._id = id;
        this.user = user;
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
        return _id;
    }
    public String getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "Finance{" + "date=" + date + ", event=" + event + ", price=" + price + ", id=" + _id + ", user=" + user + '}';
    }
    
}
