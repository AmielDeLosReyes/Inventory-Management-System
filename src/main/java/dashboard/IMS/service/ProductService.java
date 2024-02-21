package dashboard.IMS.service;

import dashboard.IMS.dto.ProductDTO;
import dashboard.IMS.dto.ProductVariationDTO;
import dashboard.IMS.entity.Product;
import dashboard.IMS.mapper.ProductMapper;
import dashboard.IMS.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

}
