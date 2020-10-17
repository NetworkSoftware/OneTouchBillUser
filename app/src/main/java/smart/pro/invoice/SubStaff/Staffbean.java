package smart.pro.invoice.SubStaff;

import java.io.Serializable;
import java.util.ArrayList;

import smart.pro.invoice.invoice.Particularbean;

public class Staffbean implements Serializable {
    String id;
    String staff_name;
    String staff_phone;

    public Staffbean() {
    }

    public Staffbean(String staff_name, String staff_phone) {
        this.staff_name = staff_name;
        this.staff_phone = staff_phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStaff_name() {
        return staff_name;
    }

    public void setStaff_name(String staff_name) {
        this.staff_name = staff_name;
    }

    public String getStaff_phone() {
        return staff_phone;
    }

    public void setStaff_phone(String staff_phone) {
        this.staff_phone = staff_phone;
    }
}