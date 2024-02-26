package dashboard.IMS.controller;

import dashboard.IMS.dto.ProductVariationDTO;
import dashboard.IMS.entity.Product;
import dashboard.IMS.service.ProductService;
import dashboard.IMS.service.ProductVariationService;
import dashboard.IMS.utilities.FileUploadUtil;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
* Controller class for handling product-related operations
 *
 * Author: Amiel De Los Reyes
 * Date: 02/26/2024
 */
@Controller
@RequestMapping("/product")
public class ProductController {

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
                                 RedirectAttributes redirectAttributes){

        try {
            // Process form data and create DTOs
            Product product = new Product();
            product.setProductName(productName);
            product.setProductDescription(productDescription);
            product.setCostPrice(BigDecimal.valueOf(cost));
            product.setSellingPrice(BigDecimal.valueOf(sellingPrice));

            // Save uploaded images and get their URLs
            List<String> imageUrls = new ArrayList<>();
            for (MultipartFile image : images) {
                if (image != null && !image.isEmpty()) {
                    String filename = StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()));
                    // Save the file to the server
                    String uploadDir = "src/main/resources/images/" + productName; // You might want to change the upload directory
                    FileUploadUtil.saveFile(uploadDir, filename, image);
                    // Get the URL of the saved file
                    String imageUrl = "/images/" + productName + "/" + filename; // Adjust this URL as per your directory structure
                    imageUrls.add(imageUrl);
                }
            }

            // Save Product with image URLs
            product.setImageUrls(String.valueOf(imageUrls));

            // Save Product
            Product savedProduct = productService.addProduct(product);


            // Process form data and create ProductVariationDTO
            ProductVariationDTO productVariationDTO = new ProductVariationDTO();
            productVariationDTO.setProductId(savedProduct.getId());
            productVariationDTO.setSizeId(size);
            productVariationDTO.setColorId(color);
            productVariationDTO.setQuantity(quantity);

            // Save ProductVariation
            productVariationService.createProductVariation(productVariationDTO);

            return "redirect:/products";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to add product. Please try again.");
            return "redirect:/product/add-product";
        }
    }
}
