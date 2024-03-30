package dashboard.IMS.entity;

import lombok.*;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime; // Import LocalDateTime for the transaction date


/**
 * Entity class for Sales.
 * Represents sales data stored in the database.
 *
 * @author Amiel De Los Reyes
 * @date 02/20/2024
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "sales")
public class Sales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "product_variation_id")
    private Integer productVariationId;

    @Column(name = "quantity_sold")
    private Integer quantitySold;

    @Column(name = "total_revenue")
    private BigDecimal totalRevenue;

    @Column(name = "total_cost")
    private BigDecimal totalCost;

    @Column(name = "total_profit")
    private BigDecimal totalProfit;

    @Column(name = "transaction_date") // Add column for transaction date
    private LocalDateTime transactionDate;

    @Transient
    private String productName; // Add product name field

    @Transient
    private String productImageUrl; // Add product image URL field

    // Add setter method for product name
    public void setProductName(String productName) {
        this.productName = productName;
    }

    // Add setter method for product image URL
    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
