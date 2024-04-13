package dashboard.IMS.service;

import dashboard.IMS.dto.ProductVariationDTO;
import dashboard.IMS.entity.Color;
import dashboard.IMS.entity.Product;
import dashboard.IMS.entity.ProductVariation;
import dashboard.IMS.entity.Size;
import dashboard.IMS.repository.ProductVariationRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for ProductVariation entity.
 * Handles business logic related to ProductVariation entities.
 *
 * Author: Amiel De Los Reyes
 * Date: 02/20/2024
 */
@Service
public class ProductVariationService {

    private final ProductVariationRepository productVariationRepository;

    public ProductVariationService(ProductVariationRepository productVariationRepository) {
        this.productVariationRepository = productVariationRepository;
    }

    /**
     * Creates a new ProductVariation.
     *
     * @param productVariationDTO The DTO representing the ProductVariation to be created.
     * @return The created ProductVariation DTO.
     */
    public ProductVariationDTO createProductVariation(ProductVariationDTO productVariationDTO) {
        ProductVariation productVariation = new ProductVariation();

        // Set product ID
        if (productVariationDTO.getProductId() != null) {
            Product product = new Product();
            product.setId(productVariationDTO.getProductId());
            productVariation.setProduct(product);
        }

        // Set color ID
        Color color = new Color();
        color.setId(productVariationDTO.getColorId());
        productVariation.setColor(color);

        // Set size ID
        Size size = new Size();
        size.setId(productVariationDTO.getSizeId());
        productVariation.setSize(size);

        // Set quantity
        productVariation.setQuantity(productVariationDTO.getQuantity());


        productVariation.setUserId(productVariationDTO.getUserId());

        ProductVariation savedEntity = productVariationRepository.save(productVariation);
        return toDTO(savedEntity);
    }


    /**
     * Retrieves all ProductVariations.
     *
     * @return A list of all ProductVariation DTOs.
     */
    public List<ProductVariationDTO> getAllProductVariations() {
        List<ProductVariation> productVariationEntities = productVariationRepository.findAll();
        return productVariationEntities.stream().map(this::toDTO).collect(Collectors.toList());
    }

    /**
     * Retrieves a ProductVariation by its ID.
     *
     * @param id The ID of the ProductVariation to retrieve.
     * @return The ProductVariation DTO if found, otherwise null.
     */
    public ProductVariationDTO getProductVariationById(Integer id) {
        Optional<ProductVariation> productVariationOptional = productVariationRepository.findById(id);
        return productVariationOptional.map(this::toDTO).orElse(null);
    }

    /**
     * Updates a ProductVariation.
     *
     * @param id                The ID of the ProductVariation to update.
     * @param productVariationDTO The DTO representing the updated ProductVariation.
     * @return The updated ProductVariation DTO if successful, otherwise null.
     */
    public ProductVariationDTO updateProductVariation(Integer id, ProductVariationDTO productVariationDTO) {
        Optional<ProductVariation> productVariationOptional = productVariationRepository.findById(id);
        if (productVariationOptional.isPresent()) {
            ProductVariation existingEntity = productVariationOptional.get();
            BeanUtils.copyProperties(productVariationDTO, existingEntity);
            ProductVariation updatedEntity = productVariationRepository.save(existingEntity);
            return toDTO(updatedEntity);
        }
        return null; // Or throw an exception indicating the entity was not found
    }

    /**
     * Deletes a ProductVariation by its ID.
     *
     * @param id The ID of the ProductVariation to delete.
     */
    public void deleteProductVariation(Integer id) {
        productVariationRepository.deleteById(id);
    }

    /**
     * Converts a ProductVariation entity to a DTO.
     *
     * @param entity The ProductVariation entity.
     * @return The corresponding ProductVariation DTO.
     */
    private ProductVariationDTO toDTO(ProductVariation entity) {
        ProductVariationDTO dto = new ProductVariationDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    public boolean existsProductVariation(Integer productId) {
        // Assuming you have a method to fetch product variations by product ID
        List<ProductVariation> variations = productVariationRepository.findByProductId(productId);
        return !variations.isEmpty();
    }

}
