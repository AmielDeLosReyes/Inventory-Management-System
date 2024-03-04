package dashboard.IMS.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import dashboard.IMS.dto.SalesDTO;
import dashboard.IMS.entity.Sales;
import dashboard.IMS.repository.SalesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

/**
 * Unit tests for the SalesService class.
 */
public class SalesServiceTest {

    @Mock
    private SalesRepository salesRepository;

    @InjectMocks
    private SalesService salesService;

    @BeforeEach
    void setUp() {
        // Initialize mocks before each test
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Test case to verify creating a new Sales record.
     */
    @Test
    void testCreateSales() {
        // Arrange
        SalesDTO salesDTO = new SalesDTO();
        Sales sales = new Sales();
        when(salesRepository.save(any())).thenReturn(sales);

        // Act
        SalesDTO result = salesService.createSales(salesDTO);

        // Assert
        assertNotNull(result);
    }

    /**
     * Test case to verify retrieving all Sales records.
     */
    @Test
    void testGetAllSales() {
        // Arrange
        List<Sales> salesList = new ArrayList<>();
        salesList.add(new Sales());
        salesList.add(new Sales());
        when(salesRepository.findAll()).thenReturn(salesList);

        // Act
        List<SalesDTO> result = salesService.getAllSales();

        // Assert
        assertEquals(2, result.size());
    }

    /**
     * Test case to verify retrieving a Sales record by its ID.
     */
    @Test
    void testGetSalesById() {
        // Arrange
        Sales sales = new Sales();
        sales.setId(1);
        when(salesRepository.findById(1)).thenReturn(Optional.of(sales));

        // Act
        SalesDTO result = salesService.getSalesById(1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
    }

    /**
     * Test case to verify updating a Sales record.
     */
//    @Test
//    void testUpdateSales() {
//        // Arrange
//        // Create a sample Sales entity
//        Sales sales = new Sales();
//        sales.setId(1);
//
//        // Mock the behavior of the repository to return the sample entity when findById is called
//        when(salesRepository.findById(1)).thenReturn(Optional.of(sales));
//
//        // Create a sample SalesDTO object with some data
//        SalesDTO salesDTO = SalesDTO.builder()
//                .id(1)  // Set the ID to match the existing sales record
//                .productVariationId(2)  // Set some example values for productVariationId, quantitySold, totalRevenue, totalCost, and totalProfit
//                .quantitySold(5)
//                .totalRevenue(new BigDecimal("100.00"))
//                .totalCost(new BigDecimal("50.00"))
//                .totalProfit(new BigDecimal("50.00"))
//                .build();
//
//        // Act
//        SalesDTO result = salesService.updateSales(1, salesDTO);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(1, result.getId());
//        // Add more assertions based on the expected behavior after updating the Sales record
//        assertEquals(2, result.getProductVariationId());
//        assertEquals(5, result.getQuantitySold());
//        assertEquals(new BigDecimal("100.00"), result.getTotalRevenue());
//        assertEquals(new BigDecimal("50.00"), result.getTotalCost());
//        assertEquals(new BigDecimal("50.00"), result.getTotalProfit());
//    }

    /**
     * Test case to verify deleting a Sales record by its ID.
     */
    @Test
    void testDeleteSales() {
        // Act
        salesService.deleteSales(1);

        // Assert
        verify(salesRepository, times(1)).deleteById(1);
    }
}
