package dashboard.IMS.dto;

import lombok.*;

/**
 * Data Transfer Object (DTO) class for Size.
 * Represents size data to be transferred between layers of the application.
 *
 * @author Amiel De Los Reyes
 * @date 02/20/2024
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SizeDTO {
    private Integer id;
    private String name;
}
