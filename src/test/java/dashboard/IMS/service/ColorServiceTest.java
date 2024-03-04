package dashboard.IMS.service;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import dashboard.IMS.entity.Color;
import dashboard.IMS.repository.ColorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for the ColorService class.
 *
 * Author: Amiel De Los Reyes
 * Date: 03/04/2024
 */
public class ColorServiceTest {

    @Mock
    private ColorRepository colorRepository;

    @InjectMocks
    private ColorService colorService;

    @BeforeEach
    void setUp() {
        // Initialize mocks before each test
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Test case to verify saving a color in the database.
     */
    @Test
    void testSaveColor() {
        // Arrange
        Color color = new Color();
        when(colorRepository.save(any())).thenReturn(color);

        // Act
        Color result = colorService.saveColor(color);

        // Assert
        assertNotNull(result);
    }

    /**
     * Test case to verify retrieving all colors from the database.
     */
    @Test
    void testGetAllColors() {
        // Arrange
        List<Color> colorList = new ArrayList<>();
        colorList.add(new Color());
        colorList.add(new Color());
        when(colorRepository.findAll()).thenReturn(colorList);

        // Act
        List<Color> result = colorService.getAllColors();

        // Assert
        assertEquals(2, result.size());
    }

    /**
     * Test case to verify retrieving a color by its ID from the database.
     */
    @Test
    void testGetColorById() {
        // Arrange
        Color color = new Color();
        color.setId(1);
        when(colorRepository.findById(1)).thenReturn(java.util.Optional.of(color));

        // Act
        Color result = colorService.getColorById(1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
    }

    /**
     * Test case to verify deleting a color by its ID from the database.
     */
    @Test
    void testDeleteColorById() {
        // Act
        colorService.deleteColorById(1);

        // Assert
        verify(colorRepository, times(1)).deleteById(1);
    }
}

