package dashboard.IMS.controller;

import dashboard.IMS.dto.UserDTO;
import dashboard.IMS.entity.Product;
import dashboard.IMS.entity.ProductVariation;
import dashboard.IMS.entity.Sales;
import dashboard.IMS.entity.User;
import dashboard.IMS.repository.ProductVariationRepository;
import dashboard.IMS.repository.SalesRepository;
import dashboard.IMS.repository.UserRepository;
import dashboard.IMS.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
<<<<<<< HEAD
import java.util.List;
=======
>>>>>>> origin/main
import java.util.Optional;

@Controller
public class SalesController {
    @Autowired
    private ProductVariationRepository productVariationRepository;

    @Autowired
    private SalesRepository salesRepository; // Autowire SalesRepository

    @Autowired
    private UserService userService; // Autowire UserService

    @Autowired
    private UserRepository userRepository; // Autowire UserRepository


    /**
     * Handles the sale of a product variation.
     * Validates the sale and updates the database with the sale details.
     *
     * @param productVariationId    The ID of the product variation to be sold.
     * @param quantity              The quantity to be sold.
     * @param redirectAttributes    Redirect attributes for passing messages.
     * @param request               HTTP servlet request.
     * @return Redirect to the sales report page after successful sale or to the products page with an error message.
     */
    @PostMapping("/sell-product-variation")
    public String sellProductVariation(@RequestParam("productVariationId") Integer productVariationId,
                                       @RequestParam("quantity") Integer quantity,
                                       RedirectAttributes redirectAttributes,
                                       HttpServletRequest request) {

        // Retrieve the logged-in user from the session attribute
        UserDTO loggedInUserDTO = (UserDTO) request.getSession().getAttribute("loggedInUser");

        // Check if the logged-in user is valid
        if (loggedInUserDTO != null) {

            User loggedInUser = userRepository.findUserById(loggedInUserDTO.getId());
            // Retrieve the product variation using its ID
            Optional<ProductVariation> optionalProductVariation = productVariationRepository.findByIdAndProductUserId(productVariationId, loggedInUser.getId());

            if (optionalProductVariation.isPresent()) {
                ProductVariation productVariation = optionalProductVariation.get();

                // Check if the available quantity is sufficient
                if (productVariation.getQuantity() < quantity) {
                    // If quantity is insufficient, redirect to 404
                    return "redirect:/404";
                }

                // Subtract the sold quantity from the current quantity of the product variation
                int remainingQuantity = productVariation.getQuantity() - quantity;
                if (remainingQuantity <= 0) {
                    // If remaining quantity is zero or negative, redirect to 404
                    return "redirect:/404";
                }

                // Update the quantity with the remaining quantity
                productVariation.setQuantity(remainingQuantity);

                // Save the updated product variation to the database
                productVariationRepository.save(productVariation);

                // Access the product object associated with the product variation
                Product product = productVariation.getProduct();

                if (product != null) {
                    // Calculate total revenue, total cost, and total profit
                    BigDecimal totalRevenue = product.getSellingPrice().multiply(BigDecimal.valueOf(quantity));
                    BigDecimal totalCost = product.getCostPrice().multiply(BigDecimal.valueOf(quantity));
                    BigDecimal totalProfit = totalRevenue.subtract(totalCost);

                    // Create a new Sales object
                    Sales sales = Sales.builder()
                            .productVariationId(productVariationId)
                            .quantitySold(quantity)
                            .totalRevenue(totalRevenue)
                            .totalCost(totalCost)
                            .totalProfit(totalProfit)
                            .transactionDate(LocalDateTime.now()) // Set the transaction date to current date and time
                            .user(loggedInUser) // Associate the current user with the sale record
                            .build();

                    // Save the sales record to the database
                    salesRepository.save(sales);

                    // Redirect to the sales report page
                    return "redirect:/sales-report";
                }
            }
        }

        // If product or product variation not found, or user not logged in, redirect with error message
<<<<<<< HEAD
        redirectAttributes.addFlashAttribute("message", "Failed to refund product variation. Please try again.");
        redirectAttributes.addFlashAttribute("messageType", "failure");
=======
        redirectAttributes.addFlashAttribute("message", "Failed to sell product variation. Please try again.");
>>>>>>> origin/main
        return "redirect:/products";
    }

    @PostMapping("/refund-product-variation")
    public String refundProductVariation(@RequestParam("productVariationId") Integer productVariationId,
                                         @RequestParam("quantity") Integer quantity,
                                         RedirectAttributes redirectAttributes,
                                         HttpServletRequest request) {

        // Retrieve the logged-in user from the session attribute
        UserDTO loggedInUserDTO = (UserDTO) request.getSession().getAttribute("loggedInUser");

        // Check if the logged-in user is valid
        if (loggedInUserDTO != null) {
            // Retrieve the User entity corresponding to the logged-in user
            User loggedInUser = userRepository.findUserById(loggedInUserDTO.getId());
            if (loggedInUser == null) {
                // Handle the case where the User entity is not found
                redirectAttributes.addFlashAttribute("message", "Failed to refund product variation. Please try again.");
                redirectAttributes.addFlashAttribute("messageType", "failure");
                return "redirect:/products";
            }

            // Retrieve the sales record using productVariationId and quantity
            List<Sales> salesRecords = salesRepository.findByProductVariationIdAndUserIdOrderByTransactionDateDesc(productVariationId, loggedInUser.getId());

            int quantityToRefund = quantity;
            for (Sales salesRecord : salesRecords) {
                if (salesRecord.getQuantitySold() >= quantityToRefund) {
                    // Calculate the refund amounts
                    BigDecimal refundRevenue = salesRecord.getTotalRevenue().divide(BigDecimal.valueOf(salesRecord.getQuantitySold())).multiply(BigDecimal.valueOf(quantityToRefund));
                    BigDecimal refundCost = salesRecord.getTotalCost().divide(BigDecimal.valueOf(salesRecord.getQuantitySold())).multiply(BigDecimal.valueOf(quantityToRefund));
                    BigDecimal refundProfit = salesRecord.getTotalProfit().divide(BigDecimal.valueOf(salesRecord.getQuantitySold())).multiply(BigDecimal.valueOf(quantityToRefund));

                    // Update the sales record
                    int updatedQuantitySold = salesRecord.getQuantitySold() - quantityToRefund;
                    if (updatedQuantitySold == 0) {
                        // If updated quantity sold is zero, delete the sales record and return the original quantity to the product variation
                        salesRepository.delete(salesRecord);
                        ProductVariation productVariation = salesRecord.getProductVariation();
                        productVariation.setQuantity(productVariation.getQuantity() + quantityToRefund); // Add the refunded quantity back to the product variation
                        productVariationRepository.save(productVariation);
                    } else {
                        salesRecord.setQuantitySold(updatedQuantitySold);
                        salesRepository.save(salesRecord);
                    }

                    // Create a new Sales object for the refund
                    Sales refundSales = Sales.builder()
                            .productVariationId(productVariationId)
                            .quantitySold(quantityToRefund) // Positive quantity for a refund
                            .totalRevenue(refundRevenue.negate()) // Negative revenue for a refund
                            .totalCost(refundCost.negate()) // Negative cost for a refund
                            .totalProfit(refundProfit.negate()) // Negative profit for a refund
                            .transactionDate(LocalDateTime.now()) // Set the transaction date to current date and time
                            .user(loggedInUser) // Associate the current user with the sale record
                            .isRefund(true) // Indicate that this is a refund
                            .build();

                    // Save the refund sales record to the database
                    salesRepository.save(refundSales);

                    // Redirect to the sales report page
                    return "redirect:/sales-report";
                } else {
                    // If the refund quantity is greater than the sold quantity, return an error message
                    redirectAttributes.addFlashAttribute("message", "Refund quantity cannot be greater than sold quantity.");
                    redirectAttributes.addFlashAttribute("messageType", "failure");
                    return "redirect:/products";
                }
            }
        }

        // If sales record or product variation not found, or user not logged in, redirect with error message
        redirectAttributes.addFlashAttribute("message", "Failed to refund product variation. Please try again.");
        redirectAttributes.addFlashAttribute("messageType", "failure");
        return "redirect:/products";
    }

}
