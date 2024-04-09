package dashboard.IMS.entity;

import lombok.*;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    @Getter
    @ManyToOne
    @JoinColumn(name = "product_variation_id", insertable = false, updatable = false)
    private ProductVariation productVariation;

    @Column(name = "quantity_sold")
    private Integer quantitySold = 0;

    @Column(name = "quantity_refunded")
    private Integer quantityRefunded = 0;

    @Column(name = "total_revenue")
    private BigDecimal totalRevenue;

    @Column(name = "total_cost")
    private BigDecimal totalCost;

    @Column(name = "total_profit")
    private BigDecimal totalProfit;

    @Column(name = "transaction_date")
    private LocalDateTime transactionDate;

    @Transient
    private String productName;

    @Transient
    private String productImageUrl;

    @Column(name = "is_refund")
    private boolean isRefund = false;

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public void setProductVariation(ProductVariation productVariation) {
        this.productVariation = productVariation;
    }

    public boolean getIsRefund() {
        return isRefund;
    }

    public void setIsRefund(boolean isRefund) {
        this.isRefund = isRefund;
    }

    public boolean isRefund() {
        return false;
    }
}