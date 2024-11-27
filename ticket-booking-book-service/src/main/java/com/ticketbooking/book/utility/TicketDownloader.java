package com.ticketbooking.book.utility;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.VerticalPositionMark;
import com.ticketbooking.book.dto.BookingResponse;

public class TicketDownloader {
	
	private List<BookingResponse> bookings;
	
	public TicketDownloader() {
		
	}

	public TicketDownloader(List<BookingResponse> bookings) {
		super();
		this.bookings = bookings;
	}

	private void writeTableHeader(PdfPTable table) {
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(new Color(237, 242, 243));
		cell.setPadding(5);

		Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(new Color(31, 53, 65));

		cell.setPhrase(new Phrase("Train Seat", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Price", font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Booing Date", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Status", font));
		table.addCell(cell);

	}

	private void writeTableData(PdfPTable table) {
		for (BookingResponse booking : bookings) {
			table.addCell(booking.getTrainSeat());
			table.addCell(String.valueOf(booking.getSeatPrice()));
			table.addCell(booking.getBookingTime());
			table.addCell(booking.getStatus());
		}
	}

	public void export(HttpServletResponse response) throws DocumentException, IOException {
		Document document = new Document(PageSize.A4);

		PdfWriter.getInstance(document, response.getOutputStream());
		document.open();
		Image image = Image.getInstance("classpath:images/logo.png");
		image.scalePercent(8.0f, 8.0f);
		document.add(image);

		Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		headerFont.setSize(25);
		headerFont.setColor(new Color(31, 53, 65));
		Paragraph pHeader = new Paragraph("Train Ticket Details\n", headerFont);
		pHeader.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(pHeader);

		Font fontD = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		fontD.setSize(13);
		fontD.setColor(Color.BLACK);
		Paragraph pD = new Paragraph("Scheduled Train Id: " + bookings.get(0).getScheduleTrainId(), fontD);
		pD.setAlignment(Paragraph.ALIGN_RIGHT);
		document.add(pD);

		Font fontAI = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		fontAI.setSize(13);
		fontAI.setColor(Color.BLACK);
		Paragraph pAI = new Paragraph("Customer Booking Id: " + bookings.get(0).getBookingId(), fontAI);
		pAI.setAlignment(Paragraph.ALIGN_RIGHT);
		document.add(pAI);

		Font fontP = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		fontP.setSize(18);
		fontP.setColor(new Color(31, 53, 65));
		Chunk glue = new Chunk(new VerticalPositionMark());
		Paragraph pp = new Paragraph("\nTrain Details", fontP);
		pp.add(new Chunk(glue));
		pp.add("Customer Details:");
		document.add(pp);

		Font fontN = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		fontN.setSize(12);
		fontN.setColor(Color.BLACK);
		Chunk glueN = new Chunk(new VerticalPositionMark());
		Paragraph pN = new Paragraph(
				"Train Name: " + bookings.get(0).getTrainName(), fontN);
		pN.add(new Chunk(glueN));
		pN.add("Customer Name: " + bookings.get(0).getUsername());
		document.add(pN);

		Font fontA = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		fontA.setSize(12);
		fontA.setColor(Color.BLACK);
		Chunk glueA = new Chunk(new VerticalPositionMark());
		Paragraph pA = new Paragraph("Train Number: " + bookings.get(0).getTrainNumber(),
				fontA);
		pA.add(new Chunk(glueA));
		pA.add("Customer Mobile No: " + bookings.get(0).getMobile());
		document.add(pA);

		Font fontBG = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		fontBG.setSize(12);
		fontBG.setColor(Color.BLACK);
		Paragraph pBG = new Paragraph(
				"Source Location: " + bookings.get(0).getFromLocation(), fontBG);
		pBG.setAlignment(Paragraph.ALIGN_LEFT);
		document.add(pBG);

		Font fontE = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		fontE.setSize(12);
		fontE.setColor(Color.BLACK);
		Paragraph pE = new Paragraph(
				"Destination Location: " + bookings.get(0).getToLocation(), fontE);
		pE.setAlignment(Paragraph.ALIGN_LEFT);
		document.add(pE);

		Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font.setSize(18);
		font.setColor(new Color(31, 53, 65));
		Paragraph p = new Paragraph("\nBooked Train Seat Details\n", font);
		p.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(p);

		PdfPTable table = new PdfPTable(4);
		table.setWidthPercentage(100f);
		table.setWidths(new float[] { 3.0f, 2.5f, 2.5f, 2.7f });
		table.setSpacingBefore(10);

		writeTableHeader(table);
		writeTableData(table);

		document.add(table);
		
		document.close();

	}

}
