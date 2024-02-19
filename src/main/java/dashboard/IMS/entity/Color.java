package dashboard.IMS.entity;

import lombok.*;

import jakarta.persistence.*;

/**
 * Entity class for Color.
 * Represents color data stored in the database.
 *
 * @author Amiel De Los Reyes
 * @date 02/20/2024
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "color")
public class Color {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
}
