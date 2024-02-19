package dashboard.IMS.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

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

    @Column(name = "image_urls", columnDefinition = "JSON")
    private String imageUrls;
}
