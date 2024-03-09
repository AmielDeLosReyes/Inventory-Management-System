package dashboard.IMS.controller;

import dashboard.IMS.entity.ProductVariation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class SalesController {

    @PostMapping("/sell-product-variation")
    public String sellProductVariation(@RequestParam("productVariationId") Integer productVariationId, @RequestParam("quantity") Integer quantity) {
        // Print a message with the sold product variation ID and quantity
        System.out.println("Selling product variation with ID: " + productVariationId + " with a quantity of " + quantity);

        // Redirect to the sales report page
        return "redirect:/sales-report";
    }
}
