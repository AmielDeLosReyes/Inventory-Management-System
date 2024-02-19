package dashboard.IMS.dto;

import lombok.*;

import java.sql.Timestamp;

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
