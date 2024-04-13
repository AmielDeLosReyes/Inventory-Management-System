package dashboard.IMS.service;

import dashboard.IMS.entity.Color;
import dashboard.IMS.repository.ColorRepository;
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
 * Unit tests for the ColorService class.
 * These tests validate the behavior of ColorService methods.
 * Mocking is used to isolate the class under test and focus on its logic independently.
 *
 * Author: Amiel De Los Reyes
 * Date: 02/20/2024
 */
public class ColorServiceTest {

    @InjectMocks
    private ColorService colorService;

    @Mock
    private ColorRepository colorRepository;

    /**
     * Initialize mocks before each test method execution.
     */
    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test case for retrieving all colors.
     * Verifies that the correct number of colors is returned.
     */
    @Test
    public void testGetAllColors() {
        when(colorRepository.findAll()).thenReturn(Arrays.asList(new Color(), new Color()));
        assertEquals(2, colorService.getAllColors().size());
        verify(colorRepository, times(1)).findAll();
    }

    /**
     * Test case for retrieving a color by its ID.
     * Verifies that the correct color is returned when it exists.
     */
    @Test
    public void testGetColorById() {
        Color color = new Color();
        when(colorRepository.findById(1)).thenReturn(Optional.of(color));
        assertEquals(color, colorService.getColorById(1));
        verify(colorRepository, times(1)).findById(1);
    }

    /**
     * Test case for retrieving a color by its ID when it does not exist.
     * Verifies that null is returned when the color is not found.
     */
    @Test
    public void testGetColorByIdNotFound() {
        when(colorRepository.findById(1)).thenReturn(Optional.empty());
        assertNull(colorService.getColorById(1));
        verify(colorRepository, times(1)).findById(1);
    }

    /**
     * Test case for saving a color.
     * Verifies that the saved color is returned.
     */
    @Test
    public void testSaveColor() {
        Color color = new Color();
        when(colorRepository.save(color)).thenReturn(color);
        assertEquals(color, colorService.saveColor(color));
        verify(colorRepository, times(1)).save(color);
    }

    /**
     * Test case for deleting a color by its ID.
     * Verifies that the color deletion is invoked with the correct ID.
     */
    @Test
    public void testDeleteColorById() {
        colorService.deleteColorById(1);
        verify(colorRepository, times(1)).deleteById(1);
    }
}
