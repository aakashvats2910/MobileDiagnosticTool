package org.kashisol.mobilediagnostictool.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PDFFunctionality {

    public static void makepdfv2(Context context, String data) {
        System.out.println("()()()() PHUL DATA: " + data);
        try {
            final File file = new File(context.getFilesDir() + "/sample.pdf");
            file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file);

            System.out.println("()()()() PATH: " + file.getAbsolutePath());

            PdfDocument document = new PdfDocument();
            PdfDocument.PageInfo pageInfo = new
                    PdfDocument.PageInfo.Builder(100, 100, 1).create();
            PdfDocument.Page page = document.startPage(pageInfo);
            Canvas canvas = page.getCanvas();
            Paint paint = new Paint();

            canvas.drawText(data, 10, 10, paint);


            document.finishPage(page);
            document.writeTo(fOut);
            document.close();

        } catch (IOException e) {
            System.out.println("()()()() ERROR: " + e.toString());
        }
    }

    public static void createMyPDF(Context context, String data){

        PdfDocument myPdfDocument = new PdfDocument();
        PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(300,1000,1).create();
        PdfDocument.Page myPage = myPdfDocument.startPage(myPageInfo);

        Paint myPaint = new Paint();
        String myString = data;
        int x = 10, y=25;
        for (String line:myString.split("\n")){
            myPage.getCanvas().drawText(line, x, y, myPaint);
            y+=myPaint.descent()-myPaint.ascent();
        }
        myPdfDocument.finishPage(myPage);

        String myFilePath = context.getFilesDir() + "/myPDFFile.pdf";
        File myFile = new File(myFilePath);
        System.out.println("()()()() SAVING PATH: " + myFile.getAbsolutePath());
        try {
            myPdfDocument.writeTo(new FileOutputStream(myFile));
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println(("error"));
        }
        myPdfDocument.close();
    }

//    public static void createMyPDFv2(Context context, String data){
//
//        File mydir = context.getDir("users", Context.MODE_PRIVATE); //Creating an internal dir;
//        if (!mydir.exists())
//        {
//            mydir.mkdirs();
//        }
//        System.out.println("()()()() PAATH: " + mydir.getAbsolutePath());
//
//        File file = new File(this.getFilesDir(), "/mobilediagnosisresult.pdf");
//        file.createNewFile();
//        System.out.println("()()()() PATH: " + file.getAbsolutePath());
//
//        Document document = new Document();  // create the document
//        PdfWriter.getInstance(document, new FileOutputStream(file));
//        document.open();
//
//        Paragraph p3=new Paragraph();  // to enter value you have to create paragraph  and add value in it then paragraph is added into document
//        p3.add("Mobile Daignosis Report");
//        document.add(p3);
//
//        PdfPTable table = new PdfPTable(4);
//
//        table.addCell("S No.");
//        table.addCell("Name");
//        table.addCell("Value");
//        table.addCell("Result");
//
//        int i=1;
//        while (cursor.moveToNext()){
//            table.addCell(String.valueOf(i));
//            table.addCell(cursor.getString(1));
//            table.addCell(cursor.getString(2));
//            table.addCell(cursor.getString(3));
//            i++;
//        }
//        document.add(table);
//        document.addCreationDate();
//        document.close();
//    }

}
