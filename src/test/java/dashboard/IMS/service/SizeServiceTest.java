package dashboard.IMS.service;

import dashboard.IMS.entity.Size;
import dashboard.IMS.repository.SizeRepository;
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
 * Unit tests for the SizeService class.
 * These tests validate the behavior of SizeService methods.
 * Mocking is used to isolate the class under test and focus on its logic independently.
 *
 * Author: Amiel De Los Reyes
 * Date: 02/20/2024
 */
public class SizeServiceTest {

    // Inject the SizeService object which is to be tested
    @InjectMocks
    private SizeService sizeService;

    // Mock the SizeRepository which is used by the SizeService
    @Mock
    private SizeRepository sizeRepository;

    // This method is executed before each test. It initializes the mock objects.
    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    // Test case for the getAllSizes method
    @Test
    public void testGetAllSizes() {
        // Define the behavior of the mock object
        when(sizeRepository.findAll()).thenReturn(Arrays.asList(new Size(), new Size()));
        // Assert that the size of the result is as expected
        assertEquals(2, sizeService.getAllSizes().size());
        // Verify that the findAll method was called exactly once
        verify(sizeRepository, times(1)).findAll();
    }

    // Test case for the getSizeById method when the ID is not found
    @Test
    public void testGetSizeByIdNotFound() {
        // Define the behavior of the mock object
        when(sizeRepository.findById(1)).thenReturn(Optional.empty());
        // Assert that the result is null
        assertNull(sizeService.getSizeById(1));
        // Verify that the findById method was called exactly once
        verify(sizeRepository, times(1)).findById(1);
    }

    // Test case for the saveSize method
    @Test
    public void testSaveSize() {
        Size size = new Size();
        // Define the behavior of the mock object
        when(sizeRepository.save(size)).thenReturn(size);
        // Assert that the result is as expected
        assertEquals(size, sizeService.saveSize(size));
        // Verify that the save method was called exactly once
        verify(sizeRepository, times(1)).save(size);
    }

    // Test case for the updateSize method
    @Test
    public void testUpdateSize() {
        Size newSize = new Size();
        Size existingSize = new Size();
        // Define the behavior of the mock object
        when(sizeRepository.findById(1)).thenReturn(Optional.of(existingSize));
        when(sizeRepository.save(existingSize)).thenReturn(newSize);
        // Assert that the result is as expected
        assertEquals(newSize, sizeService.updateSize(1, newSize));
        // Verify that the findById and save methods were each called exactly once
        verify(sizeRepository, times(1)).findById(1);
        verify(sizeRepository, times(1)).save(existingSize);
    }

    // Test case for the deleteSizeById method
    @Test
    public void testDeleteSizeById() {
        // Call the method to test
        sizeService.deleteSizeById(1);
        // Verify that the deleteById method was called exactly once
        verify(sizeRepository, times(1)).deleteById(1);
    }
}