package dashboard.IMS.restcontroller;

import dashboard.IMS.dto.ProductDTO;
import dashboard.IMS.dto.ProductVariationDTO;
import dashboard.IMS.service.ProductService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller class for handling product-related endpoints.
 *
 * Author: Amiel De Los Reyes
 * Date: 02/21/2024
 */
@RestController
@RequestMapping("/products")
public class ProductRestController {

    @Autowired
    private ProductService productService;

    @PostMapping("/add-with-variations")
    public ResponseEntity<?> addProductWithVariations(@RequestBody ProductWithVariationsRequest request) {
        try {
            productService.addProductWithVariations(request.getProductDTO(), request.getVariationsDTO());
            return ResponseEntity.ok("Product added successfully with variations.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add product with variations: " + e.getMessage());
        }
    }

    @Getter
    public static class ProductWithVariationsRequest {
        private ProductDTO productDTO;
        private List<ProductVariationDTO> variationsDTO;

        public void setProductDTO(ProductDTO productDTO) {
            this.productDTO = productDTO;
        }

        public void setVariationsDTO(List<ProductVariationDTO> variationsDTO) {
            this.variationsDTO = variationsDTO;
        }
    }
}
