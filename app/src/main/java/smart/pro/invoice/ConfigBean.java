package smart.pro.invoice;

import android.graphics.Bitmap;
import android.graphics.Rect;

import com.itextpdf.text.BaseColor;

import java.io.Serializable;

public class ConfigBean implements Serializable {

    private static ConfigBean _instance;

    public String brandName;
    public String colorPrimary;
    public String pdfLogo;
    public String poweredByLogo;
    public String ownerSign;
    public String greenBase;
    public String greenLightBase;
    public String headerGreenBase;
    public String whatsapp;
    public String whatsappAnc;
    public String telegram;
    public String telegramChunk;
    public String mail;
    public String website;
    public String websiteAnc;
    public String logoRect;
    public String hsn;
    public Bitmap ownersignBit;
    public Bitmap poweredByLogoBit;
    public Bitmap pdfLogoBit;

    public ConfigBean(String brandName, String colorPrimary, String pdfLogo, String poweredByLogo, String ownerSign, String greenBase, String greenLightBase, String headerGreenBase, String whatsapp, String whatsappAnc, String telegram, String telegramChunk, String mail, String website, String websiteAnc, String logoRect) {
        this.brandName = brandName;
        this.colorPrimary = colorPrimary;
        this.pdfLogo = pdfLogo;
        this.poweredByLogo = poweredByLogo;
        this.ownerSign = ownerSign;
        this.greenBase = greenBase;
        this.greenLightBase = greenLightBase;
        this.headerGreenBase = headerGreenBase;
        this.whatsapp = whatsapp;
        this.whatsappAnc = whatsappAnc;
        this.telegram = telegram;
        this.telegramChunk = telegramChunk;
        this.mail = mail;
        this.website = website;
        this.websiteAnc = websiteAnc;
        this.logoRect = logoRect;
    }

    public void setPdfLogo(String pdfLogo) {
        this.pdfLogo = pdfLogo;
    }

    public void setPoweredByLogo(String poweredByLogo) {
        this.poweredByLogo = poweredByLogo;
    }

    public void setOwnerSign(String ownerSign) {
        this.ownerSign = ownerSign;
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


    public String getPdfLogo() {
        return pdfLogo;
    }

    public String getPoweredByLogo() {
        return poweredByLogo;
    }

    public String getOwnerSign() {
        return ownerSign;
    }

    public String getGreenBase() {
        return greenBase;
    }

    public void setGreenBase(String greenBase) {
        this.greenBase = greenBase;
    }

    public String getGreenLightBase() {
        return greenLightBase;
    }

    public void setGreenLightBase(String greenLightBase) {
        this.greenLightBase = greenLightBase;
    }

    public String getHeaderGreenBase() {
        return headerGreenBase;
    }

    public void setHeaderGreenBase(String headerGreenBase) {
        this.headerGreenBase = headerGreenBase;
    }

    public String getLogoRect() {
        return logoRect;
    }

    public void setLogoRect(String logoRect) {
        this.logoRect = logoRect;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
    }

    public String getWhatsappAnc() {
        return whatsappAnc;
    }

    public void setWhatsappAnc(String whatsappAnc) {
        this.whatsappAnc = whatsappAnc;
    }

    public String getTelegram() {
        return telegram;
    }

    public void setTelegram(String telegram) {
        this.telegram = telegram;
    }

    public String getTelegramChunk() {
        return telegramChunk;
    }

    public void setTelegramChunk(String telegramChunk) {
        this.telegramChunk = telegramChunk;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getWebsiteAnc() {
        return websiteAnc;
    }

    public void setWebsiteAnc(String websiteAnc) {
        this.websiteAnc = websiteAnc;
    }

    public ConfigBean() {
    }

    public Bitmap getOwnersignBit() {
        return ownersignBit;
    }

    public void setOwnersignBit(Bitmap ownersignBit) {
        this.ownersignBit = ownersignBit;
    }

    public Bitmap getPoweredByLogoBit() {
        return poweredByLogoBit;
    }

    public void setPoweredByLogoBit(Bitmap poweredByLogoBit) {
        this.poweredByLogoBit = poweredByLogoBit;
    }

    public Bitmap getPdfLogoBit() {
        return pdfLogoBit;
    }

    public void setPdfLogoBit(Bitmap pdfLogoBit) {
        this.pdfLogoBit = pdfLogoBit;
    }

    public String getHsn() {
        return hsn;
    }

    public void setHsn(String hsn) {
        this.hsn = hsn;
    }

    public static void set_instance(ConfigBean _instanceV) {
        _instance = _instanceV;
    }

    public static ConfigBean getInstance() {
        if (_instance == null) {
            _instance = new ConfigBean();
        }
        return _instance;
    }
}
