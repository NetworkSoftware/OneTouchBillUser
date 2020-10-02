package smart.pro.invoice.invoice;

import java.io.Serializable;

public class Particularbean implements Serializable {
    public String id;
    public String particular;
    public String quantity;
    public String perquantity;
    public String cgst;
    public String sgst;
    public String igst;
    public String total;

    public Particularbean(String particular, String quantity, String perquantity, String cgst, String sgst, String igst) {
        this.particular = particular;
        this.quantity = quantity;
        this.perquantity = perquantity;
        this.cgst = cgst;
        this.sgst = sgst;
        this.igst = igst;
    }

    public Particularbean(String particular, String quantity, String perquantity) {
        this.particular = particular;
        this.quantity = quantity;
        this.perquantity = perquantity;
    }

    public String getCgst() {
        return cgst;
    }

    public void setCgst(String cgst) {
        this.cgst = cgst;
    }

    public String getSgst() {
        return sgst;
    }

    public void setSgst(String sgst) {
        this.sgst = sgst;
    }

    public String getIgst() {
        return igst;
    }

    public void setIgst(String igst) {
        this.igst = igst;
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

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}