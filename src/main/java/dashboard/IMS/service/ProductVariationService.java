package dashboard.IMS.service;

import dashboard.IMS.dto.ProductVariationDTO;
import dashboard.IMS.entity.ProductVariation;
import dashboard.IMS.repository.ProductVariationRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductVariationService {

    private final ProductVariationRepository productVariationRepository;

    public ProductVariationService(ProductVariationRepository productVariationRepository) {
        this.productVariationRepository = productVariationRepository;
    }

    public ProductVariationDTO createProductVariation(ProductVariationDTO productVariationDTO) {
        ProductVariation ProductVariation = new ProductVariation();
        BeanUtils.copyProperties(productVariationDTO, ProductVariation);
        ProductVariation savedEntity = productVariationRepository.save(ProductVariation);
        return toDTO(savedEntity);
    }

    public List<ProductVariationDTO> getAllProductVariations() {
        List<ProductVariation> productVariationEntities = productVariationRepository.findAll();
        return productVariationEntities.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public ProductVariationDTO getProductVariationById(Integer id) {
        Optional<ProductVariation> productVariationOptional = productVariationRepository.findById(id);
        return productVariationOptional.map(this::toDTO).orElse(null);
    }

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

    public void deleteProductVariation(Integer id) {
        productVariationRepository.deleteById(id);
    }

    private ProductVariationDTO toDTO(ProductVariation entity) {
        ProductVariationDTO dto = new ProductVariationDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
}

