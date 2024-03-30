package dashboard.IMS.controller;

import java.util.concurrent.CompletableFuture;
import dashboard.IMS.dto.ProductVariationDTO;
import dashboard.IMS.dto.UserDTO;
import dashboard.IMS.entity.Product;
import dashboard.IMS.entity.ProductVariation;
import dashboard.IMS.entity.User;
import dashboard.IMS.service.ProductService;
import dashboard.IMS.service.ProductVariationService;
import dashboard.IMS.service.SalesService;
import dashboard.IMS.utilities.FileUploadUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private SalesService salesService;

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
                                 RedirectAttributes redirectAttributes,
                                 HttpServletRequest request) {
        try {
            // Get logged-in user from session
            UserDTO loggedInUser = (UserDTO) request.getSession().getAttribute("loggedInUser");
            if (loggedInUser == null) {
                // Redirect to login if user not logged in
                return "redirect:/login";
            }

            // Call the method in ProductService to handle product addition with variations
            productService.addProductWithVariationsFromFormData(productName, productDescription, images,
                    size, color, quantity, cost, sellingPrice, loggedInUser);
            return "redirect:/products";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to add product. Please try again.");
            return "redirect:/product/add-product";
        }
    }


    @PostMapping("/products/{id}/delete")
    @Transactional
    public String deleteProduct(@PathVariable Integer id, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        try {
            // Get the logged-in user from the session
            UserDTO loggedInUser = (UserDTO) request.getSession().getAttribute("loggedInUser");
            if (loggedInUser == null) {
                // Redirect to login if user not logged in
                return "redirect:/login";
            }

            // Check if the product belongs to the logged-in user
            Product productToDelete = productService.getProductById(id);
            if (productToDelete == null || !productToDelete.getUser().getId().equals(loggedInUser.getId())) {
                // Product not found or does not belong to the logged-in user, handle accordingly (e.g., show error message)
                redirectAttributes.addFlashAttribute("error", "You are not authorized to delete this product.");
                return "redirect:/products";
            }

            // Delete the sales records associated with the product variations of the product
            for (ProductVariation productVariation : productToDelete.getProductVariations()) {
                salesService.deleteSalesByProductVariationId(productVariation.getId());
            }

            // Delete the product and its variations
            productVariationService.deleteProductVariation(id);
            productService.deleteProduct(id);

            // Add success message
            redirectAttributes.addFlashAttribute("message", "Successfully deleted the item");
            return "redirect:/products";
        } catch (Exception e) {
            e.printStackTrace();
            // You may handle exception here
            redirectAttributes.addFlashAttribute("error", "Failed to delete product. Please try again.");
            return "redirect:/products";
        }
    }


}
