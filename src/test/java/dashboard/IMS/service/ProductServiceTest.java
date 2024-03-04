/**
 * This class contains unit tests for the ProductService class.
 *
 *  Author: Amiel De Los Reyes
 *  Date: 03/04/2024
 */
package dashboard.IMS.service;

import dashboard.IMS.dto.ProductDTO;
import dashboard.IMS.dto.ProductVariationDTO;
import dashboard.IMS.entity.Product;
import dashboard.IMS.mapper.ProductMapper;
import dashboard.IMS.repository.ProductRepository;
import dashboard.IMS.service.ProductService;
import dashboard.IMS.service.ProductVariationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the ProductService class.
 *
 * Author: Amiel De Los Reyes
 * Date: 02/21/2024
 */
class ProductServiceTest {

    // Mocking dependencies
    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductVariationService productVariationService;

    @Mock
    private ProductMapper productMapper;

    // Class under test
    @InjectMocks
    private ProductService productService;

    // Method to initialize mocks
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Test case to verify the behavior of adding a product with variations.
     */
    @Test
    void testAddProductWithVariations() {
        // Create a sample product DTO
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductName("T-Shirt");
        productDTO.setProductDescription("Comfortable cotton T-shirt");
        productDTO.setCostPrice(BigDecimal.valueOf(15.99));
        productDTO.setSellingPrice(BigDecimal.valueOf(25.99));
        productDTO.setImageUrls("https://example.com/image1.jpg,https://example.com/image2.jpg");

        // Create sample variation DTOs
        List<ProductVariationDTO> variations = new ArrayList<>();
        ProductVariationDTO variationDTO1 = new ProductVariationDTO();
        variationDTO1.setColorId(3);
        variationDTO1.setSizeId(3);
        variationDTO1.setQuantity(50);
        variations.add(variationDTO1);

        ProductVariationDTO variationDTO2 = new ProductVariationDTO();
        variationDTO2.setColorId(1);
        variationDTO2.setSizeId(4);
        variationDTO2.setQuantity(30);
        variations.add(variationDTO2);

        // Mocking the repository and service methods
        when(productRepository.findByProductName("T-Shirt")).thenReturn(null);
        when(productMapper.toEntity(productDTO)).thenReturn(new Product());
        when(productRepository.save(any())).thenReturn(new Product());
        when(productVariationService.createProductVariation(any())).thenReturn(new ProductVariationDTO()); // Uncommented this line

        // Calling the method to be tested
        productService.addProductWithVariations(productDTO, variations);

        // Verifying that the save methods were called properly
        verify(productRepository, times(1)).save(any());
        verify(productVariationService, times(2)).createProductVariation(any());
    }


    /**
     * Test case to verify retrieving all products from the database.
     */
    @Test
    void testGetAllProducts() {
        // Arrange
        List<Product> productList = new ArrayList<>();
        productList.add(new Product());
        productList.add(new Product());
        when(productRepository.findAll()).thenReturn(productList);

        // Act
        List<Product> result = productService.getAllProducts();

        // Assert
        assertEquals(2, result.size());
    }

    /**
     * Test case to verify retrieving a product by its ID from the database.
     */
    @Test
    void testGetProductById() {
        // Arrange
        Product product = new Product();
        product.setId(1);
        when(productRepository.findById(1)).thenReturn(java.util.Optional.of(product));

        // Act
        Product result = productService.getProductById(1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
    }

    /**
     * Test case to verify adding a product to the database.
     */
    @Test
    void testAddProduct() {
        // Arrange
        Product product = new Product();
        when(productRepository.save(any())).thenReturn(product);

        // Act
        Product result = productService.addProduct(product);

        // Assert
        assertNotNull(result);
    }

    /**
     * Test case to verify updating an existing product in the database.
     */
    @Test
    void testUpdateProduct() {
        // Arrange
        Product product = new Product();
        product.setId(1);
        when(productRepository.existsById(1)).thenReturn(true);
        when(productRepository.save(product)).thenReturn(product);

        // Act
        Product result = productService.updateProduct(1, product);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
    }

    /**
     * Test case to verify deleting a product from the database.
     */
    @Test
    void testDeleteProduct() {
        // Act
        productService.deleteProduct(1);

        // Assert
        verify(productRepository, times(1)).deleteById(1);
    }
}
