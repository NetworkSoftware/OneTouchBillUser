package smart.pro.pattasukadai.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.format.DateFormat;

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

import smart.pro.pattasukadai.ConfigBean;
import smart.pro.pattasukadai.Mainbean;
import smart.pro.pattasukadai.PreferenceBean;
import smart.pro.pattasukadai.StoreMainbean;
import smart.pro.pattasukadai.invoice.Particularbean;


public class PatasuPdfConfig {

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

    private static BaseColor greenBase = new BaseColor(0, 0, 255);
    private static BaseColor whiteBase = new BaseColor(255, 255, 255);
    private static BaseColor greenLightBase = new BaseColor(142, 204, 74);
    private static BaseColor gray = new BaseColor(237, 237, 237);

    private static Font titleFont = new Font(urName, 8, Font.UNDERLINE | Font.BOLD, greenBase);
    private static Font nameFont = new Font(urName, 45, Font.BOLD, greenBase);
    private static Font catFont = new Font(urName, 25, Font.BOLD, greenBase);
    private static Font catFontWhite = new Font(urName, 6, Font.BOLD, greenBase);
    private static Font catNormalFont = new Font(urName, 25, Font.NORMAL, greenBase);
    private static Font subFont = new Font(urName, 16, Font.NORMAL, greenBase);
    private static Font invoiceFont = new Font(urName, 0, Font.BOLD, greenBase);

    public static void addMetaData(Document document) {
        document.addTitle("My first PDF");
        document.addSubject("Using iText");
        document.addKeywords("Java, PDF, iText");
        document.addAuthor("Lars Vogel");
        document.addCreator("Lars Vogel");
    }

    public static void addContent(Document document, Mainbean mainbean, Context context) throws Exception {

        //invoiceFont = new Font(urName, 20, Font.BOLD, greenBase);


        PdfPTable table1 = new PdfPTable(1);
        table1.setWidthPercentage(100);
        table1.setWidths(new int[]{1});

        table1.addCell(createTextCellCenter("Sri Vinayaka\nSivakasi Patasu Kadai", nameFont, true));
        table1.addCell(createTextCellCenter(mainbean.sellername + "\n" + mainbean.selleraddress + "\nPh:" + mainbean.sellerphone, catFont, true));

        table1.setSplitLate(false);
        document.add(table1);

        PdfPTable table2 = new PdfPTable(1);
        table2.setWidthPercentage(100);
        table2.setWidths(new int[]{1});

        table2.addCell(createTextLeft("Customer Name: " + mainbean.buyername, catNormalFont, false));
        table2.addCell(createTextLeft("Mobile No: " + mainbean.buyerphone, catNormalFont, false));
        String date = "";
        if (mainbean.getTimestamp() != null && mainbean.getTimestamp().length() > 0) {
            date = mainbean.getTimestamp().split(" ")[0];
        } else {
            Date d = new Date();
            CharSequence s = DateFormat.format("dd-MM-yyyy", d.getTime());
            date = s.toString();
        }
        table2.addCell(createTextLeft("Date: " + date.split(" ")[0], catNormalFont, true));
        table2.setSplitLate(false);
        document.add(table2);

        PdfPTable table3 = new PdfPTable(6);
        table3.setWidthPercentage(100);
        table3.setWidths(new float[]{0.5f, 0.5f, 2f, 1, 1, 1});
        table3.addCell(createTextLeft("SNo", catFont, false));
        table3.addCell(createTextLeft("PNo", catFont, false));
        table3.addCell(createTextLeft("Particulars", catFont, false));
        table3.addCell(createTextLeft("Qty", catFont, false));
        table3.addCell(createTextLeft("Rate", catFont, false));
        table3.addCell(createTextLeft("Amount", catFont, false));
        double grantTotal = 0;

        ArrayList<Particularbean> particularbeans = mainbean.getParticularbeans();
        for (int i = 0; i < particularbeans.size(); i++) {
            Particularbean fields = particularbeans.get(i);
            int quant = Integer.parseInt(fields.getQuantity());
            int perquant = Integer.parseInt(fields.getPerquantity());
            table3.addCell(createTextLeft(i + 1 + "", catNormalFont, false));
            table3.addCell(createTextLeft(fields.getId(), catNormalFont, false));
            table3.addCell(createTextLeft(fields.getParticular(), catNormalFont, false));
            float perQuanPri = Float.parseFloat(fields.getPerquantity());
            table3.addCell(createTextLeft(fields.getQuantity(), catNormalFont, false));
            table3.addCell(createTextLeft(round(perQuanPri, 2), catNormalFont, false));
            float quan = Float.parseFloat(fields.getQuantity());
            double total = quan * perQuanPri;
            grantTotal = grantTotal + total;

            table3.addCell(createTextLeft(round(total, 2), catFont, false));

        }
        table3.setSplitLate(false);
        document.add(table3);

        PdfPTable table40 = new PdfPTable(1);
        table40.setWidthPercentage(100);
        table40.setWidths(new float[]{1});
        table40.addCell(createTextLeft("\n", catNormalFont, false));
        table40.setSplitLate(false);
        document.add(table40);
        PdfPTable table4 = new PdfPTable(6);
        table4.setWidthPercentage(100);
        table4.setWidths(new float[]{0.5f, 0.5f, 2f, 1, 1, 1});
        table4.addCell(createTextLeft("", catNormalFont, false));
        table4.addCell(createTextLeft("", catNormalFont, false));
        table4.addCell(createTextLeft(AppConfig.convertToIndianCurrency(grantTotal + ""), catNormalFont, false));
        table4.addCell(createTextLeft("", catNormalFont, false));
        table4.addCell(createTextLeft("", catNormalFont, false));
        table4.addCell(createTextLeft(round(grantTotal, 2) + "", catFont, false));
        table4.setSplitLate(false);
        document.add(table4);

        PdfPTable table5 = new PdfPTable(1);
        table5.setWidthPercentage(100);
        table5.setWidths(new float[]{1});
        table5.addCell(createTextcenter("\n", catNormalFont, true));
        table5.addCell(createTextcenter("E & OE", subFont, false));
        table5.addCell(createTextcenter("FOR EXCHANGE POLICY PLEASE VISIT WWW.PATTASUKADAI.COM/FAQ", subFont, true));
        table5.addCell(createTextcenter("Tax Invoice/Bill of Supply -Sale", subFont, false));
        table5.addCell(createTextcenter("Bill No: " + AppConfig.intToString(Integer.parseInt(mainbean.getDbid()), 5)+ " Date : " + date, subFont, true));
        table5.addCell(createTextcenter("Its is a computer generated invoice generated original / customer copy", subFont, false));
        table5.addCell(createTextcenter("***Thank you for you purchase, visit again***", subFont, false));
        table5.setSplitLate(false);
        document.add(table5);

    }

    public static PdfPCell createTextCellTable(String text, Font font) throws DocumentException, IOException {
        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph(text, font);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }


    public static PdfPCell createTable(PdfPTable pTable, int padding, BaseColor baseColor, boolean isBorder) throws DocumentException, IOException {

        PdfPCell cell = new PdfPCell();
        cell.addElement(pTable);
        cell.setPaddingTop(0);
        cell.setPaddingBottom(0);
        cell.setPaddingLeft(padding);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.NO_BORDER);
        if (isBorder) {
            cell.setCellEvent(new DottedCell(PdfPCell.BOX));
        }
        cell.setBackgroundColor(baseColor);
        return cell;
    }


    public static PdfPCell createTextRight(String text, Font font) throws DocumentException, IOException {
        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph(text, font);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        cell.setPaddingLeft(5);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    public static PdfPCell createTextLeft(String text, Font font, boolean isBorder) throws DocumentException, IOException {
        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph(text, font);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        cell.setVerticalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(Rectangle.NO_BORDER);
        if (isBorder) {
            cell.setPaddingBottom(10);
            cell.setCellEvent(new DottedCell(Rectangle.BOTTOM));
        }
        return cell;
    }

    public static PdfPCell createTextcenter(String text, Font font, boolean isBorder) throws DocumentException, IOException {
        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph(text, font);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.NO_BORDER);
        if (isBorder) {
            cell.setPaddingBottom(10);
            cell.setCellEvent(new DottedCell(Rectangle.BOTTOM));
        }
        return cell;
    }

    public static PdfPCell createTextImage(Image image) throws DocumentException, IOException {
        PdfPCell cell = new PdfPCell();
        cell.addElement(image);
        cell.setUseAscender(true);
        cell.setPaddingTop(10);
        cell.setVerticalAlignment(Element.ALIGN_RIGHT);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    public static PdfPCell createTextCellLeftRight(String text, Font font) throws DocumentException, IOException {
        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph(text, font);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        cell.setUseAscender(true);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPaddingTop(15);
        cell.setPaddingBottom(15);
        cell.setPaddingLeft(5);
        cell.setPaddingRight(15);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
        return cell;
    }

    public static PdfPCell createTextCellleftrightRight(String text, Font font) throws DocumentException, IOException {
        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph(text, font);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        cell.setUseAscender(true);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPaddingTop(7);
        cell.setPaddingBottom(5);
        cell.setPaddingLeft(5);
        cell.setPaddingRight(7);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
        return cell;
    }

    public static PdfPCell createTextCelllefttotal(String text, Font font) throws DocumentException, IOException {
        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph(text, font);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        cell.setUseAscender(true);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPaddingTop(7);
        cell.setPaddingBottom(5);
        cell.setPaddingLeft(5);
        cell.setPaddingRight(7);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
        return cell;
    }

    public static PdfPCell createTextCellBootomparticlure(String text, Font font) throws DocumentException, IOException {
        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph(text, font);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        cell.setUseAscender(true);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPaddingTop(15);
        cell.setPaddingBottom(15);
        cell.setPaddingLeft(5);
        cell.setPaddingRight(15);
        cell.setBorder(Rectangle.LEFT | Rectangle.TOP | Rectangle.BOTTOM);
        return cell;
    }

    public static PdfPCell createTextCellBootompakage(String text, Font font) throws DocumentException, IOException {
        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph(text, font);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        cell.setUseAscender(true);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPaddingTop(7);
        cell.setPaddingBottom(7);
        cell.setPaddingLeft(5);
        cell.setPaddingRight(7);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
        return cell;
    }

    public static PdfPCell createTextCellToplessRight(String text, Font font) throws DocumentException, IOException {
        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph(text, font);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        cell.setUseAscender(true);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPaddingTop(7);
        cell.setPaddingBottom(5);
        cell.setPaddingLeft(5);
        cell.setPaddingRight(7);
        cell.setBorder(Rectangle.BOX);
        return cell;
    }

    public static PdfPCell createTextCellprevoius(String text, Font font) throws DocumentException, IOException {
        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph(text, font);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        cell.setUseAscender(true);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPaddingTop(7);
        cell.setPaddingBottom(5);
        cell.setPaddingLeft(5);
        cell.setPaddingRight(7);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
        return cell;
    }

    public static PdfPCell createTextCellBottomlessRight(String text, Font font) throws DocumentException, IOException {
        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph(text, font);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        cell.setUseAscender(true);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPaddingTop(15);
        cell.setPaddingBottom(15);
        cell.setPaddingLeft(5);
        cell.setPaddingRight(15);
        cell.setBorder(Rectangle.LEFT | Rectangle.TOP);
        return cell;
    }

    public static PdfPCell createTextCellTopless(String text, Font font) throws DocumentException, IOException {
        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph(text, font);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        cell.setUseAscender(true);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPaddingTop(15);
        cell.setPaddingBottom(15);
        cell.setPaddingLeft(5);
        cell.setPaddingRight(15);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
        return cell;
    }

    public static PdfPCell createTextCellBottom(String text, Font font) throws DocumentException, IOException {
        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph(text, font);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        cell.setUseAscender(true);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPaddingTop(15);
        cell.setPaddingBottom(15);
        cell.setPaddingLeft(5);
        cell.setPaddingRight(15);
        cell.setBorder(Rectangle.BOTTOM);
        return cell;
    }

    public static PdfPCell createTextCellBottomlessHigh(String text, Font font) throws DocumentException, IOException {
        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph(text, font);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        cell.setUseAscender(true);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPaddingTop(15);
        cell.setPaddingBottom(150);
        cell.setPaddingLeft(5);
        cell.setPaddingRight(15);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.TOP);
        return cell;
    }

    public static PdfPCell createTextCellBottomless(String text, Font font) throws DocumentException, IOException {
        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph(text, font);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        cell.setUseAscender(true);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPaddingTop(15);
        cell.setPaddingBottom(15);
        cell.setPaddingLeft(5);
        cell.setPaddingRight(15);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.TOP);
        return cell;
    }

    public static PdfPCell createTextCellBorder(String text, Font font) throws DocumentException, IOException {
        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph(text, font);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        cell.setUseAscender(true);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPaddingTop(10);
        cell.setPaddingBottom(10);
        cell.setPaddingLeft(5);
        cell.setPaddingRight(10);
        cell.setBorder(Rectangle.BOX);
        return cell;
    }

    public static PdfPCell createTextCellBorderLeft(String text, Font font) throws DocumentException, IOException {
        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph(text, font);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        cell.setUseAscender(true);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPaddingTop(7);
        cell.setPaddingBottom(7);
        cell.setPaddingLeft(5);
        cell.setPaddingRight(7);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
        return cell;
    }

    public static PdfPCell createTextCellBorderbootom(String text, Font font) throws DocumentException, IOException {
        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph(text, font);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        cell.setUseAscender(true);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPaddingTop(7);
        cell.setPaddingBottom(55);
        cell.setPaddingLeft(5);
        cell.setPaddingRight(7);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
        return cell;
    }

    public static PdfPCell createTextCellBorderbootomparticular(String text, Font font) throws DocumentException, IOException {
        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph(text, font);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        cell.setUseAscender(true);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPaddingTop(7);
        cell.setPaddingBottom(55);
        cell.setPaddingLeft(5);
        cell.setPaddingRight(7);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
        return cell;
    }

    public static PdfPCell createTextCellBorderbootomtotal(String text, Font font) throws DocumentException, IOException {
        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph(text, font);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        cell.setUseAscender(true);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPaddingTop(5);
        cell.setPaddingBottom(5);
        cell.setPaddingLeft(5);
        cell.setPaddingRight(5);
        cell.setBorder(Rectangle.TOP | Rectangle.BOTTOM);
        return cell;
    }

    public static PdfPCell createTextCellcheck(String text, Font font) throws DocumentException, IOException {
        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph(text, font);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        cell.setUseAscender(true);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPaddingTop(7);
        cell.setPaddingBottom(5);
        cell.setPaddingLeft(5);
        cell.setPaddingRight(7);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
        return cell;
    }

    public static PdfPCell createTextCellBorderHigh(String text, Font font) throws DocumentException, IOException {
        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph(text, font);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        cell.setUseAscender(true);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPaddingTop(7);
        cell.setPaddingBottom(55);
        cell.setPaddingLeft(5);
        cell.setPaddingRight(7);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
        return cell;
    }

    public static PdfPCell createTextCellBorderRight(String text, Font font) throws DocumentException, IOException {
        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph(text, font);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        cell.setUseAscender(true);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPaddingTop(15);
        cell.setPaddingBottom(15);
        cell.setPaddingLeft(5);
        cell.setPaddingRight(15);
        cell.setBorder(Rectangle.BOX);
        return cell;
    }

    public static PdfPCell createTextCellCenter(String text, Font font, boolean isBorder) throws DocumentException, IOException {
        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph(text, font);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPaddingBottom(10);
        if (isBorder) {
            cell.setCellEvent(new DottedCell(Rectangle.BOTTOM));
        }
        return cell;
    }

    public static PdfPCell createTextCellRightNoBorder(String text, Font font) throws DocumentException, IOException {
        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph(text, font);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    public static PdfPCell createTextCellRight(String text, Font font) throws DocumentException, IOException {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    public static PdfPCell border(String text, Font font) throws DocumentException, IOException {
        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph(text, font);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        cell.setBorder(Rectangle.BOX);
        return cell;
    }

    public static String round(double d, int decimalPlace) {
        return String.format("%.2f", d);
    }

    static class DottedCell implements PdfPCellEvent {
        private int border = 0;

        public DottedCell(int border) {
            this.border = border;
        }

        public void cellLayout(PdfPCell cell, Rectangle position,
                               PdfContentByte[] canvases) {
            PdfContentByte canvas = canvases[PdfPTable.LINECANVAS];
            canvas.saveState();
            canvas.setColorStroke(greenBase);
            canvas.setLineDash(1, 3, 1);
            if ((border & PdfPCell.TOP) == PdfPCell.TOP) {
                canvas.moveTo(position.getRight(), position.getTop());
                canvas.lineTo(position.getLeft(), position.getTop());
            }
            if ((border & PdfPCell.BOTTOM) == PdfPCell.BOTTOM) {
                canvas.moveTo(position.getRight(), position.getBottom());
                canvas.lineTo(position.getLeft(), position.getBottom());
            }
            if ((border & PdfPCell.RIGHT) == PdfPCell.RIGHT) {
                canvas.moveTo(position.getRight(), position.getTop());
                canvas.lineTo(position.getRight(), position.getBottom());
            }
            if ((border & PdfPCell.LEFT) == PdfPCell.LEFT) {
                canvas.moveTo(position.getLeft(), position.getTop());
                canvas.lineTo(position.getLeft(), position.getBottom());
            }
            canvas.stroke();
            canvas.restoreState();
        }
    }
}
