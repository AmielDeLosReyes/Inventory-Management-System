package dashboard.IMS.utilities;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import dashboard.IMS.entity.Sales;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.itextpdf.text.Chunk;
import org.springframework.stereotype.Service;

@Service
public class PdfUtil {
    public static byte[] generateSalesReportPdf(List<Sales> salesList) throws DocumentException, IOException {
        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, outputStream);
        document.open();

        Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        Font cellFont = new Font(Font.FontFamily.HELVETICA, 10);

        // Title: Sales Report - Date
        Paragraph title = new Paragraph("Sales Report - " + getCurrentDate(), titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        // Add line break
        document.add(new Paragraph(" "));

        // Table
        PdfPTable table = new PdfPTable(8); // 8 columns (excluding ID and Photo)
        table.setWidthPercentage(100);

        // Headers
        String[] headers = {"#", "Name", "Cost", "Revenue", "Quantity", "Type", "Profit", "Transaction Date"};
        for (String header : headers) {
            PdfPCell headerCell = new PdfPCell(new Paragraph(header, headerFont));
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(headerCell);
        }

        // Data
        int rowCount = 1;
        for (Sales sale : salesList) {
            table.addCell(createCell(String.valueOf(rowCount++), cellFont)); // Row count
            table.addCell(createCell(sale.getProductName(), cellFont)); // Name
            table.addCell(createCell(String.valueOf(sale.getTotalCost()), cellFont)); // Cost
            table.addCell(createCell(String.valueOf(sale.getTotalRevenue()), cellFont)); // Revenue
            table.addCell(createCell(String.valueOf(sale.getQuantitySold()), cellFont)); // Quantity
            table.addCell(createCell(sale.getIsRefund() ? "Refund" : "Sale", cellFont)); // Type
            table.addCell(createCell(String.valueOf(sale.getTotalProfit()), cellFont)); // Profit
            table.addCell(createCell(String.valueOf(sale.getTransactionDate()), cellFont)); // Transaction Date
        }

        document.add(table);
        document.close();
        return outputStream.toByteArray();
    }

    private static String getCurrentDate() {
        LocalDate currentDate = LocalDate.now();
        return currentDate.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")); // Format: April 20, 2024
    }

    private static PdfPCell createCell(String content, Font font) {
        PdfPCell cell = new PdfPCell(new Paragraph(content, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return cell;
    }
}

