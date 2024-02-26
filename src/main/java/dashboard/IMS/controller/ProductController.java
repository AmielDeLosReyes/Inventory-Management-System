package dashboard.IMS.controller;

import dashboard.IMS.dto.ProductDTO;
import dashboard.IMS.dto.ProductVariationDTO;
import dashboard.IMS.entity.Product;
import dashboard.IMS.entity.ProductVariation;
import dashboard.IMS.service.ProductService;
import dashboard.IMS.service.ProductVariationService;
import dashboard.IMS.utilities.FileUploadUtil;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import java.io.File;


@Controller
@RequestMapping("/product")
public class ProductController {

    @Value("${upload.dir}")
    private String uploadDir;

    private final ProductService productService;
    private final ProductVariationService productVariationService;

    public ProductController(ProductService productService, ProductVariationService productVariationService) {
        this.productService = productService;
        this.productVariationService = productVariationService;
    }

    @PostMapping("add-product")
    public String addProductForm(@RequestParam("productName") String productName,
                                 @RequestParam("productDescription") String productDescription,
                                 @RequestParam("images") MultipartFile images,
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
            if(!images.isEmpty()){
                String filename = StringUtils.cleanPath(images.getOriginalFilename());
                product.setImageUrls(filename);
                String upload = "src/main/resources/images/" + product.getProductName();

                FileUploadUtil.saveFile(upload, filename, images);

            }else {
                if(product.getImageUrls().isEmpty()) {

                }else {
                    product.setImageUrls(null);
                }
            }

            // Save Product
            Product savedProduct = productService.addProduct(product);

            // Convert list of image URLs to comma-separated string
//            String imagesString = String.join(",", imageUrls);
//            product.setImageUrls(imagesString);

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
