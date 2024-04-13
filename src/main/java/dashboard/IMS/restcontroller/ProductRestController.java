package dashboard.IMS.restcontroller;

import dashboard.IMS.dto.UserDTO;
import dashboard.IMS.entity.Product;
import dashboard.IMS.entity.ProductVariation;
import dashboard.IMS.entity.Sales;
import dashboard.IMS.repository.ProductRepository;
import dashboard.IMS.repository.ProductVariationRepository;
import dashboard.IMS.repository.SalesRepository;
import dashboard.IMS.service.ProductService;
import dashboard.IMS.service.ProductVariationService;
import dashboard.IMS.service.SalesService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.*;

/**
 * RestController class for handling product-related operations
 *
 * Author: Amiel De Los Reyes
 * Date: 02/26/2024
 */
@RestController
@RequestMapping("/api")
public class ProductRestController {
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

    @Autowired
    private ProductService productService;

    private final ProductVariationService productVariationService;

    /**
     * Constructor for ProductRestController
     *
     * @param productService The service class for product-related operations
     * @param productVariationService The service class for product variation-related operations
     */
    public ProductRestController(ProductService productService, ProductVariationService productVariationService) {
        this.productService = productService;
        this.productVariationService = productVariationService;
    }

    /**
     * Retrieves product and product variation data owned by the logged-in user.
     * Prepares necessary data for display on the product list page.
     *
     * @param request The HttpServletRequest object
     * @param page The page number for pagination
     * @return Data for the product list page.
     */
    @GetMapping("/products")
    public ResponseEntity<Map<String, Object>> productList(HttpServletRequest request, @RequestParam(defaultValue = "0") int page) {
        Map<String, Object> data = new HashMap<>();

        // Retrieve the logged-in user from the session
        UserDTO loggedInUser = (UserDTO) request.getSession().getAttribute("loggedInUser");
        if (loggedInUser == null) {
            // User is not authenticated, return an error message
            data.put("error", "User is not authenticated");
            return new ResponseEntity<>(data, HttpStatus.UNAUTHORIZED);
        }

        Pageable pageable = PageRequest.of(page, 5); // 5 items per page
        Page<Product> productsPage = productService.getProducts(pageable);
        data.put("productsPage", productsPage);

        // Add the authenticatedUser to the data
        data.put("loggedInUser", loggedInUser);

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
                continue;
            }

            Integer productId;
            if (row[0] instanceof ProductVariation) {
                productId = ((ProductVariation) row[0]).getProduct().getId();
            } else if (row[0] instanceof Integer) {
                productId = (Integer) row[0];
            } else {
                continue;
            }

            Integer totalQuantity = ((Number) row[1]).intValue();
            productTotalQuantities.put(productId, totalQuantity);
        }

        // Extract the first image URL for each product
        Map<Integer, String> productImageUrls = new HashMap<>();
        for (Product product : products) {
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
                }
            }

            // Retrieve associated product variations for each product
            List<ProductVariation> associatedProductVariations = productVariationRepository.findByProductId(product.getId());
            // Set the retrieved product variations to the current product
            product.setProductVariations(associatedProductVariations);
        }

        // Retrieve sales records associated with the logged-in user
        List<Sales> salesRecords = salesRepository.findByUserId(loggedInUser.getId());

        // Add the sales records, product variations, products, productImageUrls, and productTotalQuantities to the data
        data.put("salesRecords", salesRecords);
        data.put("productVariations", productVariations);
        data.put("products", products);
        data.put("productImageUrls", productImageUrls);
        data.put("productTotalQuantities", productTotalQuantities);

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    /**
     * Returns a message indicating that the add product page has been accessed.
     *
     * @param request The HttpServletRequest object
     * @return Message indicating that the add product page has been accessed.
     */
    @GetMapping("/add-product")
    public ResponseEntity<String> addProduct(HttpServletRequest request) {
        return new ResponseEntity<>("Add product page accessed", HttpStatus.OK);
    }

    /**
     * Handles form submission for adding a new product.
     * Validates the form inputs and processes the addition of the product with variations.
     *
     * @return Message indicating the result of the product addition.
     */
    @PostMapping("add-product")
    public ResponseEntity<String> addProductForm(
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
                // User is not authenticated, return an error message
                return new ResponseEntity<>("User is not authenticated", HttpStatus.UNAUTHORIZED);
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

            return new ResponseEntity<>("Product added successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to add product. Please try again.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Returns a message indicating that the edit product page has been accessed.
     *
     * @param productId The ID of the product to be edited
     * @param request The HttpServletRequest object
     * @return Message indicating that the edit product page has been accessed.
     */
    @GetMapping("edit-product/{productId}")
    public ResponseEntity<String> editProduct(@PathVariable Integer productId, HttpServletRequest request) {
        return new ResponseEntity<>("Edit product page accessed for product ID: " + productId, HttpStatus.OK);
    }

    /**
     * Deletes a product and its associated data.
     * Handles product deletion based on the product ID.
     *
     * @param id The ID of the product to be deleted
     * @param redirectAttributes Redirect attributes for passing messages
     * @param request The HttpServletRequest object
     * @return Message indicating the result of the product deletion.
     */
    @PostMapping("/products/{id}/delete")
    public ResponseEntity<String> deleteProduct(@PathVariable Integer id, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        try {
            // Get the logged-in user from the session
            UserDTO loggedInUser = (UserDTO) request.getSession().getAttribute("loggedInUser");
            if (loggedInUser == null) {
                // User is not authenticated, return an error message
                return new ResponseEntity<>("User is not authenticated", HttpStatus.UNAUTHORIZED);
            }

            // Check if the product belongs to the logged-in user
            Product productToDelete = productService.getProductById(id);
            if (productToDelete == null || !productToDelete.getUser().getId().equals(loggedInUser.getId())) {
                // Product not found or does not belong to the logged-in user, handle accordingly (e.g., show error message)
                return new ResponseEntity<>("You are not authorized to delete this product.", HttpStatus.UNAUTHORIZED);
            }

            // Set the deleted flag to true instead of deleting the product
            productToDelete.setDeleted(true);
            productRepository.save(productToDelete);

            return new ResponseEntity<>("Product deleted successfully", HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            // You may handle exception here more gracefully, such as providing a user-friendly error message or logging the error for further investigation
            e.printStackTrace();
            return new ResponseEntity<>("Failed to delete product. Please try again.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * Edits a product and its associated data.
     * Handles product editing based on the product ID.
     *
     * @param productId The ID of the product to be edited
     * @param updatedProduct The updated product data
     * @return Message indicating the result of the product editing.
     */
    @PostMapping("/edit-product/{productId}")
    public ResponseEntity<String> editProduct(@PathVariable Integer productId,
                                              @ModelAttribute Product updatedProduct) {
        try {
            // Get the existing product from the database
            Product existingProduct = productService.getProductById(productId);
            if (existingProduct == null) {
                // Product not found, return an error message
                return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
            }

            // Update the existing product with the details of the updated product
            existingProduct.setProductName(updatedProduct.getProductName());
            existingProduct.setProductDescription(updatedProduct.getProductDescription());
            existingProduct.setCostPrice(updatedProduct.getCostPrice());
            existingProduct.setSellingPrice(updatedProduct.getSellingPrice());

            // Update the product in the database
            productService.updateProduct(productId, existingProduct);

            return new ResponseEntity<>("Product updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            // Handle any exceptions, such as a database error
            e.printStackTrace();
            return new ResponseEntity<>("Failed to update product. Please try again.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
