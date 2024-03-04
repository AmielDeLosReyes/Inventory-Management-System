package dashboard.IMS.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import dashboard.IMS.dto.ProductVariationDTO;
import dashboard.IMS.entity.Color;
import dashboard.IMS.entity.Product;
import dashboard.IMS.entity.ProductVariation;
import dashboard.IMS.entity.Size;
import dashboard.IMS.repository.ProductVariationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Unit tests for the ProductVariationService class.
 */
public class ProductVariationServiceTest {

    @Mock
    private ProductVariationRepository productVariationRepository;

    @InjectMocks
    private ProductVariationService productVariationService;

    @BeforeEach
    void setUp() {
        // Initialize mocks before each test
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Test case to verify creating a new ProductVariation.
     */
    @Test
    void testCreateProductVariation() {
        // Arrange
        ProductVariationDTO productVariationDTO = new ProductVariationDTO();
        productVariationDTO.setProductId(1);
        productVariationDTO.setColorId(1);
        productVariationDTO.setSizeId(1);
        productVariationDTO.setQuantity(10);

        ProductVariation productVariation = new ProductVariation();
        when(productVariationRepository.save(any())).thenReturn(productVariation);

        // Act
        ProductVariationDTO result = productVariationService.createProductVariation(productVariationDTO);

        // Assert
        assertNotNull(result);
    }

    /**
     * Test case to verify retrieving all ProductVariations from the database.
     */
    @Test
    void testGetAllProductVariations() {
        // Arrange
        List<ProductVariation> productVariationList = new ArrayList<>();
        productVariationList.add(new ProductVariation());
        productVariationList.add(new ProductVariation());
        when(productVariationRepository.findAll()).thenReturn(productVariationList);

        // Act
        List<ProductVariationDTO> result = productVariationService.getAllProductVariations();

        // Assert
        assertEquals(2, result.size());
    }

    /**
     * Test case to verify retrieving a ProductVariation by its ID from the database.
     */
    @Test
    void testGetProductVariationById() {
        // Arrange
        ProductVariation productVariation = new ProductVariation();
        productVariation.setId(1);
        when(productVariationRepository.findById(1)).thenReturn(Optional.of(productVariation));

        // Act
        ProductVariationDTO result = productVariationService.getProductVariationById(1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
    }

    /**
     * Test case to verify updating a ProductVariation in the database.
     */
    @Test
    void testUpdateProductVariation() {
        // Arrange
        ProductVariationDTO productVariationDTO = new ProductVariationDTO();
        productVariationDTO.setId(1);
        ProductVariation productVariation = new ProductVariation();
        productVariation.setId(1);
        when(productVariationRepository.findById(1)).thenReturn(Optional.of(productVariation));
        when(productVariationRepository.save(any())).thenReturn(productVariation);

        // Act
        ProductVariationDTO result = productVariationService.updateProductVariation(1, productVariationDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
    }

    /**
     * Test case to verify deleting a ProductVariation from the database.
     */
    @Test
    void testDeleteProductVariation() {
        // Act
        productVariationService.deleteProductVariation(1);

        // Assert
        verify(productVariationRepository, times(1)).deleteById(1);
    }
}
