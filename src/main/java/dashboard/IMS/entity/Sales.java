package dashboard.IMS.entity;

import lombok.*;

import jakarta.persistence.*;
import java.math.BigDecimal;

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
}
