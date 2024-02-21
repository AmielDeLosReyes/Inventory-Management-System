package dashboard.IMS.mapper;

import dashboard.IMS.dto.ProductDTO;
import dashboard.IMS.entity.Product;
import org.springframework.stereotype.Component;

/**
 * Mapper class responsible for converting between Product and ProductDTO objects.
 *
 * Author: Amiel De Los Reyes
 * Date: 02/22/2024
 */
@Component
public class ProductMapper {

    /**
     * Converts a ProductDTO object to a Product entity.
     *
     * @param productDTO The ProductDTO object to convert.
     * @return The corresponding Product entity.
     */
    public Product toEntity(ProductDTO productDTO) {
        Product product = new Product();
        product.setId(productDTO.getId());
        product.setProductName(productDTO.getProductName());
        product.setProductDescription(productDTO.getProductDescription());
        product.setCostPrice(productDTO.getCostPrice());
        product.setSellingPrice(productDTO.getSellingPrice());
        product.setImageUrls(productDTO.getImageUrls());
        return product;
    }

    /**
     * Converts a Product entity to a ProductDTO object.
     *
     * @param product The Product entity to convert.
     * @return The corresponding ProductDTO object.
     */
    public ProductDTO toDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setProductName(product.getProductName());
        productDTO.setProductDescription(product.getProductDescription());
        productDTO.setCostPrice(product.getCostPrice());
        productDTO.setSellingPrice(product.getSellingPrice());
        productDTO.setImageUrls(product.getImageUrls());
        return productDTO;
    }
}
