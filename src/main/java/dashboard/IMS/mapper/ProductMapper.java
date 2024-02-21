package dashboard.IMS.mapper;

import dashboard.IMS.dto.ProductDTO;
import dashboard.IMS.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

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
