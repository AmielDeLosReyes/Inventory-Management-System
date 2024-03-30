package dashboard.IMS.restcontroller;

import dashboard.IMS.dto.UserDTO;
import dashboard.IMS.entity.User;
import dashboard.IMS.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller class for handling user-related endpoints.
 *
 * Author: [Author Name]
 * Date: [Date]
 */
@RestController
@RequestMapping("/users")
public class UserRestController {

    @Autowired
    private UserService userService;

    /**
     * Endpoint to create a new user.
     *
     * @param userDTO The DTO representing the user to be created.
     * @return ResponseEntity with the created user DTO and HTTP status 201 (Created).
     */
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        // Create a new user using the UserService
        UserDTO createdUser = userService.createUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    /**
     * Endpoint to retrieve all users.
     *
     * @return List of all user DTOs.
     */
    @GetMapping
    public List<UserDTO> getAllUsers() {
        // Retrieve all users using the UserService
        return userService.getAllUsers();
    }

    /**
     * Endpoint to retrieve a user by ID.
     *
     * @param id The ID of the user to retrieve.
     * @return ResponseEntity with the user DTO if found, or HTTP status 404 (Not Found) if not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Integer id) {
        // Retrieve a user by ID using the UserService
        User user = userService.getUserById(id);
        return user != null ? (ResponseEntity<UserDTO>) ResponseEntity.ok() : ResponseEntity.notFound().build();
    }

    /**
     * Endpoint to update an existing user.
     *
     * @param id      The ID of the user to update.
     * @param userDTO The DTO representing the updated user.
     * @return ResponseEntity with the updated user DTO if successful, or HTTP status 404 (Not Found) if user not found.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Integer id, @RequestBody UserDTO userDTO) {
        // Update an existing user with the given ID using the UserService
        UserDTO updatedUser = userService.updateUser(id, userDTO);
        return updatedUser != null ? ResponseEntity.ok(updatedUser) : ResponseEntity.notFound().build();
    }

    /**
     * Endpoint to delete a user by ID.
     *
     * @param id The ID of the user to delete.
     * @return ResponseEntity with HTTP status 204 (No Content) if successful.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        // Delete a user by ID using the UserService
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
