package dashboard.IMS.restcontroller;

import dashboard.IMS.entity.Color;
import dashboard.IMS.service.ColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller class for handling color-related endpoints for products.
 *
 * Author: Amiel De Los Reyes
 * Date: 02/25/2024
 */
@RestController
@RequestMapping("/product/colors")
public class ProductColorRestController {

    @Autowired
    private ColorService colorService;

    /**
     * Endpoint to retrieve all colors.
     *
     * @return List of all colors retrieved from the database.
     */
    @GetMapping
    public List<Color> getAllColors() {
        // Retrieve all colors from the database using the ColorService
        return colorService.getAllColors();
    }

    /**
     * Endpoint to retrieve a color by its ID.
     *
     * @param id The ID of the color to retrieve.
     * @return The color retrieved by its ID.
     */
    @GetMapping("/{id}")
    public Color getColorById(@PathVariable Integer id) {
        // Retrieve a color by its ID from the database using the ColorService
        return colorService.getColorById(id);
    }

    /**
     * Endpoint to create a new color.
     *
     * @param color The color to be created.
     * @return The created color.
     */
    @PostMapping
    public Color createColor(@RequestBody Color color) {
        // Save the provided color in the database using the ColorService
        return colorService.saveColor(color);
    }

    /**
     * Endpoint to update an existing color.
     *
     * @param id           The ID of the color to update.
     * @param colorDetails The updated details of the color.
     * @return The updated color.
     */
    @PutMapping("/{id}")
    public Color updateColor(@PathVariable Integer id, @RequestBody Color colorDetails) {
        // Retrieve the existing color by ID
        Color existingColor = colorService.getColorById(id);
        if (existingColor == null) {
            // If color with the given ID doesn't exist, return null or handle the error accordingly
            return null;
        }
        // Update the existing color details
        existingColor.setName(colorDetails.getName());
        // Save the updated color in the database using the ColorService
        return colorService.saveColor(existingColor);
    }

    /**
     * Endpoint to delete a color by its ID.
     *
     * @param id The ID of the color to delete.
     */
    @DeleteMapping("/{id}")
    public void deleteColorById(@PathVariable Integer id) {
        // Delete the color by its ID from the database using the ColorService
        colorService.deleteColorById(id);
    }
}
