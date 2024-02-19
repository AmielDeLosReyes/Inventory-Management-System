package dashboard.IMS.dto;

import lombok.*;

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
    private String username;
    private String password;
    private String email;
    private String fullName;
    private Timestamp registrationDate;
    private Timestamp lastLogin;
    private String roles;
}
