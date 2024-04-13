package dashboard.IMS.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Data Transfer Object (DTO) class for Sales.
 * Represents sales data to be transferred between layers of the application.
 *
 * @author Amiel De Los Reyes
 * @date 02/20/2024
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesDTO {

    private Integer id;
    private Integer productVariationId;
    private Integer quantitySold;
    private BigDecimal totalRevenue;
    private BigDecimal totalCost;
    private BigDecimal totalProfit;
    private Integer userId; // Add userId field to ProductDTO

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SalesDTO salesDTO = (SalesDTO) o;
        return Objects.equals(id, salesDTO.id) &&
                Objects.equals(productVariationId, salesDTO.productVariationId) &&
                Objects.equals(quantitySold, salesDTO.quantitySold) &&
                Objects.equals(totalRevenue, salesDTO.totalRevenue) &&
                Objects.equals(totalCost, salesDTO.totalCost) &&
                Objects.equals(totalProfit, salesDTO.totalProfit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productVariationId, quantitySold, totalRevenue, totalCost, totalProfit);
    }
}
