package dashboard.IMS.dto;

import lombok.*;

/**
 * Data Transfer Object (DTO) class for Product Variation.
 * Represents product variation data to be transferred between layers of the application.
 *
 * @author Amiel De Los Reyes
 * @date 02/20/2024
 */
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
