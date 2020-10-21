package smart.pro.pattasukadai.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.format.DateFormat;

import smart.pro.pattasukadai.ConfigBean;
import smart.pro.pattasukadai.Mainbean;
import smart.pro.pattasukadai.PreferenceBean;
import smart.pro.pattasukadai.invoice.Particularbean;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;


public class PdfConfig {

    private static BaseFont urName;

    {
        try {
            urName = BaseFont.createFont("font/sans.TTF", "UTF-8", BaseFont.EMBEDDED);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static BaseColor greenBase = new BaseColor(14, 157, 31);
    private static BaseColor whiteBase = new BaseColor(255, 255, 255);
    private static BaseColor greenLightBase = new BaseColor(142, 204, 74);
    private static BaseColor gray = new BaseColor(237, 237, 237);

    private static Font titleFont = new Font(urName, 11, Font.UNDERLINE | Font.BOLD);
    private static Font nameFont = new Font(urName, 11, Font.BOLD);
    private static Font catFont = new Font(urName, 9, Font.BOLD);
    private static Font catFontWhite = new Font(urName, 9, Font.BOLD);
    private static Font catNormalFont = new Font(urName, 9, Font.NORMAL);
    private static Font subFont = new Font(urName, 7, Font.NORMAL);
    private static Font invoiceFont = new Font(urName, 20, Font.BOLD, greenBase);

    public static void addMetaData(Document document) {
        document.addTitle("My first PDF");
        document.addSubject("Using iText");
        document.addKeywords("Java, PDF, iText");
        document.addAuthor("Lars Vogel");
        document.addCreator("Lars Vogel");
    }

    public static void addContent(Document document, Mainbean mainbean,
                                  boolean isGst, Context context,
                                  ConfigBean configBean, PreferenceBean preferenceBean) throws Exception {

    }
}
