package smart.pro.pattasukadai;

import java.io.Serializable;
import java.util.ArrayList;

import smart.pro.pattasukadai.invoice.Particularbean;

public class StoreMainbean implements Serializable {
    String id;
    String storeid;
    String storename;
    String total;
    String quantity;
    String price;

    public StoreMainbean() {
    }

    public StoreMainbean(String storeid, String storename, String total, String quantity, String price) {
        this.storeid = storeid;
        this.storename = storename;
        this.total = total;
        this.quantity = quantity;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStoreid() {
        return storeid;
    }

    public void setStoreid(String storeid) {
        this.storeid = storeid;
    }

    public String getStorename() {
        return storename;
    }

    public void setStorename(String storename) {
        this.storename = storename;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}