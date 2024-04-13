package dashboard.IMS.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Entity class for Product.
 * Represents product data stored in the database.
 *
 * @author Amiel De Los Reyes
 * @date 02/20/2024
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "product")

public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "product_desc")
    private String productDescription;

    @Column(name = "cost_price", precision = 10, scale = 2)
    private BigDecimal costPrice;

    @Column(name = "selling_price", precision = 10, scale = 2)
    private BigDecimal sellingPrice;

    @Column(name = "image_urls")
    private String imageUrls;

    // Bidirectional one-to-many relationship with ProductVariation
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductVariation> productVariations;

    // Setter for productVariations
    public void setProductVariations(List<ProductVariation> productVariations) {
        this.productVariations = productVariations;
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Add a deleted flag
    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;


}
