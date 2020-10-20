package smart.pro.pattasukadai;

import java.io.Serializable;

public class UserBean implements Serializable {
    String id;
    String brandName;
    String colorPrimary;
    String mail;
    String telegram;
    String website;
    String whatsapp;
    String applogo;
    String usersign;

    public UserBean(String brandName, String colorPrimary, String mail, String telegram, String website, String whatsapp, String applogo, String usersign) {
        this.brandName = brandName;
        this.colorPrimary = colorPrimary;
        this.mail = mail;
        this.telegram = telegram;
        this.website = website;
        this.whatsapp = whatsapp;
        this.applogo = applogo;
        this.usersign = usersign;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getColorPrimary() {
        return colorPrimary;
    }

    public void setColorPrimary(String colorPrimary) {
        this.colorPrimary = colorPrimary;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getTelegram() {
        return telegram;
    }

    public void setTelegram(String telegram) {
        this.telegram = telegram;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
    }

    public String getApplogo() {
        return applogo;
    }

    public void setApplogo(String applogo) {
        this.applogo = applogo;
    }

    public String getUsersign() {
        return usersign;
    }

    public void setUsersign(String usersign) {
        this.usersign = usersign;
    }
}