package dashboard.IMS.service;

import dashboard.IMS.entity.Sales;
import dashboard.IMS.repository.SalesRepository;
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
 * Unit tests for the SalesService class.
 * These tests validate the behavior of SalesService methods.
 * Mocking is used to isolate the class under test and focus on its logic independently.
 *
 * Author: Amiel De Los Reyes
 * Date: 02/20/2024
 */
public class SalesServiceTest {

    // Inject the SalesService object which is to be tested
    @InjectMocks
    private SalesService salesService;

    // Mock the SalesRepository which is used by the SalesService
    @Mock
    private SalesRepository salesRepository;

    // This method is executed before each test. It initializes the mock objects.
    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    // Test case for the getAllSales method
    @Test
    public void testGetAllSales() {
        // Define the behavior of the mock object
        when(salesRepository.findAll()).thenReturn(Arrays.asList(new Sales(), new Sales()));
        // Assert that the size of the result is as expected
        assertEquals(2, salesService.getAllSales().size());
        // Verify that the findAll method was called exactly once
        verify(salesRepository, times(1)).findAll();
    }

    // Test case for the getSalesById method when the ID is not found
    @Test
    public void testGetSalesByIdNotFound() {
        // Define the behavior of the mock object
        when(salesRepository.findById(1)).thenReturn(Optional.empty());
        // Assert that the result is null
        assertNull(salesService.getSalesById(1));
        // Verify that the findById method was called exactly once
        verify(salesRepository, times(1)).findById(1);
    }

    // Test case for the deleteSales method
    @Test
    public void testDeleteSales() {
        // Call the method to test
        salesService.deleteSales(1);
        // Verify that the deleteById method was called exactly once
        verify(salesRepository, times(1)).deleteById(1);
    }
}