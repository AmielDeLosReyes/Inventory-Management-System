package dashboard.IMS.service;

import dashboard.IMS.dto.ProductDTO;
import dashboard.IMS.dto.ProductVariationDTO;
import dashboard.IMS.entity.Product;
import dashboard.IMS.mapper.ProductMapper;
import dashboard.IMS.repository.ProductRepository;
import dashboard.IMS.utilities.FileUploadUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Service class for Product entity.
 * Handles business logic related to Product entities.
 *
 * Author: Amiel De Los Reyes
 * Date: 02/20/2024
 */
@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductVariationService productVariationService;

    @Autowired
    private ProductMapper productMapper;

    /**
     * Retrieves all products from the database.
     *
     * @return A list of all products.
     */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Retrieves a product by its ID from the database.
     *
     * @param id The ID of the product to retrieve.
     * @return The product object if found, otherwise null.
     */
    public Product getProductById(Integer id) {
        return productRepository.findById(id).orElse(null);
    }

    /**
     * Adds a new product to the database.
     *
     * @param product The product object to add.
     * @return The added product object.
     */
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    /**
     * Updates an existing product in the database.
     *
     * @param id      The ID of the product to update.
     * @param product The updated product object.
     * @return The updated product object if successful, otherwise null.
     */
    public Product updateProduct(Integer id, Product product) {
        if (productRepository.existsById(id)) {
            product.setId(id);
            return productRepository.save(product);
        } else {
            return null;
        }
    }

    /**
     * Deletes a product by its ID from the database.
     *
     * @param id The ID of the product to delete.
     */
    public void deleteProduct(Integer id) {
        productRepository.deleteById(id);
    }

    public void addProductWithVariations(ProductDTO productDTO, List<ProductVariationDTO> variations) {
        // Map ProductDTO to Product entity
        Product product = productMapper.toEntity(productDTO);

        // Save product and get its ID
        Product savedProduct = productRepository.save(product);
        Integer productId = savedProduct.getId();

        // Iterate over variations and set productId for each variation
        for (ProductVariationDTO variationDTO : variations) {
            variationDTO.setProductId(productId);
            // Save each variation
            productVariationService.createProductVariation(variationDTO);
        }
    }

    /**
     * Adds a new product with variations to the database.
     *
     * @param productName       The name of the product.
     * @param productDescription The description of the product.
     * @param images            The array of images representing the product.
     * @param size              The size of the product variation.
     * @param color             The color of the product variation.
     * @param quantity          The quantity of the product variation.
     * @param cost              The cost price of the product.
     * @param sellingPrice      The selling price of the product.
     * @return The added product object.
     */
    @Transactional
    public Product addProductWithVariationsFromFormData(String productName, String productDescription, MultipartFile[] images, int size, int color, int quantity, double cost, double sellingPrice) {
        try {
            // Process form data and create Product entity
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
                    String uploadDir = "src/main/resources/static/images/" + productName;
                    FileUploadUtil.saveFile(uploadDir, filename, image);
                    // Get the URL of the saved file
                    String imageUrl = "/images/" + productName + "/" + filename;
                    imageUrls.add(imageUrl);
                }
            }

            // Save Product with image URLs
            product.setImageUrls(String.valueOf(imageUrls));

            // Save Product
            Product savedProduct = productRepository.save(product);

            // Create ProductVariationDTO
            ProductVariationDTO productVariationDTO = new ProductVariationDTO();
            productVariationDTO.setProductId(savedProduct.getId());
            productVariationDTO.setSizeId(size);
            productVariationDTO.setColorId(color);
            productVariationDTO.setQuantity(quantity);

            // Save ProductVariation
            productVariationService.createProductVariation(productVariationDTO);

            return savedProduct;
        } catch (Exception e) {
            e.printStackTrace();
            // You may handle exception here
            return null;
        }
    }

}
