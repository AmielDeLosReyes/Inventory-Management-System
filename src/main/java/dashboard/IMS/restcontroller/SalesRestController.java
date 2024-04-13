package dashboard.IMS.restcontroller;

import com.itextpdf.text.DocumentException;
import dashboard.IMS.dto.UserDTO;
import dashboard.IMS.entity.Product;
import dashboard.IMS.entity.ProductVariation;
import dashboard.IMS.entity.Sales;
import dashboard.IMS.entity.User;
import dashboard.IMS.repository.ProductVariationRepository;
import dashboard.IMS.repository.SalesRepository;
import dashboard.IMS.repository.UserRepository;
import dashboard.IMS.service.SalesService;
import dashboard.IMS.service.UserService;
import dashboard.IMS.utilities.ExcelUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import dashboard.IMS.utilities.PdfUtil;

/**
 * RestController class for handling sales-related requests.
 *
 * @author Amiel De Los Reyes
 * @date 02/20/2024
 */
@RestController
@RequestMapping("/api")
public class SalesRestController {

    @Autowired
    private ProductVariationRepository productVariationRepository;

    @Autowired
    private SalesRepository salesRepository; // Autowire SalesRepository

    @Autowired
    private UserService userService; // Autowire UserService

    @Autowired
    private UserRepository userRepository; // Autowire UserRepository

    @Autowired
    private SalesService salesService; // Autowire SalesService

    @Autowired
    private PdfUtil pdfUtil;

    /**
     * Fetches sales data associated with the logged-in user and prepares it for display.
     *
     * @param request HTTP servlet request.
     * @param page    The page number.
     * @return Data for the sales report page.
     */
    @GetMapping("/sales-report")
    public ResponseEntity<Map<String, Object>> salesReport(HttpServletRequest request, @RequestParam(defaultValue = "0") int page) {
        Map<String, Object> response = new HashMap<>();
        UserDTO loggedInUser = (UserDTO) request.getSession().getAttribute("loggedInUser");

        if (loggedInUser != null) {
            Pageable pageable = PageRequest.of(page, 5); // 5 items per page
            Page<Sales> salesPage = salesService.getSales(loggedInUser, pageable);
            response.put("salesPage", salesPage);

            List<Sales> salesList = salesPage.getContent(); // Get sales list from page
            int startingId = page * 5; // Calculate the starting ID for this page
            response.put("startingId", startingId);
            response.put("profilePicture", loggedInUser.getProfilePicture());
            response.put("loggedInUser", loggedInUser);

            Map<Integer, String> productImageUrls = new HashMap<>();
            for (Sales sale : salesList) {
                ProductVariation productVariation = productVariationRepository.findById(sale.getProductVariationId()).orElse(null);
                if (productVariation != null) {
                    Product product = productVariation.getProduct();
                    if (product != null) {
                        sale.setProductName(product.getProductName());
                        String imageUrls = product.getImageUrls();
                        if (imageUrls != null && !imageUrls.isEmpty()) {
                            imageUrls = imageUrls.replaceAll("\\[|\\]", "");
                            String[] imageUrlArray = imageUrls.split(",");
                            if (imageUrlArray.length > 0) {
                                String firstImageUrl = imageUrlArray[0].trim();
                                if (firstImageUrl.startsWith("/")) {
                                    firstImageUrl = firstImageUrl.substring(1);
                                }
                                productImageUrls.put(product.getId(), firstImageUrl);
                                sale.setProductImageUrl(firstImageUrl);
                            }
                        }
                    }
                }
            }

            response.put("salesList", salesList);
            response.put("productImageUrls", productImageUrls);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("message", "User is not authenticated");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Handles the sale of a product variation.
     * Validates the sale and updates the database with the sale details.
     *
     * @param productVariationId    The ID of the product variation to be sold.
     * @param quantity              The quantity to be sold.
     * @param request               HTTP servlet request.
     * @return Message indicating the result of the sale.
     */
    @PostMapping("/sell-product-variation")
    public ResponseEntity<String> sellProductVariation(@RequestParam("productVariationId") Integer productVariationId,
                                                       @RequestParam("quantity") Integer quantity,
                                                       HttpServletRequest request) {
        UserDTO loggedInUserDTO = (UserDTO) request.getSession().getAttribute("loggedInUser");

        if (loggedInUserDTO != null) {
            User loggedInUser = userRepository.findUserById(loggedInUserDTO.getId());
            Optional<ProductVariation> optionalProductVariation = productVariationRepository.findByIdAndProductUserId(productVariationId, loggedInUser.getId());

            if (quantity == null || quantity <= 0) {
                return new ResponseEntity<>("Invalid quantity for the sale.", HttpStatus.BAD_REQUEST);
            }

            if (optionalProductVariation.isPresent()) {
                ProductVariation productVariation = optionalProductVariation.get();

                if (productVariation.getQuantity() < quantity) {
                    return new ResponseEntity<>("Quantity is insufficient for the sale.", HttpStatus.BAD_REQUEST);
                }

                int remainingQuantity = productVariation.getQuantity() - quantity;
                if (remainingQuantity < 0) {
                    return new ResponseEntity<>("Invalid quantity for the sale.", HttpStatus.BAD_REQUEST);
                }

                productVariation.setQuantity(remainingQuantity);
                productVariationRepository.save(productVariation);

                Product product = productVariation.getProduct();
                if (product != null) {
                    BigDecimal totalRevenue = product.getSellingPrice().multiply(BigDecimal.valueOf(quantity));
                    BigDecimal totalCost = product.getCostPrice().multiply(BigDecimal.valueOf(quantity));
                    BigDecimal totalProfit = totalRevenue.subtract(totalCost);

                    Sales sales = Sales.builder()
                            .productVariationId(productVariationId)
                            .quantitySold(quantity)
                            .quantityRefunded(0)
                            .totalRevenue(totalRevenue)
                            .totalCost(totalCost)
                            .totalProfit(totalProfit)
                            .transactionDate(LocalDateTime.now())
                            .user(loggedInUser)
                            .build();

                    salesRepository.save(sales);

                    return new ResponseEntity<>("Sale successful.", HttpStatus.CREATED);
                }
            }
        }

        return new ResponseEntity<>("Failed to sell product variation. Please try again.", HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles the refund of a product variation.
     * Validates the refund and updates the database with the refund details.
     *
     * @param productVariationId    The ID of the product variation to be refunded.
     * @param quantity              The quantity to be refunded.
     * @param request               HTTP servlet request.
     * @return Message indicating the result of the refund.
     */
    @PostMapping("/refund-product-variation")
    public ResponseEntity<String> refundProductVariation(@RequestParam("productVariationId") Integer productVariationId,
                                                         @RequestParam("quantity") Integer quantity,
                                                         HttpServletRequest request) {
        UserDTO loggedInUserDTO = (UserDTO) request.getSession().getAttribute("loggedInUser");

        if (loggedInUserDTO != null) {
            User loggedInUser = userRepository.findUserById(loggedInUserDTO.getId());
            Optional<ProductVariation> optionalProductVariation = productVariationRepository.findByIdAndProductUserId(productVariationId, loggedInUser.getId());

            if (quantity == null || quantity <= 0) {
                return new ResponseEntity<>("Invalid quantity for the refund.", HttpStatus.BAD_REQUEST);
            }

            if (optionalProductVariation.isPresent()) {
                ProductVariation productVariation = optionalProductVariation.get();

                List<Sales> salesRecords = salesRepository.findByProductVariationIdAndUserIdOrderByTransactionDateDesc(productVariationId, loggedInUser.getId());

                int totalSoldQuantity = salesRecords.stream()
                        .filter(record -> record.getQuantitySold() != null)
                        .mapToInt(Sales::getQuantitySold)
                        .sum();

                int totalRefundedQuantity = salesRecords.stream()
                        .filter(record -> record.getQuantityRefunded() != null)
                        .mapToInt(Sales::getQuantityRefunded)
                        .sum();

                if (totalSoldQuantity - totalRefundedQuantity - quantity < 0) {
                    return new ResponseEntity<>("Refund quantity cannot be greater than total sold quantity.", HttpStatus.BAD_REQUEST);
                }

                int remainingQuantityToRefund = quantity;
                for (Sales salesRecord : salesRecords) {
                    if (salesRecord.getQuantitySold() != null && salesRecord.getQuantityRefunded() != null && remainingQuantityToRefund > 0) {
                        int quantitySold = salesRecord.getQuantitySold();
                        int quantityRefunded = salesRecord.getQuantityRefunded();
                        int quantityAvailableForRefund = quantitySold - quantityRefunded;

                        int quantityToRefund = Math.min(quantityAvailableForRefund, remainingQuantityToRefund);

                        if (quantityToRefund > 0) {
                            salesRecord.setQuantityRefunded(quantityRefunded + quantityToRefund);
                            salesRepository.save(salesRecord);

                            BigDecimal refundRevenue = salesRecord.getTotalRevenue() != null ? salesRecord.getTotalRevenue().divide(BigDecimal.valueOf(quantitySold), RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(quantityToRefund)) : BigDecimal.ZERO;
                            BigDecimal refundCost = salesRecord.getTotalCost() != null ? salesRecord.getTotalCost().divide(BigDecimal.valueOf(quantitySold), RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(quantityToRefund)) : BigDecimal.ZERO;
                            BigDecimal refundProfit = salesRecord.getTotalProfit() != null ? salesRecord.getTotalProfit().divide(BigDecimal.valueOf(quantitySold), RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(quantityToRefund)) : BigDecimal.ZERO;

                            Sales refundSales = Sales.builder()
                                    .productVariationId(productVariationId)
                                    .quantitySold(quantityToRefund * -1)
                                    .totalRevenue(refundRevenue.negate())
                                    .totalCost(refundCost.negate())
                                    .totalProfit(refundProfit.negate())
                                    .transactionDate(LocalDateTime.now())
                                    .user(loggedInUser)
                                    .isRefund(true)
                                    .build();

                            salesRepository.save(refundSales);

                            productVariation.setQuantity(productVariation.getQuantity() + quantityToRefund);
                            productVariationRepository.save(productVariation);

                            remainingQuantityToRefund -= quantityToRefund;
                        }
                    }
                }

                return new ResponseEntity<>("Refund successful.", HttpStatus.CREATED);
            }
        }

        return new ResponseEntity<>("Failed to refund product variation. Please try again.", HttpStatus.BAD_REQUEST);
    }

    /**
     * Generates a PDF report of all sales associated with the logged-in user.
     * The PDF is sent as a response to the client.
     *
     * @param request  The HTTP servlet request.
     * @throws DocumentException If there is an error while generating the PDF.
     * @throws IOException       If there is an error while writing the PDF to the response output stream.
     */
    @GetMapping("/sales-report/pdf")
    public ResponseEntity<byte[]> salesReportPdf(HttpServletRequest request) throws DocumentException, IOException {
        // Retrieve the logged-in user from the session
        UserDTO loggedInUser = (UserDTO) request.getSession().getAttribute("loggedInUser");

        // Check if the logged-in user is valid
        if (loggedInUser != null) {
            // Fetch all the sales associated with the logged-in user from the repository
            List<Sales> salesList = salesRepository.findAllByUserId(loggedInUser.getId());

            // Set the product name for each sales record
            for (Sales sale : salesList) {
                ProductVariation productVariation = productVariationRepository.findById(sale.getProductVariationId()).orElse(null);
                if (productVariation != null) {
                    Product product = productVariation.getProduct();
                    if (product != null) {
                        sale.setProductName(product.getProductName());
                    }
                }
            }

            // Generate PDF
            byte[] pdfBytes = PdfUtil.generateSalesReportPdf(salesList);

            // Get current date and format it as a string
            String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            // Set response headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.builder("attachment").filename("sales-report-" + currentDate + ".pdf").build());

            // Return the PDF bytes as a ResponseEntity
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } else {
            // If the user is not authenticated, return an unauthorized status
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }


    /**
     * Generates an Excel report of all sales associated with the logged-in user.
     * The Excel file is sent as a response to the client.
     *
     * @param request  The HTTP servlet request.
     * @return ResponseEntity with the Excel file bytes and appropriate HTTP headers.
     * @throws IOException If there is an error while generating the Excel file.
     */
    @GetMapping("/sales-report/excel")
    public ResponseEntity<byte[]> salesReportExcel(HttpServletRequest request) throws IOException {
        // Retrieve the logged-in user from the session
        UserDTO loggedInUser = (UserDTO) request.getSession().getAttribute("loggedInUser");

        // Check if the logged-in user is valid
        if (loggedInUser != null) {
            // Fetch all the sales associated with the logged-in user from the repository
            List<Sales> salesList = salesRepository.findAllByUserId(loggedInUser.getId());

            // Set the product name for each sales record
            for (Sales sale : salesList) {
                ProductVariation productVariation = productVariationRepository.findById(sale.getProductVariationId()).orElse(null);
                if (productVariation != null) {
                    Product product = productVariation.getProduct();
                    if (product != null) {
                        sale.setProductName(product.getProductName());
                    }
                }
            }

            // Generate Excel
            byte[] excelBytes = ExcelUtil.generateSalesReportExcel(salesList);

            // Get current date and format it as a string
            String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            // Set response headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDisposition(ContentDisposition.builder("attachment").filename("sales-report-" + currentDate + ".xlsx").build());

            // Return the Excel bytes as a ResponseEntity
            return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
        } else {
            // If the user is not authenticated, return an unauthorized status
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

}
