package dashboard.IMS.entity;

import lombok.*;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.List;

/**
 * Entity class for User.
 * Represents user data stored in the database.
 *
 * @author Amiel De Los Reyes
 * @date 02/20/2024
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "registration_date", updatable = false, nullable = false)
    private Timestamp registrationDate;

    @Column(name = "last_login")
    private Timestamp lastLogin;

    @Column(nullable = false)
    private String roles;

    @Column(name = "profile_picture")
    private String profilePicture;  // Add this field to store the profile picture path


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Sales> sales;
}
