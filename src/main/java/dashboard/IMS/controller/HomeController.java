package dashboard.IMS.controller;

import dashboard.IMS.entity.Product;
import dashboard.IMS.entity.ProductVariation;
import dashboard.IMS.entity.Sales;
import dashboard.IMS.repository.ProductRepository;
import dashboard.IMS.repository.ProductVariationRepository;
import dashboard.IMS.repository.SalesRepository;
import dashboard.IMS.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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

    public HomeController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Directs users to the index page.
     *
     * @return Name of the index page.
     */
    @GetMapping("/")
    public String index(Model model) {

        // Retrieve all product variations from the database
        List<ProductVariation> productVariations = productVariationRepository.findAll();

        List<Product> products = productRepository.findAll();

        List<Object[]> totalQuantities = productVariationRepository.getTotalQuantities();

        // Create a map to store total quantities for each product
        Map<Integer, Integer> productTotalQuantities = new HashMap<>();
        for (Object[] row : totalQuantities) {
            Integer productId = ((Number) row[0]).intValue(); // Cast to Integer
            Integer totalQuantity = ((Number) row[1]).intValue(); // Cast to Integer
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
     * Directs users to the login page.
     *
     * @return Name of the login page.
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * Directs users to the signup page.
     *
     * @return Name of the signup page.
     */
    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    /**
     * Directs users to the add product page.
     *
     * @return Name of the add product page.
     */
    @GetMapping("/add-product")
    public String addProduct() {
        return "addproduct";
    }

    /**
     * Directs users to the sales report page.
     *
     * @return Name of the sales report page.
     */
    @GetMapping("/sales-report")
    public String salesReport(Model model) {
        // Fetch the list of sales from the repository
        List<Sales> salesList = salesRepository.findAll();

        // Create a map to store product image URLs
        Map<Integer, String> productImageUrls = new HashMap<>();

        // For each sale, fetch the corresponding product details
        for (Sales sale : salesList) {
            // Retrieve the product variation using its ID
            ProductVariation productVariation = productVariationRepository.findById(sale.getProductVariationId()).orElse(null);

            if (productVariation != null) {
                // Access the product object associated with the product variation
                Product product = productVariation.getProduct();

                if (product != null) {
                    // Retrieve the image URLs for the product
                    String imageUrls = product.getImageUrls();
                    if (imageUrls != null && !imageUrls.isEmpty()) {
                        // Remove square brackets if present
                        imageUrls = imageUrls.replaceAll("\\[|\\]", "");
                        String[] imageUrlArray = imageUrls.split(","); // Split by comma
                        if (imageUrlArray.length > 0) {
                            String firstImageUrl = imageUrlArray[0].trim(); // Get the first URL
                            // Remove leading backslash if present
                            if (firstImageUrl.startsWith("/")) {
                                firstImageUrl = firstImageUrl.substring(1);
                            }
                            // Store the image URL in the map
                            productImageUrls.put(product.getId(), firstImageUrl);
                            // Set the image URL in the sales object
                            sale.setProductImageUrl(firstImageUrl);
                        }
                    }
                }
            }
        }

        // Add the sales list and product image URLs to the model
        model.addAttribute("salesList", salesList);
        model.addAttribute("productImageUrls", productImageUrls);

        // Return the view name
        return "salesreport";
    }



    /**
     * Directs users to the product list page.
     *
     * @return Name of the product list page.
     */
    @GetMapping("/products")
    public String productList(Model model) {

        // Retrieve all product variations from the database
        List<ProductVariation> productVariations = productVariationRepository.findAll();

        List<Product> products = productRepository.findAll();

        List<Object[]> totalQuantities = productVariationRepository.getTotalQuantities();

        // Create a map to store total quantities for each product
        Map<Integer, Integer> productTotalQuantities = new HashMap<>();
        for (Object[] row : totalQuantities) {
            Integer productId = ((Number) row[0]).intValue(); // Cast to Integer
            Integer totalQuantity = ((Number) row[1]).intValue(); // Cast to Integer
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


        return "products";
    }

    /**
     * Directs users to the 404 error page.
     *
     * @return Name of the 404 error page.
     */
    @GetMapping("/404")
    public String notFound() {
        return "404";
    }

    /**
     * Directs users to the blank page.
     *
     * @return Name of the blank page.
     */
    @GetMapping("/blank-page")
    public String blankPage() {
        return "blankpage";
    }
}
