package dashboard.IMS.restcontroller;

import dashboard.IMS.dto.UserDTO;
import dashboard.IMS.repository.UserRepository;
import dashboard.IMS.utilities.FileUploadUtil;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import dashboard.IMS.entity.User;
import dashboard.IMS.service.UserService;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * RestController class for handling user-related requests.
 *
 * @author Amiel De Los Reyes
 * @date 02/20/2024
 */
@RestController
public class UserRestController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ServletContext servletContext;

    /**
     * Handles user signup process.
     *
     * @param userDTO      UserDTO object containing user information.
     * @param bindingResult Binding result for validating user input.
     * @return ResponseEntity with the UserDTO after successful signup or with the BindingResult errors.
     */
    @PostMapping("/signup-user")
    public ResponseEntity<?> signupUser(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        userService.signupUser(userDTO);

        // Return the UserDTO after successful signup
        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
    }


    /**
     * Handles the login process.
     *
     * @param username          Username entered by the user.
     * @param password          Password entered by the user.
     * @param request           HTTP servlet request.
     * @return ResponseEntity with the authenticated UserDTO after successful login or with an error message.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password, HttpServletRequest request) {
        // Authenticate user
        UserDTO authenticatedUser = userService.loginUser(username, password);

        if (authenticatedUser != null) {
            // Update last login timestamp
            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
            userService.updateLastLogin(username, currentTimestamp);

            // Authentication successful, set session attribute indicating user is logged in
            HttpSession session = request.getSession();
            session.setAttribute("loggedInUser", authenticatedUser);

            return new ResponseEntity<>(authenticatedUser, HttpStatus.OK);
        } else {
            // Authentication failed, return an error message
            return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
        }
    }


    /**
     * Updates user details including profile picture.
     *
     * @param profilePicture     Profile picture file.
     * @param username           Username.
     * @param fullName           Full name.
     * @param email              Email address.
     * @param request            HTTP servlet request.
     * @return ResponseEntity with the updated UserDTO after successful update or with an error message.
     */
    @PostMapping("/updateUserDetails")
    public ResponseEntity<?> updateUserDetails(@RequestParam("profilePicture") MultipartFile profilePicture,
                                               @RequestParam("username") String username,
                                               @RequestParam("fullName") String fullName,
                                               @RequestParam("email") String email,
                                               HttpServletRequest request) {
        try {
            // Retrieve the HttpSession
            HttpSession session = request.getSession();

            // Check if a user is logged in based on the session attribute
            if (session.getAttribute("loggedInUser") == null) {
                // User is not authenticated, return an error message
                return new ResponseEntity<>("User is not authenticated", HttpStatus.UNAUTHORIZED);
            }

            // Retrieve the authenticatedUser from the session
            UserDTO userDTO = (UserDTO) session.getAttribute("loggedInUser");

            // Check if a new profile picture is uploaded
            String pictureUrl = null;
            if (profilePicture != null && !profilePicture.isEmpty()) {
                // Save profile picture and get the URL
                pictureUrl = saveProfilePicture(profilePicture, userDTO.getUsername());
            } else {
                // Use the existing profile picture from the database
                pictureUrl = userDTO.getProfilePicture();
            }

            // Update the user details
            UserDTO updatedUserDTO = userService.updateUserDetails(userDTO.getId(), username, fullName, email, pictureUrl);

            // Update the user in the session
            session.setAttribute("loggedInUser", updatedUserDTO);

            // Return the updated UserDTO
            return new ResponseEntity<>(updatedUserDTO, HttpStatus.OK);
        } catch (IOException | IllegalArgumentException e) {
            // Return an error message
            return new ResponseEntity<>("Failed to update user details: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * Saves the profile picture of a user.
     *
     * @param file     The profile picture file to be saved.
     * @param username The username of the user.
     * @return The URL of the saved profile picture.
     * @throws IOException If there is an error while saving the file.
     */
    private String saveProfilePicture(MultipartFile file, String username) throws IOException {
        if (file != null && !file.isEmpty()) {
            String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            // Save the file to the server
            String uploadDir = "src/main/resources/static/users/" + username + "/images";
            FileUploadUtil.saveFile(uploadDir, filename, file);
            // Get the URL of the saved file
            return "/users/" + username + "/images/" + filename;
        }
        return null;
    }
}
