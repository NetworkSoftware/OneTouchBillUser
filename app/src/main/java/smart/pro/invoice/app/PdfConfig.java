package smart.pro.invoice.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.format.DateFormat;

import smart.pro.invoice.ConfigBean;
import smart.pro.invoice.Mainbean;
import smart.pro.invoice.PreferenceBean;
import smart.pro.invoice.invoice.Particularbean;

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
        String[] getGreenBaseString = configBean.getGreenBase().split("#");
        greenBase = new BaseColor(Integer.parseInt(getGreenBaseString[0]), Integer.parseInt(getGreenBaseString[1]), Integer.parseInt(getGreenBaseString[2]));

        String[] getGreenLightBaseString = configBean.getGreenLightBase().split("#");
        greenLightBase = new BaseColor(Integer.parseInt(getGreenLightBaseString[0]), Integer.parseInt(getGreenLightBaseString[1]), Integer.parseInt(getGreenLightBaseString[2]));

        invoiceFont = new Font(urName, 20, Font.BOLD, greenBase);

        PdfPTable tableAll = new PdfPTable(1);
        tableAll.setWidthPercentage(100);
        tableAll.setWidths(new int[]{1});

        PdfPTable table01 = new PdfPTable(3);
        table01.setWidthPercentage(100);
        table01.setWidths(new int[]{1, 1, 1});
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        configBean.getPdfLogoBit().compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        Image img = Image.getInstance(byteArray);
        String[] getLogoRect = configBean.getLogoRect().split("#");
       img.scaleAbsolute(Integer.parseInt(getLogoRect[0]), Integer.parseInt(getLogoRect[1]));
        //img.scaleAbsolute(60, 60);
        table01.addCell(createTextImage(img));
        String billMode = (((mainbean.getBillmode() != null && mainbean.getBillmode().length() > 0) ? mainbean.getBillmode() : "INVOICE"));

        PdfPTable table000 = new PdfPTable(1);
        table000.setWidthPercentage(100);
        table000.addCell(createTextRight(billMode, invoiceFont));
        if (mainbean.getTimestamp() != null && mainbean.getTimestamp().length() > 0) {
            table000.addCell(createTextRight("Date:" + mainbean.getTimestamp().split(" ")[0], catNormalFont));
        } else {
            Date d = new Date();
            CharSequence s = DateFormat.format("dd-MM-yyyy", d.getTime());
            table000.addCell(createTextRight("Date:" + s.toString(), catNormalFont));
        }
        table01.addCell(createTextRight("", catNormalFont));
        table01.addCell(createTable(table000, 0, whiteBase, false));

        table01.setKeepTogether(true);
        //document.add(table01);
        tableAll.addCell(createTable(table01, 0, whiteBase, false));

      /*  PdfPTable table00 = new PdfPTable(4);
        table00.setWidthPercentage(100);
        table00.setWidths(new int[]{1, 1, 1, 1});

        table00.addCell(createTextCell("", catFont));
        table00.addCell(createTextCell("", catFont));
        table00.addCell(createTextCellRightNoBorder("Bill No ", catNormalFont));
        table00.addCell(createTextCell(" " + mainbean.getSellerbillNo(), catFont));
        table00.addCell(createTextCell("", catFont));
        table00.addCell(createTextCell("", catFont));
        table00.addCell(createTextCellRightNoBorder("Date ", catNormalFont));
        Date d = new Date();
        CharSequence s = DateFormat.format("dd-MM-yyyy", d.getTime());
        table00.addCell(createTextCell(" " + s.toString(), catFont));
        table00.setKeepTogether(true);
        document.add(table00);*/

        PdfPTable table001 = new PdfPTable(1);
        table001.setWidthPercentage(100);
        table001.addCell(createTextCellTable(mainbean.getSellername(), nameFont));
        table001.addCell(createTextCellTable(mainbean.getSelleraddress(), catNormalFont));
        table001.addCell(createTextCellTable("Mob: " + mainbean.getSellerphone(), catNormalFont));
        if (isGst && mainbean.getSellergstNo() != null &&
                mainbean.getSellergstNo().length() >= 0 && !mainbean.getSellergstNo().equals("NA")) {
            table001.addCell(createTextCellTable("GST No: " + mainbean.getSellergstNo(), catNormalFont));
        }
        table001.addCell(createTextCellTable(mainbean.getHoldername() + " - " + mainbean.getBankname(), catNormalFont));
        table001.addCell(createTextCellTable(mainbean.getAccountNo() + " - " + mainbean.getIfcno(), catNormalFont));
        table001.addCell(createTextCellTable(" ", catNormalFont));

        PdfPTable table002 = new PdfPTable(1);
        table002.setWidthPercentage(100);
        table002.addCell(createTextCellTable(mainbean.getBuyername(), nameFont));
        table002.addCell(createTextCellTable(mainbean.getBuyeraddress(), catNormalFont));
        table002.addCell(createTextCellTable("Mob: " + mainbean.getBuyerphone(), catNormalFont));
        if (isGst && mainbean.getBuyergstNo() != null &&
                mainbean.getBuyergstNo().length() >= 0 && !mainbean.getBuyergstNo().equals("NA")) {
            table002.addCell(createTextCellTable("GST No: " + mainbean.getBuyergstNo(), catNormalFont));
        }
        table002.addCell(createTextCellTable("Bill No: " + AppConfig.intToString(Integer.parseInt(
                mainbean.getSellerbillNo()), 5) + " - " + "Cust ID: " + AppConfig.intToString(
                ((mainbean.getCustomerid() != null && mainbean.getCustomerid().length() > 0) ? Integer.parseInt(mainbean.getCustomerid()) : 1), 4), catNormalFont));
        table002.addCell(createTextCellTable(" ", catNormalFont));

        PdfPTable table003 = new PdfPTable(1);
        table003.setWidthPercentage(100);
        table003.addCell(createTextCellTable("HSN code for products", nameFont));
        if (configBean.getHsn() != null && configBean.getHsn().length() > 0) {
            String[] hsns = configBean.getHsn().split("#");
            for (int i = 0; i < hsns.length; i++) {
                table003.addCell(createTextCellTable(hsns[i], catNormalFont));
            }
        }
        table003.addCell(createTextCellTable(" ", catNormalFont));


        PdfPTable table1 = new PdfPTable(7);
        table1.setWidthPercentage(100);
        table1.setWidths(new float[]{0.07f, 1.4f, 0.07f, 1.4f, 0.07f, 1.4f, 0.07f});
        table1.setSpacingAfter(7);
        table1.setSpacingBefore(7);
        table1.addCell(createTextCellTable("", catNormalFont));
        table1.addCell(createTable(table001, 5, gray, true));
        table1.addCell(createTextCellTable("", catNormalFont));
        table1.addCell(createTable(table002, 5, gray, true));
        table1.addCell(createTextCellTable("", catNormalFont));
        table1.addCell(createTable(table003, 5, gray, true));
        table1.addCell(createTextCellTable("", catNormalFont));
        table1.setKeepTogether(true);
        // document.add(table1);
        tableAll.addCell(createTable(table1, 0, whiteBase, false));

        PdfPTable table2 = null;
        if (!preferenceBean.isSerial()) {
            table2 = new PdfPTable(7);
            table2.setWidthPercentage(100);
            if (isGst) {
                table2.setWidths(new float[]{2.8f, 0.7f, 0.7f, 0.7f, 0.7f, 0.7f, 0.9f});
            } else {
                table2.setWidths(new float[]{4.9f, 0, 0, 0, 0.7f, 0.7f, 0.9f});
            }
        } else {
            table2 = new PdfPTable(8);
            table2.setWidthPercentage(100);
            if (isGst) {
                table2.setWidths(new float[]{0.5f, 2.3f, 0.7f, 0.7f, 0.7f, 0.7f, 0.7f, 0.9f});
            } else {
                table2.setWidths(new float[]{0.5f, 4.4f, 0, 0, 0, 0.7f, 0.7f, 0.9f});
            }
        }
        if (preferenceBean.isSerial()) {
            table2.addCell(createTextCellBorder("S. No", catFontWhite));
            table2.addCell(createTextCellBorder("PARTICULARS", catFontWhite));
        } else {
            table2.addCell(createTextCellBorder("PARTICULARS", catFontWhite));
        }
        if (isGst) {
            table2.addCell(createTextCellBorder("QTY", catFontWhite));
            table2.addCell(createTextCellBorder("PRICE", catFontWhite));
            table2.addCell(createTextCellBorder("SGST", catFontWhite));
            table2.addCell(createTextCellBorder("CGST", catFontWhite));
            table2.addCell(createTextCellBorder("IGST", catFontWhite));
        } else {
            table2.addCell(createTextCellBorder("", catFontWhite));
            table2.addCell(createTextCellBorder("", catFontWhite));
            table2.addCell(createTextCellBorder("", catFontWhite));
            table2.addCell(createTextCellBorder("QTY", catFontWhite));
            table2.addCell(createTextCellBorder("PRICE", catFontWhite));
        }
        table2.addCell(createTextCellBorder("TOTAL", catFontWhite));
        double grantTotal = 0;
        ArrayList<Particularbean> particularbeans = mainbean.getParticularbeans();
//        particularbeans.addAll(mainbean.getParticularbeans());
//        for (int i = 0; i < 100; i++) {
//            particularbeans.addAll(mainbean.getParticularbeans());
//        }
        for (int i = 0; i < particularbeans.size(); i++) {
            Particularbean particularbean = particularbeans.get(i);
            if (i == particularbeans.size() - 1) {
                if (preferenceBean.isSerial()) {
                    table2.addCell(createTextCellBorderbootom(String.valueOf(i + 1), catNormalFont));
                }
                table2.addCell(createTextCellBorderbootomparticular(particularbean.getParticular().replace(" inch", "\""), catNormalFont));
                float quan = Float.parseFloat(particularbean.getQuantity());
                float perQuanPri = Float.parseFloat(particularbean.getPerquantity());
                double total = quan * perQuanPri;
                if (isGst) {
                    double cgstVal = ((particularbean.getCgst().length() > 0 ? Float.parseFloat(particularbean.getCgst()) : 0) * total) / 100f;
                    double igstVal = ((particularbean.getIgst().length() > 0 ? Float.parseFloat(particularbean.getIgst()) : 0) * total) / 100f;
                    double sgstVal = ((particularbean.getSgst().length() > 0 ? Float.parseFloat(particularbean.getSgst()) : 0) * total) / 100f;
                    String cgstValS = particularbean.getCgst().length() > 0 ? particularbean.getCgst() : "0";
                    String igstValS = particularbean.getIgst().length() > 0 ? particularbean.getIgst() : "0";
                    String sgstValS = particularbean.getSgst().length() > 0 ? particularbean.getSgst() : "0";
                    table2.addCell(createTextCellBorderbootom(particularbean.getQuantity(), catNormalFont));
                    table2.addCell(createTextCellBorderbootom(particularbean.getPerquantity(), catNormalFont));
                    table2.addCell(createTextCellBorderbootom(round(sgstVal, 2) + "\n@" + sgstValS + "%", catNormalFont));
                    table2.addCell(createTextCellBorderbootom(round(cgstVal, 2) + "\n@" + cgstValS + "%", catNormalFont));
                    table2.addCell(createTextCellBorderbootom(round(igstVal, 2) + "\n@" + igstValS + "%", catNormalFont));
                    total = total + cgstVal + igstVal + sgstVal;
                } else {
                    table2.addCell(createTextCellBorderbootom("", catNormalFont));
                    table2.addCell(createTextCellBorderbootom("", catNormalFont));
                    table2.addCell(createTextCellBorderbootom("", catNormalFont));
                    table2.addCell(createTextCellBorderbootom(particularbean.getQuantity(), catNormalFont));
                    table2.addCell(createTextCellBorderbootom(particularbean.getPerquantity(), catNormalFont));
                }
                table2.addCell(createTextCellBorderHigh(round(total, 2), catFont));
                grantTotal = grantTotal + total;

            } else {
                if (preferenceBean.isSerial()) {
                    table2.addCell(createTextCellBorderLeft(String.valueOf(i + 1), catNormalFont));
                }
                table2.addCell(createTextCellcheck(particularbean.getParticular().replace(" inch", "\""), catNormalFont));
                float quan = Float.parseFloat(particularbean.getQuantity());
                float perQuanPri = Float.parseFloat(particularbean.getPerquantity());
                double total = quan * perQuanPri;
                if (isGst) {
                    double cgstVal = ((particularbean.getCgst().length() > 0 ? Float.parseFloat(particularbean.getCgst()) : 0) * total) / 100f;
                    double igstVal = ((particularbean.getIgst().length() > 0 ? Float.parseFloat(particularbean.getIgst()) : 0) * total) / 100f;
                    double sgstVal = ((particularbean.getSgst().length() > 0 ? Float.parseFloat(particularbean.getSgst()) : 0) * total) / 100f;
                    String cgstValS = particularbean.getCgst().length() > 0 ? particularbean.getCgst() : "0";
                    String igstValS = particularbean.getIgst().length() > 0 ? particularbean.getIgst() : "0";
                    String sgstValS = particularbean.getSgst().length() > 0 ? particularbean.getSgst() : "0";
                    table2.addCell(createTextCellBorderLeft(particularbean.getQuantity(), catNormalFont));
                    table2.addCell(createTextCellBorderLeft(particularbean.getPerquantity(), catNormalFont));
                    table2.addCell(createTextCellBorderLeft(round(sgstVal, 2) + "\n@" + sgstValS + "%", catNormalFont));
                    table2.addCell(createTextCellBorderLeft(round(cgstVal, 2) + "\n@" + cgstValS + "%", catNormalFont));
                    table2.addCell(createTextCellBorderLeft(round(igstVal, 2) + "\n@" + igstValS + "%", catNormalFont));
                    total = total + cgstVal + igstVal + sgstVal;
                } else {
                    table2.addCell(createTextCellBorderLeft("", catNormalFont));
                    table2.addCell(createTextCellBorderLeft("", catNormalFont));
                    table2.addCell(createTextCellBorderLeft("", catNormalFont));
                    table2.addCell(createTextCellBorderLeft(particularbean.getQuantity(), catNormalFont));
                    table2.addCell(createTextCellBorderLeft(particularbean.getPerquantity(), catNormalFont));
                }

                table2.addCell(createTextCellleftrightRight(round(total, 2), catFont));
                grantTotal = grantTotal + total;
            }


        }

        table2.setKeepTogether(true);
        //document.add(table2);
        tableAll.addCell(createTable(table2, 0, whiteBase, false));


        PdfPTable table3 = null;
        if (!preferenceBean.isSerial()) {
            table3 = new PdfPTable(7);
            table3.setWidthPercentage(100);
            if (isGst) {
                table3.setWidths(new float[]{2.8f, 0.7f, 0.7f, 0.7f, 0.7f, 0.7f, 0.9f});
            } else {
                table3.setWidths(new float[]{4.9f, 0, 0, 0, 0.7f, 0.7f, 0.9f});
            }
        } else {
            table3 = new PdfPTable(8);
            table3.setWidthPercentage(100);
            if (isGst) {
                table3.setWidths(new float[]{0.5f, 2.3f, 0.7f, 0.7f, 0.7f, 0.7f, 0.7f, 0.9f});
            } else {
                table3.setWidths(new float[]{0.5f, 4.4f, 0, 0, 0, 0.7f, 0.7f, 0.9f});
            }
        }
        if (preferenceBean.isSerial()) {
            table3.addCell(createTextCellBootompakage("", catFont));
        }
        table3.addCell(createTextCellBootompakage("L.T + P&H Charges", catFont));
        table3.addCell(createTextCellBootompakage("", catNormalFont));
        table3.addCell(createTextCellBootompakage("", catNormalFont));
        if (isGst) {
            table3.addCell(createTextCellBootompakage("", catNormalFont));
            table3.addCell(createTextCellBootompakage("", catNormalFont));
            table3.addCell(createTextCellBootompakage("", catNormalFont));
        } else {
            table3.addCell(createTextCellBootompakage("", catNormalFont));
            table3.addCell(createTextCellBootompakage("", catNormalFont));
            table3.addCell(createTextCellBootompakage("", catNormalFont));

        }
        table3.addCell(createTextCellprevoius(mainbean.getPakagecost() != null ? round(
                Double.parseDouble(mainbean.getPakagecost()), 2) : "0.00", catFont));

        if (preferenceBean.isSerial()) {
            table3.addCell(createTextCellBootompakage("", catFont));
        }
        table3.addCell(createTextCellBootompakage("Previous Balance", catFont));
        table3.addCell(createTextCellBootompakage("", catNormalFont));
        table3.addCell(createTextCellBootompakage("", catNormalFont));
        if (isGst) {
            table3.addCell(createTextCellBootompakage("", catNormalFont));
            table3.addCell(createTextCellBootompakage("", catNormalFont));
            table3.addCell(createTextCellBootompakage("", catNormalFont));
        } else {
            table3.addCell(createTextCellBootompakage("", catNormalFont));
            table3.addCell(createTextCellBootompakage("", catNormalFont));
            table3.addCell(createTextCellBootompakage("", catNormalFont));

        }
        table3.addCell(createTextCellprevoius(mainbean.getPrevious() != null ? round(
                Double.parseDouble(mainbean.getPrevious()), 2) : "0.00", catFont));


        table3.setKeepTogether(true);
        //document.add(table3);
        tableAll.addCell(createTable(table3, 0, whiteBase, false));


        PdfPTable table03 = null;
        if (isGst) {
            table03 = new PdfPTable(7);
            table03.setWidthPercentage(100);
            table03.setWidths(new float[]{5.6f, 0, 0, 0, 0, 0.7f, 0.9f});

        } else {
            table03 = new PdfPTable(7);
            table03.setWidthPercentage(100);
            table03.setWidths(new float[]{5.6f, 0, 0, 0, 0, 0.7f, 0.9f});
        }

        float pakageVal = (((mainbean.getPakagecost() != null && mainbean.getPakagecost().length() > 0) ? Float.parseFloat(mainbean.getPakagecost()) : 0));
        float previousVal = ((mainbean.getPrevious().length() > 0 ? Float.parseFloat(mainbean.getPrevious()) : 0));
        grantTotal = grantTotal + pakageVal + previousVal;

        table03.addCell(createTextCellBootomparticlure(AppConfig.convertToIndianCurrency(grantTotal + ""), catNormalFont));

        table03.addCell(createTextCellBorderbootomtotal("", catNormalFont));
        table03.addCell(createTextCellBorderbootomtotal("", catNormalFont));
        if (isGst) {
            table03.addCell(createTextCellBorderbootomtotal("", catNormalFont));
            table03.addCell(createTextCellBorderbootomtotal("", catNormalFont));
            table03.addCell(createTextCellBorderbootomtotal("", catNormalFont));
        } else {
            table03.addCell(createTextCellBorderbootomtotal("", catNormalFont));
            table03.addCell(createTextCellBorderbootomtotal("", catNormalFont));
            table03.addCell(createTextCellBorderbootomtotal("", catNormalFont));

        }
        table03.addCell(createTextCellToplessRight(round(grantTotal, 2) + "", catFont));

        table03.setKeepTogether(true);
        //document.add(table03);
        tableAll.addCell(createTable(table03, 0, whiteBase, false));
        tableAll.setSplitLate(false);
        document.add(tableAll);

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
        cell.setBackgroundColor(greenLightBase);
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
        cell.setBackgroundColor(greenLightBase);
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

    public static PdfPCell createTextCellCenter(String text, Font font) throws DocumentException, IOException {
        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph(text, font);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        cell.setBorder(Rectangle.NO_BORDER);
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
            canvas.setLineDash(2, 6, 2);
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
