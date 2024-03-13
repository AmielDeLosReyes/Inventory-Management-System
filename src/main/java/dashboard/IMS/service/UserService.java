package dashboard.IMS.service;

import dashboard.IMS.dto.UserDTO;
import dashboard.IMS.entity.User;
import dashboard.IMS.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

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
}
