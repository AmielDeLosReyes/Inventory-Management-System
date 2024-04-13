package dashboard.IMS.restcontrollers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dashboard.IMS.dto.UserDTO;
import dashboard.IMS.repository.UserRepository;
import dashboard.IMS.restcontroller.UserRestController;
import dashboard.IMS.service.UserService;
import jakarta.servlet.ServletContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit tests for the UserRestController class.
 *
 * @author Amiel De Los Reyes
 * @date 02/20/2024
 */
public class UserRestControllerTest {

    @InjectMocks
    UserRestController userRestController;

    @Mock
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Mock
    ServletContext servletContext;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userRestController).build();
    }

    /**
     * Test case for signing up a user.
     *
     * @throws Exception if an error occurs during the test execution
     */
    @Test
    public void testSignupUser() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("test");
        userDTO.setPassword("test12345"); // Password should be at least 8 characters long
        userDTO.setEmail("test@test.com");
        userDTO.setFullName("Test User"); // Full name is required
// No need to set the profilePicture, registrationDate, and lastLogin fields for the signup test

        when(userService.signupUser(any(UserDTO.class))).thenReturn(userDTO);

        ObjectMapper objectMapper = new ObjectMapper();
        String userDTOJson = objectMapper.writeValueAsString(userDTO);

        mockMvc.perform(post("/api/signup-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDTOJson))
                .andExpect(status().isCreated());
    }

    /**
     * Test case for logging in a user.
     *
     * @throws Exception if an error occurs during the test execution
     */
    @Test
    public void testLogin() throws Exception {
        UserDTO userDTO = new UserDTO();
        when(userService.loginUser(any(String.class), any(String.class))).thenReturn(userDTO);

        mockMvc.perform(post("/api/login")
                        .param("username", "test")
                        .param("password", "test"))
                .andExpect(status().isOk());
    }

    /**
     * Test case for updating user details including profile picture.
     *
     * @throws Exception if an error occurs during the test execution
     */
    @Test
    public void testUpdateUserDetails() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1); // Set an ID for the user
        userDTO.setUsername("test");
        userDTO.setFullName("test");
        userDTO.setEmail("test@test.com");

        when(userService.updateUserDetails(any(Integer.class), any(String.class), any(String.class), any(String.class), any(String.class))).thenReturn(userDTO);

        // Create a MockMultipartFile object
        MockMultipartFile profilePicture = new MockMultipartFile("profilePicture", "test.jpg", "image/jpeg", "test image content".getBytes());

        // Mock the session
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("loggedInUser", userDTO);

        mockMvc.perform(multipart("/api/updateUserDetails")
                        .file(profilePicture)
                        .param("username", "test")
                        .param("fullName", "test")
                        .param("email", "test@test.com")
                        .session(session)) // Add the mock session to the request
                .andExpect(status().isOk());
    }
}