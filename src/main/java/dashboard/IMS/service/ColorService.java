package dashboard.IMS.service;

import dashboard.IMS.entity.Color;
import dashboard.IMS.repository.ColorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for Color entity.
 * Handles business logic related to Color entities.
 *
 * Author: Amiel De Los Reyes
 * Date: 02/20/2024
 */
@Service
public class ColorService {
    private final ColorRepository colorRepository;

    @Autowired
    public ColorService(ColorRepository colorRepository) {
        this.colorRepository = colorRepository;
    }

    /**
     * Saves a color in the database.
     *
     * @param color The color object to save.
     * @return The saved color object.
     */
    public Color saveColor(Color color) {
        return colorRepository.save(color);
    }

    /**
     * Retrieves all colors from the database.
     *
     * @return A list of all colors.
     */
    public List<Color> getAllColors() {
        return colorRepository.findAll();
    }

    /**
     * Retrieves a color by its ID from the database.
     *
     * @param id The ID of the color to retrieve.
     * @return The color object if found, otherwise null.
     */
    public Color getColorById(Integer id) {
        return colorRepository.findById(id).orElse(null);
    }

    /**
     * Deletes a color by its ID from the database.
     *
     * @param id The ID of the color to delete.
     */
    public void deleteColorById(Integer id) {
        colorRepository.deleteById(id);
    }
}
