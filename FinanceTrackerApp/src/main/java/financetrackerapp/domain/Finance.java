
package financetrackerapp.domain;

import com.google.gson.annotations.Expose;
import java.util.Objects;


public class Finance {
    
    private String id;
    private double price;
    private String event;
    private String date;
    private String user;
    
    @Expose(serialize = false)
    private String formattedPrice;
    
    public Finance(String id, double price, String event, String date, String user) {
        this.id = id;
        this.price = price;
        this.event = event;
        this.date = date;
        this.user = user;
        this.formattedPrice = getFormattedPrice();
    }

    public String getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }

    public String getEvent() {
        return event;
    }

    public String getDate() {
        return date;
    }

    public String getUser() {
        return user;
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
        return "Finance{" + "id=" + id + ", price=" + price + ", event=" + event + ", date=" + date + ", user=" + user + ", formattedPrice=" + formattedPrice + '}';
    }
}
