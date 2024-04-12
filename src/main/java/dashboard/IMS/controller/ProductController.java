package dashboard.IMS.controller;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import dashboard.IMS.dto.ProductVariationDTO;
import dashboard.IMS.dto.UserDTO;
import dashboard.IMS.entity.Product;
import dashboard.IMS.entity.ProductVariation;
import dashboard.IMS.entity.Sales;
import dashboard.IMS.entity.User;

import dashboard.IMS.repository.ProductRepository;

import dashboard.IMS.repository.ProductVariationRepository;
import dashboard.IMS.repository.SalesRepository;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
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

    @Autowired
    ProductVariationRepository productVariationRepository;

    @Autowired
    private SalesRepository salesRepository;

    private final ProductService productService;
    private final ProductVariationService productVariationService;

    public ProductController(ProductService productService, ProductVariationService productVariationService) {
        this.productService = productService;
        this.productVariationService = productVariationService;
    }



    /**
     * Directs users to the product list page.
     * Retrieves product and product variation data owned by the logged-in user.
     * Prepares necessary data for display on the product list page.
     *
     * @return Name of the product list page.
     */
    @GetMapping("/products")
    @ResponseStatus(HttpStatus.OK)
    public String productList(Model model, HttpServletRequest request, @RequestParam(defaultValue = "0") int page) {

        // Retrieve the logged-in user from the session
        UserDTO loggedInUser = (UserDTO) request.getSession().getAttribute("loggedInUser");
        if (loggedInUser == null) {
            // Redirect to login if user not logged in
            return "redirect:/login";
        }

        Pageable pageable = PageRequest.of(page, 5); // 5 items per page
        Page<Product> productsPage = productService.getProducts(pageable);
        model.addAttribute("productsPage", productsPage);

        // Add the authenticatedUser to the model if needed for the view
        model.addAttribute("loggedInUser", loggedInUser);

        model.addAttribute("profilePicture", loggedInUser.getProfilePicture());

        // Retrieve products owned by the logged-in user from the database
        List<Product> products = productRepository.findByUserIdAndDeletedFalse(loggedInUser.getId());

        // Retrieve all product variations owned by the logged-in user from the database
        List<ProductVariation> productVariations = productVariationRepository.findAllByProductUserId(loggedInUser.getId());

        // Retrieve total quantities of product variations owned by the logged-in user
        List<Object[]> totalQuantities = productVariationRepository.getTotalQuantitiesByProductUserId(loggedInUser.getId());

        // Create a map to store total quantities for each product
        Map<Integer, Integer> productTotalQuantities = new HashMap<>();
        for (Object[] row : totalQuantities) {
            if (row.length < 2) {
                // Handle the case when the row doesn't contain enough elements
                // Maybe log an error or skip this row
                continue; // Skip this row and continue with the next one
            }

            Integer productId;
            if (row[0] instanceof ProductVariation) {
                productId = ((ProductVariation) row[0]).getProduct().getId();
            } else if (row[0] instanceof Integer) {
                productId = (Integer) row[0];
            } else {
                // Handle the case when the type of row[0] is unexpected
                // Maybe log an error or skip this row
                continue; // Skip this row and continue with the next one
            }

            Integer totalQuantity = ((Number) row[1]).intValue();
            productTotalQuantities.put(productId, totalQuantity);
        }

        // Extract the first image URL for each product
        Map<Integer, String> productImageUrls = new HashMap<>();
        for (Product product : products) {
            String imageUrls = product.getImageUrls(); // Get the comma-separated list
            System.out.println("Product ID: " + product.getId());
            System.out.println("Image URLs: " + imageUrls);
            if (imageUrls != null && !imageUrls.isEmpty()) {
                // Remove square brackets if present
                imageUrls = imageUrls.replaceAll("\\[|\\]", "");
                String[] imageUrlArray = imageUrls.split(","); // Split by comma
                System.out.println("Image URL Array Length: " + imageUrlArray.length);
                if (imageUrlArray.length > 0) {
                    String firstImageUrl = imageUrlArray[0].trim(); // Get the first URL
                    // Remove leading backslash if present
                    if (firstImageUrl.startsWith("/")) {
                        firstImageUrl = firstImageUrl.substring(1);
                    }
                    System.out.println("First Image URL: " + firstImageUrl);
                    productImageUrls.put(product.getId(), firstImageUrl);
                } else {
                    System.out.println("No image URLs found for product ID: " + product.getId());
                }
            } else {
                System.out.println("No image URLs found for product ID: " + product.getId());
            }

            // Retrieve associated product variations for each product
            List<ProductVariation> associatedProductVariations = productVariationRepository.findByProductId(product.getId());
            // Set the retrieved product variations to the current product
            product.setProductVariations(associatedProductVariations);
        }

        // Retrieve sales records associated with the logged-in user
        List<Sales> salesRecords = salesRepository.findByUserId(loggedInUser.getId());

        // Pass the sales records to the view
        model.addAttribute("salesRecords", salesRecords);

        // Pass the product variations to the view
        model.addAttribute("productVariations", productVariations);
        model.addAttribute("products", products);
        model.addAttribute("productImageUrls", productImageUrls);
        model.addAttribute("productTotalQuantities", productTotalQuantities); // Pass total quantities as an attribute

        // Check if message exists in flash attributes
        if (model.containsAttribute("message")) {
            // Retrieve the message from flash attributes and add it to the model
            model.addAttribute("message", model.getAttribute("message"));
        }

        return "products";
    }

    /**
     * Directs users to the add product page.
     *
     * @return Name of the add product page.
     */
    @GetMapping("/add-product")
    @ResponseStatus(HttpStatus.OK)
    public String addProduct(Model model, HttpServletRequest request) {
        UserDTO loggedInUser = (UserDTO) request.getSession().getAttribute("loggedInUser");

        if (loggedInUser != null) {
            model.addAttribute("profilePicture", loggedInUser.getProfilePicture());
            model.addAttribute("loggedInUser", loggedInUser);
        }
        return "addproduct";
    }

    /**
     * Directs users to the edit product page.
     *
     * @return Name of the edit product page.
     */
    @GetMapping("edit-product/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public String editProduct(@PathVariable Integer productId, Model model, HttpServletRequest request) {
        UserDTO loggedInUser = (UserDTO) request.getSession().getAttribute("loggedInUser");

        if (loggedInUser != null) {
            Product product = productService.getProductById(productId);
            model.addAttribute("product", product);

            model.addAttribute("profilePicture", loggedInUser.getProfilePicture());
            model.addAttribute("loggedInUser", loggedInUser);
        }
        return "editproduct";
    }

    /**
     * Handles form submission for adding a new product.
     * Validates the form inputs and processes the addition of the product with variations.
     *
     * @return Redirect to the product list page after adding the product.
     */
    @PostMapping("add-product")
    @ResponseStatus(HttpStatus.CREATED)
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


    /**
     * Deletes a product and its associated data.
     * Handles product deletion based on the product ID.
     *
     * @param id                The ID of the product to be deleted.
     * @param redirectAttributes Redirect attributes for passing messages.
     * @param request           HTTP servlet request.
     * @return Redirect to the product list page after deletion.
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
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

            return "redirect:/products";
        } catch (Exception e) {
            // You may handle exception here more gracefully, such as providing a user-friendly error message or logging the error for further investigation
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to delete product. Please try again.");
            redirectAttributes.addFlashAttribute("messageType", "failure");

            return "redirect:/products";
        }
    }


    /**
     * Edits a product and its associated data.
     * Handles product editing based on the product ID.
     *
     * @param productId          The ID of the product to be edited.
     * @param updatedProduct     The updated product data.
     * @param redirectAttributes Redirect attributes for passing messages.
     * @return Redirect to the product list page after editing.
     */
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/edit-product/{productId}")
    public String editProduct(@PathVariable Integer productId,
                              @ModelAttribute Product updatedProduct,
                              RedirectAttributes redirectAttributes) {
        try {
            productService.updateProduct(productId, updatedProduct);
            redirectAttributes.addFlashAttribute("message", "Product updated successfully");
            redirectAttributes.addFlashAttribute("messageType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Failed to update product. Please try again.");
            redirectAttributes.addFlashAttribute("messageType", "failure");
        }
        return "redirect:/products";
    }


}
