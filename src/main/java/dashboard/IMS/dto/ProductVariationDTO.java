package dashboard.IMS.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariationDTO {

    private Integer id;
    private Integer productId;
    private Integer colorId;
    private Integer sizeId;
    private int quantity;
}
