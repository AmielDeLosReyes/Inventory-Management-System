package dashboard.IMS.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductDTO {
    private Integer id;
    private String productName;
    private String productDescription;
    private BigDecimal costPrice;
    private BigDecimal sellingPrice;
    private String imageUrls;
}

