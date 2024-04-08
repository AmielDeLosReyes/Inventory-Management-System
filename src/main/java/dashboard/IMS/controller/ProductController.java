package dashboard.IMS.controller;

import java.util.concurrent.CompletableFuture;
import dashboard.IMS.dto.ProductVariationDTO;
import dashboard.IMS.dto.UserDTO;
import dashboard.IMS.entity.Product;
import dashboard.IMS.entity.ProductVariation;
import dashboard.IMS.entity.User;

import dashboard.IMS.repository.ProductRepository;

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

    @Autowired
    private ProductRepository productRepository;

    private final ProductService productService;
    private final ProductVariationService productVariationService;

    public ProductController(ProductService productService, ProductVariationService productVariationService) {
        this.productService = productService;
        this.productVariationService = productVariationService;
    }

    /**
     * Handles form submission for adding a new product.
     * Validates the form inputs and processes the addition of the product with variations.
     *
     * @return Redirect to the product list page after adding the product.
     */
    @PostMapping("add-product")
    public String addProductForm(
            @RequestParam("productName") String productName,
            @RequestParam("productDescription") String productDescription,
            @RequestParam("images") MultipartFile[] images,
            @RequestParam("size[]") int[] sizes,
            @RequestParam("color[]") int[] colors,
            @RequestParam("quantity[]") int[] quantities,
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

            // Create a new product
            Product newProduct = new Product();
            newProduct.setProductName(productName);
            newProduct.setProductDescription(productDescription);
            newProduct.setCostPrice(BigDecimal.valueOf(cost));
            newProduct.setSellingPrice(BigDecimal.valueOf(sellingPrice));
            // Save the new product
            Product savedProduct = productRepository.save(newProduct);

            // Call the method in ProductService to handle product addition with variations
            int iterations = Math.min(images.length, sizes.length);
            iterations = Math.min(iterations, colors.length);
            iterations = Math.min(iterations, quantities.length);
            for (int i = 0; i < iterations; i++) {
                productService.addProductWithVariationsFromFormData(savedProduct, images, sizes[i], colors[i], quantities[i], loggedInUser);
            }

            return "redirect:/products";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to add product. Please try again.");
            return "redirect:/product/add-product";
        }
    }



//    @PostMapping("add-product")
//    public String addProductForm(@RequestParam("productName") String productName,
//                                 @RequestParam("productDescription") String productDescription,
//                                 @RequestParam("images") MultipartFile[] images,
//                                 @RequestParam("size") int size,
//                                 @RequestParam("color") int color,
//                                 @RequestParam("quantity") int quantity,
//                                 @RequestParam("cost") double cost,
//                                 @RequestParam("sellingPrice") double sellingPrice,
//                                 RedirectAttributes redirectAttributes,
//                                 HttpServletRequest request) {
//        try {
//            // Get logged-in user from session
//            UserDTO loggedInUser = (UserDTO) request.getSession().getAttribute("loggedInUser");
//            if (loggedInUser == null) {
//                // Redirect to login if user not logged in
//                return "redirect:/login";
//            }
//
//            // Call the method in ProductService to handle product addition with variations
//            productService.addProductWithVariationsFromFormData(productName, productDescription, images,
//                    size, color, quantity, cost, sellingPrice, loggedInUser);
//            return "redirect:/products";
//        } catch (Exception e) {
//            e.printStackTrace();
//            redirectAttributes.addFlashAttribute("error", "Failed to add product. Please try again.");
//            return "redirect:/product/add-product";
//        }
//    }


    /**
     * Deletes a product and its associated data.
     * Handles product deletion based on the product ID.
     *
     * @param id                The ID of the product to be deleted.
     * @param redirectAttributes Redirect attributes for passing messages.
     * @param request           HTTP servlet request.
     * @return Redirect to the product list page after deletion.
     */
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


            // Set the deleted flag to true instead of deleting the product
            productToDelete.setDeleted(true);
            productRepository.save(productToDelete);

            // Add success message
            redirectAttributes.addFlashAttribute("message", "Successfully deleted the item");
            redirectAttributes.addFlashAttribute("messageType", "success");

            // Add success message
            redirectAttributes.addFlashAttribute("message", "Successfully deleted the item");

            return "redirect:/products";
        } catch (Exception e) {
            e.printStackTrace();
            // You may handle exception here
            redirectAttributes.addFlashAttribute("error", "Failed to delete product. Please try again.");

            redirectAttributes.addFlashAttribute("messageType", "failure");

            return "redirect:/products";
        }
    }


}
