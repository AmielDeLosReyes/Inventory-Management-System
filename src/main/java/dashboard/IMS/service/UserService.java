package dashboard.IMS.service;

import dashboard.IMS.dto.UserDTO;
import dashboard.IMS.entity.User;
import dashboard.IMS.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;



/**
 * Service class for User entity.
 * Handles business logic related to User entities.
 *
 * Author: Amiel De Los Reyes
 * Date: 02/20/2024
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Creates a new User.
     *
     * @param userDTO The DTO representing the User to be created.
     * @return The created User DTO.
     */
    public UserDTO createUser(UserDTO userDTO) {
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        user.setRegistrationDate(new Timestamp(System.currentTimeMillis()));
        User savedEntity = userRepository.save(user);
        return toDTO(savedEntity);
    }

    /**
     * Retrieves all Users.
     *
     * @return A list of all User DTOs.
     */
    public List<UserDTO> getAllUsers() {
        List<User> userEntities = userRepository.findAll();
        return userEntities.stream().map(this::toDTO).collect(Collectors.toList());
    }

    /**
     * Retrieves a User by its ID.
     *
     * @param id The ID of the User to retrieve.
     * @return The User DTO if found, otherwise null.
     */
    public UserDTO getUserById(Integer id) {
        Optional<User> userOptional = userRepository.findById(id);
        return userOptional.map(this::toDTO).orElse(null);
    }

    /**
     * Updates a User.
     *
     * @param id      The ID of the User to update.
     * @param userDTO The DTO representing the updated User.
     * @return The updated User DTO if successful, otherwise null.
     */
    public UserDTO updateUser(Integer id, UserDTO userDTO) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User existingEntity = userOptional.get();
            BeanUtils.copyProperties(userDTO, existingEntity);
            User updatedEntity = userRepository.save(existingEntity);
            return toDTO(updatedEntity);
        }
        return null; // Or throw an exception indicating the user was not found
    }

    /**
     * Deletes a User by its ID.
     *
     * @param id The ID of the User to delete.
     */
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    /**
     * Converts a User entity to a DTO.
     *
     * @param entity The User entity.
     * @return The corresponding User DTO.
     */
    private UserDTO toDTO(User entity) {
        UserDTO dto = new UserDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    /**
     * Signs up a new User.
     *
     * @param userDTO The DTO representing the User to be signed up.
     * @return The signed up User DTO.
     */
    public UserDTO signupUser(UserDTO userDTO) {
        // Check if username or email already exists
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Encrypt the password before saving
        String password = userDTO.getPassword();
        userDTO.setPassword(password);

        // Set registration date
        userDTO.setRegistrationDate(new Timestamp(System.currentTimeMillis()));

        return createUser(userDTO);
    }

    /**
     * Authenticates a user.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @return The authenticated user DTO if successful, otherwise null.
     */
    public UserDTO loginUser(String username, String password) {
        // Find the user by username
        User user = userRepository.findByUsername(username);

        if (user != null) {
            // Check if the password matches
            if (password.equals(user.getPassword())) {
                // Password matches, return the user DTO
                return toDTO(user);
            }
        }

        // Authentication failed, return null
        return null;
    }

    /**
     * Logs out the current user and redirects to the login page.
     *
     * @param request  HTTP request.
     * @param response HTTP response.
     * @return Name of the destination page.
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        // Invalidate the user session
        request.getSession().invalidate();

        // Redirect to the login page after logout
        return "redirect:/login";
    }

    /**
     * Updates the last login timestamp for the user with the given username.
     *
     * @param username   The username of the user to update.
     * @param lastLogin  The timestamp of the last login.
     * @return True if the update was successful, false otherwise.
     */
    public boolean updateLastLogin(String username, Timestamp lastLogin){
        User user = userRepository.findByUsername(username);
        if (user != null) {
            user.setLastLogin(lastLogin);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public UserDTO updateUserDetails(Integer userId, String username, String fullName, String email, String profilePicturePath) {
        // Check if the user exists in the database
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User existingUser = userOptional.get();

            // Update the user details
            existingUser.setUsername(username);
            existingUser.setFullName(fullName);
            existingUser.setEmail(email);
            existingUser.setProfilePicture(profilePicturePath);

            // Save the updated user to the database
            User updatedUser = userRepository.save(existingUser);

            // Convert the updated user entity to a DTO
            UserDTO updatedUserDTO = new UserDTO();
            BeanUtils.copyProperties(updatedUser, updatedUserDTO);

            return updatedUserDTO;
        } else {
            throw new IllegalArgumentException("User with id " + userId + " not found");
        }
    }


}
