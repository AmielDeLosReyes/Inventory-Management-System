package dashboard.IMS.service;

import dashboard.IMS.entity.Size;
import dashboard.IMS.repository.SizeRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for Size entity.
 * Handles business logic related to Size entities.
 *
 * Author: Amiel De Los Reyes
 * Date: 02/20/2024
 */
@Service
public class SizeService {

    private final SizeRepository sizeRepository;

    @Autowired
    public SizeService(SizeRepository sizeRepository) {
        this.sizeRepository = sizeRepository;
    }

    /**
     * Retrieves all Sizes from the database.
     *
     * @return A list of all Sizes.
     */
    public List<Size> getAllSizes() {
        return sizeRepository.findAll();
    }

    /**
     * Retrieves a Size by its ID from the database.
     *
     * @param id The ID of the Size to retrieve.
     * @return The Size object if found, otherwise null.
     */
    public Size getSizeById(Integer id) {
        return sizeRepository.findById(id).orElse(null);
    }

    /**
     * Saves a new Size record to the database.
     *
     * @param size The Size object to save.
     * @return The saved Size object.
     */
    public Size saveSize(Size size) {
        return sizeRepository.save(size);
    }

    /**
     * Updates an existing Size record in the database.
     *
     * @param id      The ID of the Size record to update.
     * @param newSize The updated Size object.
     * @return The updated Size object.
     */
    public Size updateSize(Integer id, Size newSize) {
        Size existingSize = sizeRepository.findById(id).orElse(null);
        if (existingSize != null) {
            BeanUtils.copyProperties(newSize, existingSize, "id");
            return sizeRepository.save(existingSize);
        }
        return null; // or throw exception indicating Size not found
    }

    /**
     * Deletes a Size record from the database by its ID.
     *
     * @param id The ID of the Size record to delete.
     */
    public void deleteSizeById(Integer id) {
        sizeRepository.deleteById(id);
    }
}
