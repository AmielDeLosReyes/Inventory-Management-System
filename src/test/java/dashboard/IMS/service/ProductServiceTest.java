package dashboard.IMS.service;


import dashboard.IMS.entity.Product;
import dashboard.IMS.repository.ProductRepository;
import dashboard.IMS.mapper.ProductMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the ProductService class.
 * These tests validate the behavior of ProductService methods.
 * Mocking is used to isolate the class under test and focus on its logic independently.
 *
 * Author: Amiel De Los Reyes
 * Date: 02/20/2024
 */
public class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    /**
     * Initialize mocks before each test method execution.
     */
    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test case for retrieving all products.
     * Verifies that the correct number of products is returned.
     */
    @Test
    public void testGetAllProducts() {
        when(productRepository.findAll()).thenReturn(Arrays.asList(new Product(), new Product()));
        assertEquals(2, productService.getAllProducts().size());
        verify(productRepository, times(1)).findAll();
    }

    /**
     * Test case for retrieving a product by its ID.
     * Verifies that the correct product is returned when it exists.
     */
    @Test
    public void testGetProductById() {
        Product product = new Product();
        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        assertEquals(product, productService.getProductById(1));
        verify(productRepository, times(1)).findById(1);
    }

    /**
     * Test case for retrieving a product by its ID when it does not exist.
     * Verifies that null is returned when the product is not found.
     */
    @Test
    public void testGetProductByIdNotFound() {
        when(productRepository.findById(1)).thenReturn(Optional.empty());
        assertNull(productService.getProductById(1));
        verify(productRepository, times(1)).findById(1);
    }

    /**
     * Test case for adding a product.
     * Verifies that the added product is returned.
     */
    @Test
    public void testAddProduct() {
        Product product = new Product();
        when(productRepository.save(product)).thenReturn(product);
        assertEquals(product, productService.addProduct(product));
        verify(productRepository, times(1)).save(product);
    }

    /**
     * Test case for deleting a product.
     * Verifies that the product deletion is invoked with the correct ID.
     */
    @Test
    public void testDeleteProduct() {
        productService.deleteProduct(1);
        verify(productRepository, times(1)).deleteById(1);
    }
}