package dashboard.IMS.dto;

import lombok.*;

/**
 * Data Transfer Object (DTO) class for Color.
 * Represents color data to be transferred between layers of the application.
 *
 * @author Amiel De Los Reyes
 * @date 02/20/2024
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ColorDTO {
    private Integer id;
    private String name;
}
