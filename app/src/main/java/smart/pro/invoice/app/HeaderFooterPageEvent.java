package smart.pro.invoice.app;

import smart.pro.invoice.ConfigBean;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.IOException;
import java.util.ArrayList;

public class HeaderFooterPageEvent extends PdfPageEventHelper {
    Image image;
    Image image2;
    boolean isDigital;
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

    private static Font subFont = new Font(urName, 8, Font.NORMAL);
    private static Font poweredFont = new Font(urName, 6, Font.ITALIC, new BaseColor(10, 242, 255));
    private static Font PagenoFont = new Font(urName, 8, Font.NORMAL);
    private static Font PagenoFont1 = new Font(urName, 8, Font.BOLD, greenBase);

    private static Font webPrint = new Font(urName, 8, Font.BOLD, new BaseColor(66, 133, 244));
    private ConfigBean configBean;

    public HeaderFooterPageEvent(Image image1, Image image21, boolean isDigital, ConfigBean configBean) {
        image = image1;
//        image.scalePercent(30);
        image.scaleAbsolute(82,22);
        image2 = image21;
        this.isDigital = isDigital;
        image2.scalePercent(3f);
        this.configBean = configBean;
        String[] strings=configBean.getHeaderGreenBase().split("#");
        greenBase = new BaseColor(Integer.parseInt(strings[0]),Integer.parseInt(strings[1]),Integer.parseInt(strings[2]));
        PagenoFont1 = new Font(urName, 8, Font.BOLD, greenBase);
    }

    /**
     * The template with the total number of pages.
     */


    public void onStartPage(PdfWriter writer, Document document) {
//        Rectangle rect = writer.getBoxSize("art");

//        ColumnText.showTextAligned(writer.getDirectContent(),
//                Element.ALIGN_CENTER, new Phrase("Bharathiar university"), rect.getRight(), rect.getTop(), 0);
        // image.setAbsolutePosition(rect.getRight(),rect.getTop());

    }

    public void onEndPage(PdfWriter writer, Document document) {
        //    Rectangle rect = writer.getBoxSize("art");
//        ColumnText.showTextAligned(writer.getDirectContent(),
//                Element.ALIGN_CENTER, new Phrase(
//                        "THANK YOU FOR YOUR BUSINESS!",
//                        subFont), rect.getLeft(), rect.getBottom(), 0);

        Phrase phrase0 = new Phrase();
        phrase0.add("");
        Chunk chunk0 = new Chunk(configBean.whatsapp, webPrint);
        chunk0.setAnchor(configBean.whatsappAnc);
        phrase0.add(chunk0);

        Phrase phrase = new Phrase();
        phrase.add("");
        Chunk chunk = new Chunk(configBean.telegram, webPrint);
        chunk.setAnchor(configBean.telegramChunk);
        phrase.add(chunk);

        Phrase phrase1 = new Phrase();
        phrase1.add("");
        Chunk chunk1 = new Chunk(configBean.mail, webPrint);
        chunk1.setAnchor("https://mail.google.com/mail/u/0/?tab\\u003drm\\u0026ogbl#inbox?compose\\u003dnew");
        phrase1.add(chunk1);

        Phrase phrase2 = new Phrase();
        phrase2.add("");
        Chunk chunk2 = new Chunk(configBean.website, webPrint);
        chunk2.setAnchor(configBean.websiteAnc);
        phrase2.add(chunk2);

        ArrayList<Phrase> phrases = new ArrayList<>();
        if (configBean.telegram.length() > 0) {
            phrases.add(phrase);
        }
        if (configBean.whatsapp.length() > 0) {
            phrases.add(phrase0);
        }
        if (configBean.mail.length() > 0) {
            phrases.add(phrase1);
        }
        if (configBean.website.length() > 0) {
            phrases.add(phrase2);
        }
        for (int i = 0; i < phrases.size(); i++) {
            int y = 20 + (i * 15);
            ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, phrases.get(i), 30, y, 0);
        }

        ColumnText.showTextAligned(writer.getDirectContent(),
                Element.ALIGN_CENTER, new Phrase("THANK YOU FOR YOUR BUSINESS!", subFont),
                PageSize.A4.getWidth() / 2, 20, 0);
        ColumnText.showTextAligned(writer.getDirectContent(),
                Element.ALIGN_CENTER, new Phrase("powered by", poweredFont),
                PageSize.A4.getWidth() / 2 + 10, 10, 0);
        try {
            image2.setAbsolutePosition(PageSize.A4.getWidth() / 2 + 27, 10.3f);
            writer.getDirectContent().addImage(image2);
        } catch (Exception e) {

        }

        ColumnText.showTextAligned(writer.getDirectContent(),
                Element.ALIGN_RIGHT, new Phrase("For " + configBean.brandName + " ", PagenoFont1),
                PageSize.A4.getWidth() - 30, 60, 0);
        if (isDigital) {
            try {
                image.setAbsolutePosition(PageSize.A4.getWidth() - 108, 30);
                writer.getDirectContent().addImage(image);
            } catch (Exception e) {

            }
        }
        ColumnText.showTextAligned(writer.getDirectContent(),
                Element.ALIGN_RIGHT, new Phrase("Authorized Signatory ", PagenoFont),
                PageSize.A4.getWidth() - 30, 20, 0);


        PdfContentByte canvas = writer.getDirectContent();
        Rectangle rect = new Rectangle(document.getPageSize().getWidth() - 30,
                document.getPageSize().getHeight() - 40);
        rect.setBorder(Rectangle.BOX); // left, right, top, bottom border
        rect.setBorderWidth(1); // a width of 5 user units
        rect.setBorderColor(BaseColor.BLACK); // a red border
        rect.setBottom(122);
        rect.setLeft(30);
        rect.setUseVariableBorders(false); // the full width will be visible
        canvas.rectangle(rect);
//        canvas.stroke();
    }

}