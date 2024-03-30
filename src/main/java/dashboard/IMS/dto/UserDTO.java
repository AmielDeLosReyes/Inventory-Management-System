package dashboard.IMS.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;

/**
 * Data Transfer Object (DTO) class for User.
 * Represents user data to be transferred between layers of the application.
 *
 * @author Amiel De Los Reyes
 * @date 02/20/2024
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private Integer id;

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Full name is required")
    private String fullName;

    private Timestamp registrationDate;
    private Timestamp lastLogin;
    private String roles;
    @Getter
    private String profilePicture; // Add this field to store the URL of the user's profile picture
    // other fields...

    // getters and setters...

    public void setPictureUrl(String pictureUrl) {
        this.profilePicture = pictureUrl;
    }

}
