package dashboard.IMS.controller;

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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Controller class for handling HTTP requests related to navigation.
 * Responsible for directing users to various pages.
 *
 * @author Amiel De Los Reyes
 * @date 02/20/2024
 */
@Controller
public class HomeController {

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

    public HomeController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Directs users to the index page.
     * Checks if the user is logged in and redirects to the login page if not.
     * Retrieves product and product variation data owned by the logged-in user.
     * Prepares necessary data for display on the index page.
     *
     * @return Name of the index page.
     */
    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public String index(HttpServletRequest request, Model model) {

        // Retrieve the HttpSession
        HttpSession session = request.getSession();

        // Check if a user is logged in based on the session attribute
        if (session.getAttribute("loggedInUser") == null) {
            // User is not authenticated, redirect to the login page
            return "redirect:/login";
        }

        // Check if a user is logged in based on the session attribute
        UserDTO authenticatedUser = (UserDTO) session.getAttribute("loggedInUser");

        // Add the authenticatedUser to the model if needed for the view
        model.addAttribute("authenticatedUser", authenticatedUser);


        System.out.println("Profile Picture Path: " + authenticatedUser.getProfilePicture());

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
        return "index";
    }


    /**
     * Directs users to the 404 error page.
     *
     * @return Name of the 404 error page.
     */
    @GetMapping("/404")
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String notFound() {
        return "404";
    }

    /**
     * Directs users to the blank page.
     *
     * @return Name of the blank page.
     */
    @GetMapping("/blank-page")
    @ResponseStatus(HttpStatus.OK)
    public String blankPage() {
        return "blankpage";
    }

}
