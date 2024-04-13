package dashboard.IMS.service;

import dashboard.IMS.dto.UserDTO;
import dashboard.IMS.entity.User;
import dashboard.IMS.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the UserService class.
 * These tests validate the behavior of UserService methods.
 * Mocking is used to isolate the class under test and focus on its logic independently.
 *
 * Author: Amiel De Los Reyes
 * Date: 02/20/2024
 */
public class UserServiceTest {

    // Inject the UserService object which is to be tested
    @InjectMocks
    private UserService userService;

    // Mock the UserRepository which is used by the UserService
    @Mock
    private UserRepository userRepository;

    // This method is executed before each test. It initializes the mock objects.
    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    // Test case for the getAllUsers method
    @Test
    public void testGetAllUsers() {
        // Define the behavior of the mock object
        when(userRepository.findAll()).thenReturn(Arrays.asList(new User(), new User()));
        // Assert that the size of the result is as expected
        assertEquals(2, userService.getAllUsers().size());
        // Verify that the findAll method was called exactly once
        verify(userRepository, times(1)).findAll();
    }

    // Test case for the getUserDTOById method when the ID is not found
    @Test
    public void testGetUserDTOByIdNotFound() {
        // Define the behavior of the mock object
        when(userRepository.findById(1)).thenReturn(Optional.empty());
        // Assert that the result is null
        assertNull(userService.getUserDTOById(1));
        // Verify that the findById method was called exactly once
        verify(userRepository, times(1)).findById(1);
    }

    // Test case for the deleteUser method
    @Test
    public void testDeleteUser() {
        // Call the method to test
        userService.deleteUser(1);
        // Verify that the deleteById method was called exactly once
        verify(userRepository, times(1)).deleteById(1);
    }
}