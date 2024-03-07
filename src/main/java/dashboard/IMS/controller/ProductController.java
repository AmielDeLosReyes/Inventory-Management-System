package dashboard.IMS.controller;

import java.util.concurrent.CompletableFuture;
import dashboard.IMS.dto.ProductVariationDTO;
import dashboard.IMS.entity.Product;
import dashboard.IMS.service.ProductService;
import dashboard.IMS.service.ProductVariationService;
import dashboard.IMS.utilities.FileUploadUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.persistence.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
* Controller class for handling product-related operations
 *
 * Author: Amiel De Los Reyes
 * Date: 02/26/2024
 */
@Controller
@RequestMapping("/product")
public class ProductController {

    @PersistenceContext
    private EntityManager entityManager;

    private final ProductService productService;
    private final ProductVariationService productVariationService;

    public ProductController(ProductService productService, ProductVariationService productVariationService) {
        this.productService = productService;
        this.productVariationService = productVariationService;
    }

    @PostMapping("add-product")
    public String addProductForm(@RequestParam("productName") String productName,
                                 @RequestParam("productDescription") String productDescription,
                                 @RequestParam("images") MultipartFile[] images,
                                 @RequestParam("size") int size,
                                 @RequestParam("color") int color,
                                 @RequestParam("quantity") int quantity,
                                 @RequestParam("cost") double cost,
                                 @RequestParam("sellingPrice") double sellingPrice,
                                 RedirectAttributes redirectAttributes) {
        try {
            // Call the new method in ProductService to handle product addition with variations
            productService.addProductWithVariationsFromFormData(productName, productDescription, images,
                    size, color, quantity, cost, sellingPrice);
            return "redirect:/products";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to add product. Please try again.");
            return "redirect:/product/add-product";
        }
    }

    @PostMapping("/products/{id}/delete")
    @Transactional
    public String deleteProduct(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        productVariationService.deleteProductVariation(id);
        productService.deleteProduct(id);
        redirectAttributes.addFlashAttribute("message", "Successfully deleted the item");
        return "redirect:/products";
    }

}
