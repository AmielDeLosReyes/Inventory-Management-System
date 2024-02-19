package dashboard.IMS.service;

import dashboard.IMS.entity.Size;
import dashboard.IMS.repository.SizeRepository;
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

    // Add other service methods as needed
}
