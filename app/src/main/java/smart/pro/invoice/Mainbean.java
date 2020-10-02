package smart.pro.invoice;

import java.io.Serializable;
import java.util.ArrayList;

import smart.pro.invoice.invoice.Particularbean;

public class Mainbean implements Serializable {

    public static final String TABLE_NAME = "genius2";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_SELLERNAME = "sellername";
    public static final String COLUMN_SELLERADDRESS = "selleraddress";
    public static final String COLUMN_SELLERPHONE = "sellerphone";
    public static final String COLUMN_SELLERGSTNO = "sellergstNo";
    public static final String COLUMN_SELLERBILLNO = "sellerbillNo";
    public static final String COLUMN_CUSTOMER_ID = "custid";
    public static final String COLUMN_BUYERNAME = "buyername";
    public static final String COLUMN_BUYERADDRESS = "buyeraddress";
    public static final String COLUMN_BUYERPHONE = "buyerphone";
    public static final String COLUMN_BUYERGSTNO = "buyergstNo";
    public static final String COLUMN_CGST = "cgst";
    public static final String COLUMN_SGST = "sgst";
    public static final String COLUMN_IGST = "igst";
    public static final String COLUMN_BANKNAME = "bankname";
    public static final String COLUMN_ACCOUNTNO = "accountNo";
    public static final String COLUMN_IFCNO = "ifcno";
    public static final String COLUMN_PREVIOUS = "previous";
    public static final String COLUMN_PAKAGECOST = "pakagecost";
    public static final String COLUMN_BILLMODE = "billmode";
    public static final String COLUMN_HOLDER_NAME = "holdername";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_PARTICULAR = "particular";
    public static final String COLUMN_INCLUDE_GST = "includegst";


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_SELLERNAME + " sellername,"
                    + COLUMN_SELLERADDRESS + " selleraddress,"
                    + COLUMN_SELLERPHONE + " sellerphone,"
                    + COLUMN_SELLERGSTNO + " sellergstNo,"
                    + COLUMN_SELLERBILLNO + " sellerbillNo,"
                    + COLUMN_BUYERNAME + " buyername,"
                    + COLUMN_BUYERADDRESS + " buyeraddress,"
                    + COLUMN_BUYERPHONE + " buyerphone,"
                    + COLUMN_BUYERGSTNO + " buyergstNo,"
                    + COLUMN_CGST + " cgst,"
                    + COLUMN_SGST + " sgst,"
                    + COLUMN_IGST + " igst,"
                    + COLUMN_BANKNAME + " bankname,"
                    + COLUMN_ACCOUNTNO + " accountNo,"
                    + COLUMN_IFCNO + " ifcno,"
                    + COLUMN_PREVIOUS + " previous,"
                    + COLUMN_PAKAGECOST + " pakagecost,"
                    + COLUMN_PARTICULAR + " particular,"
                    + COLUMN_BILLMODE + " billmode,"
                    + COLUMN_HOLDER_NAME + " holdername,"
                    + COLUMN_CUSTOMER_ID + " customerid,"
                    + COLUMN_INCLUDE_GST + " includegst,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";
    public  int id;
    public  String sellername;
    public  String selleraddress;
    public  String sellerphone;
    public  String sellergstNo;
    public  String sellerbillNo;
    public  String buyername;
    public  String buyeraddress;
    public  String buyerphone;
    public  String buyergstNo;
    public  String cgst;
    public  String sgst;
    public  String igst;
    public  String bankname;
    public  String accountNo;
    public  String ifcno;
    public  String previous;
    public  String pakagecost;
    public  String billmode;
    public  String holdername;
    public  String customerid;
    public  String timestamp;
    public  String includegst;
    public  ArrayList<Particularbean> particularbeans;

    public Mainbean() {
    }

    public Mainbean(String sellername, String selleraddress, String sellerphone, String sellergstNo,
                    String sellerbillNo, String buyername, String buyeraddress,
                    String buyerphone, String buyergstNo, String cgst, String sgst,
                    String igst, String bankname, String accountNo, String ifcno,
                    String previous, String pakagecost, String billmode, String holdername,
                    String customerid,String includegst,ArrayList<Particularbean> particularbeans) {
        this.sellername = sellername;
        this.selleraddress = selleraddress;
        this.sellerphone = sellerphone;
        this.sellergstNo = sellergstNo;
        this.sellerbillNo = sellerbillNo;
        this.buyername = buyername;
        this.buyeraddress = buyeraddress;
        this.buyerphone = buyerphone;
        this.buyergstNo = buyergstNo;
        this.cgst = cgst;
        this.sgst = sgst;
        this.igst = igst;
        this.bankname = bankname;
        this.accountNo = accountNo;
        this.ifcno = ifcno;
        this.previous = previous;
        this.pakagecost = pakagecost;
        this.billmode = billmode;
        this.holdername = holdername;
        this.customerid = customerid;
        this.includegst = includegst;
        this.particularbeans = particularbeans;
    }

    public Mainbean(int id, String sellername, String selleraddress, String sellerphone,
                    String sellergstNo, String sellerbillNo, String buyername, String buyeraddress,
                    String buyerphone, String buyergstNo, String cgst, String sgst, String igst,
                    String bankname, String accountNo, String ifcno, String previous,
                    String pakagecost, String billmode, String timestamp, String holdername,
                    String customerid,
                    String includegst,
                    ArrayList<Particularbean> particularbeans) {
        this.id = id;
        this.sellername = sellername;
        this.selleraddress = selleraddress;
        this.sellerphone = sellerphone;
        this.sellergstNo = sellergstNo;
        this.sellerbillNo = sellerbillNo;
        this.buyername = buyername;
        this.buyeraddress = buyeraddress;
        this.buyerphone = buyerphone;
        this.buyergstNo = buyergstNo;
        this.cgst = cgst;
        this.sgst = sgst;
        this.igst = igst;
        this.bankname = bankname;
        this.accountNo = accountNo;
        this.ifcno = ifcno;
        this.previous = previous;
        this.pakagecost = pakagecost;
        this.billmode = billmode;
        this.timestamp = timestamp;
        this.holdername = holdername;
        this.includegst = includegst;
        this.customerid = customerid;
        this.particularbeans = particularbeans;
    }

    public static String getTableName() {
        return TABLE_NAME;
    }

    public static String getColumnId() {
        return COLUMN_ID;
    }

    public static String getColumnSellername() {
        return COLUMN_SELLERNAME;
    }

    public static String getColumnSelleraddress() {
        return COLUMN_SELLERADDRESS;
    }

    public static String getColumnSellerphone() {
        return COLUMN_SELLERPHONE;
    }

    public static String getColumnSellergstno() {
        return COLUMN_SELLERGSTNO;
    }

    public static String getColumnSellerbillno() {
        return COLUMN_SELLERBILLNO;
    }

    public static String getColumnBuyername() {
        return COLUMN_BUYERNAME;
    }

    public static String getColumnBuyeraddress() {
        return COLUMN_BUYERADDRESS;
    }

    public static String getColumnBuyerphone() {
        return COLUMN_BUYERPHONE;
    }

    public static String getColumnBuyergstno() {
        return COLUMN_BUYERGSTNO;
    }

    public static String getColumnCgst() {
        return COLUMN_CGST;
    }

    public static String getColumnSgst() {
        return COLUMN_SGST;
    }

    public static String getColumnIgst() {
        return COLUMN_IGST;
    }

    public static String getColumnBankname() {
        return COLUMN_BANKNAME;
    }

    public static String getColumnAccountno() {
        return COLUMN_ACCOUNTNO;
    }

    public static String getColumnIfcno() {
        return COLUMN_IFCNO;
    }

    public static String getColumnPrevious() {
        return COLUMN_PREVIOUS;
    }

    public static String getColumnPakagecost() {
        return COLUMN_PAKAGECOST;
    }

    public static String getColumnTimestamp() {
        return COLUMN_TIMESTAMP;
    }

    public static String getColumnParticular() {
        return COLUMN_PARTICULAR;
    }

    public static String getCreateTable() {
        return CREATE_TABLE;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSellername() {
        return sellername;
    }

    public void setSellername(String sellername) {
        this.sellername = sellername;
    }

    public String getSelleraddress() {
        return selleraddress;
    }

    public void setSelleraddress(String selleraddress) {
        this.selleraddress = selleraddress;
    }

    public String getSellerphone() {
        return sellerphone;
    }

    public void setSellerphone(String sellerphone) {
        this.sellerphone = sellerphone;
    }

    public String getSellergstNo() {
        return sellergstNo;
    }

    public void setSellergstNo(String sellergstNo) {
        this.sellergstNo = sellergstNo;
    }

    public String getSellerbillNo() {
        return sellerbillNo;
    }

    public void setSellerbillNo(String sellerbillNo) {
        this.sellerbillNo = sellerbillNo;
    }

    public String getBuyername() {
        return buyername;
    }

    public void setBuyername(String buyername) {
        this.buyername = buyername;
    }

    public String getBuyeraddress() {
        return buyeraddress;
    }

    public void setBuyeraddress(String buyeraddress) {
        this.buyeraddress = buyeraddress;
    }

    public String getBuyerphone() {
        return buyerphone;
    }

    public void setBuyerphone(String buyerphone) {
        this.buyerphone = buyerphone;
    }

    public String getBuyergstNo() {
        return buyergstNo;
    }

    public void setBuyergstNo(String buyergstNo) {
        this.buyergstNo = buyergstNo;
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

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getIfcno() {
        return ifcno;
    }

    public void setIfcno(String ifcno) {
        this.ifcno = ifcno;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public String getPakagecost() {
        return pakagecost;
    }

    public void setPakagecost(String pakagecost) {
        this.pakagecost = pakagecost;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public ArrayList<Particularbean> getParticularbeans() {
        return particularbeans;
    }

    public void setParticularbeans(ArrayList<Particularbean> particularbeans) {
        this.particularbeans = particularbeans;
    }

    public String getBillmode() {
        return billmode;
    }

    public void setBillmode(String billmode) {
        this.billmode = billmode;
    }

    public String getHoldername() {
        return holdername;
    }

    public void setHoldername(String holdername) {
        this.holdername = holdername;
    }

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

    public String getIncludegst() {
        return includegst;
    }

    public void setIncludegst(String includegst) {
        this.includegst = includegst;
    }
}