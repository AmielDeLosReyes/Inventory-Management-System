package dashboard.IMS.utilities;

import dashboard.IMS.entity.Sales;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelUtil {
    public static byte[] generateSalesReportExcel(List<Sales> salesList) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sales Report");

        // Headers
        String[] headers = {"#", "Name", "Cost", "Revenue", "Quantity", "Type", "Profit", "Transaction Date"};
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // Data
        int rowCount = 1;
        for (Sales sale : salesList) {
            Row row = sheet.createRow(rowCount++);
            row.createCell(0).setCellValue(rowCount);
            row.createCell(1).setCellValue(sale.getProductName());
            row.createCell(2).setCellValue(sale.getTotalCost().doubleValue()); // Convert BigDecimal to double
            row.createCell(3).setCellValue(sale.getTotalRevenue().doubleValue()); // Convert BigDecimal to double
            row.createCell(4).setCellValue(sale.getQuantitySold());
            row.createCell(5).setCellValue(sale.getIsRefund() ? "Refund" : "Sale");
            row.createCell(6).setCellValue(sale.getTotalProfit().doubleValue()); // Convert BigDecimal to double
            row.createCell(7).setCellValue(String.valueOf(sale.getTransactionDate()));
        }

        // Auto size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write to byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return outputStream.toByteArray();
    }
}