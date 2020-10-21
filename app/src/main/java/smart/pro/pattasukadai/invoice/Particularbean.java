package smart.pro.pattasukadai.invoice;

import java.io.Serializable;

public class Particularbean implements Serializable {
    public String id;
    public String particular;
    public String quantity;
    public String perquantity;

    public Particularbean() {
    }

    public Particularbean(String id, String particular, String quantity, String perquantity) {
        this.id = id;
        this.particular = particular;
        this.quantity = quantity;
        this.perquantity = perquantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParticular() {
        return particular;
    }

    public void setParticular(String particular) {
        this.particular = particular;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPerquantity() {
        return perquantity;
    }

    public void setPerquantity(String perquantity) {
        this.perquantity = perquantity;
    }

}