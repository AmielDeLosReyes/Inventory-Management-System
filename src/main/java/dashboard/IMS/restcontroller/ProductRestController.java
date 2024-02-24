package dashboard.IMS.restcontroller;

import dashboard.IMS.dto.ProductDTO;
import dashboard.IMS.dto.ProductVariationDTO;
import dashboard.IMS.entity.Product;
import dashboard.IMS.service.ProductService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/product-list")
    public List<Product> getAllProducts() {
        // Retrieve all products from the database using the ProductService
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Integer id) {
        // Retrieve a product by its ID from the database using the ProductService
        Product product = productService.getProductById(id);
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        // Add a new product to the database using the ProductService
        Product addedProduct = productService.addProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedProduct);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer id, @RequestBody Product product) {
        // Update an existing product in the database using the ProductService
        Product updatedProduct = productService.updateProduct(id, product);
        if (updatedProduct != null) {
            return ResponseEntity.ok(updatedProduct);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer id) {
        // Delete a product by its ID from the database using the ProductService
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

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
