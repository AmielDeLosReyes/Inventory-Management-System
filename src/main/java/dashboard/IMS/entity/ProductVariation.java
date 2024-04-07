package dashboard.IMS.entity;

import lombok.*;

import jakarta.persistence.*;

/**
 * Entity class for Product Variation.
 * Represents product variation data stored in the database.
 *
 * @author Amiel De Los Reyes
 * @date 02/20/2024
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "product_variation")
public class ProductVariation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = CascadeType.REMOVE) // Enable cascading deletion
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "color_id")
    private Color color;

    @ManyToOne
    @JoinColumn(name = "size_id")
    private Size size;

    @Column(name="quantity")
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "user_id") // assuming this is the column name for the user ID
    private User user;  // Reference to the User who created this variation


    public void setUserId(Integer userId) {
        if (userId != null) {
            this.user = new User();  // Instantiate a new User entity
            this.user.setId(userId); // Set the ID of the User entity
        } else {
            this.user = null; // Handle case when userId is null
        }
    }

}
