package com.vone.medisafe.cashier;

import java.net.URL;

import com.lowagie.text.Document;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

class PDFBackground extends PdfPageEventHelper{
	@Override
	public void onEndPage(PdfWriter writer, Document document){
        try {
        	URL  url = PDFBackground.class.getResource("TS.jpg");
     		Image background = Image.getInstance(url.getPath());
            // This scales the image to the page,
            // use the image's width & height if you don't want to scale.
            float width = document.getPageSize().width();
            float height = document.getPageSize().height();
            writer.getDirectContentUnder()
                    .addImage(background, width, 0, 0, height, 0, 0);

        }catch(Exception e) {
        	
        }
    }


}
