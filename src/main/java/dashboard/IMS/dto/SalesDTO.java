package dashboard.IMS.dto;

import lombok.*;

import java.math.BigDecimal;

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

}
