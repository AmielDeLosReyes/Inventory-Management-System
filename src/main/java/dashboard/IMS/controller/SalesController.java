package dashboard.IMS.controller;

import dashboard.IMS.entity.Product;
import dashboard.IMS.entity.ProductVariation;
import dashboard.IMS.entity.Sales;
import dashboard.IMS.repository.ProductVariationRepository;
import dashboard.IMS.repository.SalesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Controller
public class SalesController {
    @Autowired
    private ProductVariationRepository productVariationRepository;

    @Autowired
    private SalesRepository salesRepository; // Autowire SalesRepository

    @PostMapping("/sell-product-variation")
    public String sellProductVariation(@RequestParam("productVariationId") Integer productVariationId,
                                       @RequestParam("quantity") Integer quantity,
                                       RedirectAttributes redirectAttributes) {

        // Retrieve the product variation using its ID
        ProductVariation productVariation = productVariationRepository.findById(productVariationId).orElse(null);

        if (productVariation != null) {
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
                        .build();

                // Save the sales record to the database
                salesRepository.save(sales);

                // Redirect to the sales report page
                return "redirect:/sales-report";
            }
        }

        // If product or product variation not found, redirect with error message
        redirectAttributes.addFlashAttribute("message", "Failed to sell product variation. Please try again.");
        return "redirect:/products";
    }
}
