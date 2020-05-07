
package financetrackerapp.domain;

import javafx.scene.control.Button;


public class Finance {
    
    private String id;
    private double price;
    private String event;
    private String date;
    private String user;
    private transient String formattedPrice;
    private transient Button removeButton;
    private transient Button modifyButton;
    
    public Finance(String id, double price, String event, String date, String user) {
        this.id = id;
        this.price = price;
        this.event = event;
        this.date = date;
        this.user = user;
        this.formattedPrice = getFormattedPrice();
        this.modifyButton = new Button("Modify event");
        this.removeButton = new Button("Remove event");
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
    
    public Button getRemoveButton() {
        return removeButton;
    }
    
    public Button getModifyButton() {
        return modifyButton;
    }
    
 /**
 * returns objects price formatted
 * @see financetrackerapp.domain.DaoService#formatPrice(java.lang.Double) 
 */
    public String getFormattedPrice() {
        formattedPrice = DaoService.formatPrice(price);
        return DaoService.formatPrice(price);
    }

    @Override
    public String toString() {
        return "Finance{" + "date=" + date + ", event=" + event + ", price=" + price + ", id=" + id + ", user=" + user + '}';
    }
    
}
