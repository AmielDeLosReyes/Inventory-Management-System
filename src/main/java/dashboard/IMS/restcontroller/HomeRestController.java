package dashboard.IMS.restcontroller;

import dashboard.IMS.dto.UserDTO;
import dashboard.IMS.entity.Product;
import dashboard.IMS.entity.ProductVariation;
import dashboard.IMS.repository.ProductRepository;
import dashboard.IMS.repository.ProductVariationRepository;
import dashboard.IMS.repository.SalesRepository;
import dashboard.IMS.service.ProductService;
import dashboard.IMS.service.SalesService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * RestController class for handling HTTP requests related to navigation.
 * Responsible for directing users to various pages.
 *
 * @author Amiel De Los Reyes
 * @date 02/20/2024
 */
@RestController
public class HomeRestController {

    @Autowired
    ProductVariationRepository productVariationRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    private final ProductService productService;

    @Autowired
    private SalesRepository salesRepository;

    @Autowired
    private SalesService salesService;

    public HomeRestController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Retrieves product and product variation data owned by the logged-in user.
     * Prepares necessary data for display on the index page.
     *
     * @return Data for the index page.
     */
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> index(HttpServletRequest request) {
        Map<String, Object> data = new HashMap<>();

        // Retrieve the HttpSession
        HttpSession session = request.getSession();

        // Check if a user is logged in based on the session attribute
        if (session.getAttribute("loggedInUser") == null) {
            // User is not authenticated, return an error message
            data.put("error", "User is not authenticated");
            return new ResponseEntity<>(data, HttpStatus.UNAUTHORIZED);
        }

        // Check if a user is logged in based on the session attribute
        UserDTO authenticatedUser = (UserDTO) session.getAttribute("loggedInUser");

        // Add the authenticatedUser to the data
        data.put("authenticatedUser", authenticatedUser);

        // Retrieve products owned by the logged-in user from the database
        List<Product> products = productRepository.findByUserIdAndDeletedFalse(authenticatedUser.getId());

        // Retrieve all product variations owned by the logged-in user from the database
        List<ProductVariation> productVariations = productVariationRepository.findAllByProductUserId(authenticatedUser.getId());

        // Retrieve total quantities of product variations owned by the logged-in user
        List<Object[]> totalQuantities = productVariationRepository.getTotalQuantitiesByProductUserId(authenticatedUser.getId());

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

        // Add the product variations, products, productImageUrls, and productTotalQuantities to the data
        data.put("productVariations", productVariations);
        data.put("products", products);
        data.put("productImageUrls", productImageUrls);
        data.put("productTotalQuantities", productTotalQuantities);

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    /**
     * Returns a 404 error message.
     *
     * @return 404 error message.
     */
    @GetMapping("/404")
    public ResponseEntity<String> notFound() {
        return new ResponseEntity<>("404 error", HttpStatus.NOT_FOUND);
    }

    /**
     * Returns a blank message.
     *
     * @return Blank message.
     */
    @GetMapping("/blank-page")
    public ResponseEntity<String> blankPage() {
        return new ResponseEntity<>("This is a blank page", HttpStatus.OK);
    }
}