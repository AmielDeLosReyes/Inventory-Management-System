package dashboard.IMS.controller;

import dashboard.IMS.dto.UserDTO;
import dashboard.IMS.repository.UserRepository;
import dashboard.IMS.utilities.FileUploadUtil;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import dashboard.IMS.entity.User;
import dashboard.IMS.service.UserService;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.util.Objects;


@Controller
public class UserController {

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
     * @param model         Model for adding attributes.
     * @return Redirect to the login page after successful signup or to the signup page with errors.
     */
    @PostMapping("/signup-user")
    public String signupUser(@Valid @ModelAttribute("userDTO") UserDTO userDTO, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()) {
            return "signup";
        }
        userService.signupUser(userDTO);

        // Add userDTO object to the model
        model.addAttribute("userDTO", userDTO);

        // Redirect to login page after successful signup
        return "redirect:/login";
    }

    /**
     * Handles the login process.
     *
     * @param username          Username entered by the user.
     * @param password          Password entered by the user.
     * @param model             Model to add attributes.
     * @param request           HTTP servlet request.
     * @param redirectAttributes Redirect attributes for passing messages.
     * @return Redirect to the home page after successful login or to the login page with an error message.
     */
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        // Authenticate user
        UserDTO authenticatedUser = userService.loginUser(username, password);

        if (authenticatedUser != null) {
            // Update last login timestamp
            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
            userService.updateLastLogin(username, currentTimestamp);

            // Authentication successful, set session attribute indicating user is logged in
            HttpSession session = request.getSession();
            session.setAttribute("loggedInUser", authenticatedUser);

            // Add authenticatedUser as flash attribute
            redirectAttributes.addFlashAttribute("authenticatedUser", authenticatedUser);

            return "redirect:/";
        } else {
            // Authentication failed, add error message to the flash attribute and redirect to login page
            redirectAttributes.addFlashAttribute("error", "Invalid username or password");
            redirectAttributes.addFlashAttribute("messageType", "failure"); // Make sure to set the messageType to 'failure'
            return "redirect:/login";
        }
    }


    /**
     * Logs out the current user and redirects to the login page.
     *
     * @param request  HTTP request.
     * @param response HTTP response.
     * @return Redirect to the login page after logout.
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        // Invalidate the user session
        request.getSession().invalidate();

        // Redirect to the login page after logout
        return "redirect:/login";
    }

    /**
     * Directs users to the edit profile page.
     *
     * @param request HTTP servlet request.
     * @param model   Model for adding attributes.
     * @return Name of the edit profile page.
     */
    @GetMapping("/editProfile")
    public String editProfile(HttpServletRequest request, Model model) {
        // Retrieve the HttpSession
        HttpSession session = request.getSession();

        // Check if a user is logged in based on the session attribute
        if (session.getAttribute("loggedInUser") == null) {
            // User is not authenticated, redirect to the login page
            return "redirect:/login";
        }

        // Retrieve the authenticatedUser from the session
        UserDTO authenticatedUser = (UserDTO) session.getAttribute("loggedInUser");

        System.out.println("Authenticated User Full Name: " + authenticatedUser.getFullName());

        // Add the authenticatedUser to the model if needed for the view
        model.addAttribute("authenticatedUser", authenticatedUser);
        return "editProfile";
    }


    /**
     * Updates user details including profile picture.
     *
     * @param profilePicture     Profile picture file.
     * @param username           Username.
     * @param fullName           Full name.
     * @param email              Email address.
     * @param request            HTTP servlet request.
     * @param redirectAttributes Redirect attributes for passing messages.
     * @return Redirect to the home page after successful update or to the home page with an error message.
     */
    @PostMapping("/updateUserDetails")
    public String updateUserDetails(@RequestParam("profilePicture") MultipartFile profilePicture,
                                    @RequestParam("username") String username,
                                    @RequestParam("fullName") String fullName,
                                    @RequestParam("email") String email,
                                    HttpServletRequest request,
                                    RedirectAttributes redirectAttributes) {
        try {
            // Retrieve the HttpSession
            HttpSession session = request.getSession();

            // Check if a user is logged in based on the session attribute
            if (session.getAttribute("loggedInUser") == null) {
                // User is not authenticated, redirect to the login page
                return "redirect:/login";
            }

            // Retrieve the authenticatedUser from the session
            UserDTO userDTO = (UserDTO) session.getAttribute("loggedInUser");

            // Check if a new profile picture is uploaded
            String pictureUrl = null;
            if (profilePicture != null && !profilePicture.isEmpty()) {
                // Save profile picture and get the URL
                pictureUrl = saveProfilePicture(profilePicture, userDTO.getUsername());
                System.out.println("Profile Picture URL: " + pictureUrl); // Log the picture URL
            } else {
                // Use the existing profile picture from the database
                pictureUrl = userDTO.getProfilePicture();
            }

            // Update the user details
            UserDTO updatedUserDTO = userService.updateUserDetails(userDTO.getId(), username, fullName, email, pictureUrl);

            // Update the user in the session
            session.setAttribute("loggedInUser", updatedUserDTO);

            // Redirect with success message
            redirectAttributes.addFlashAttribute("successMessage", "User details updated successfully!");
            redirectAttributes.addFlashAttribute("updatedUser", updatedUserDTO);
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("Failed to update user details: " + e.getMessage()); // Log the exception
            // Redirect with error message
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update user details: " + e.getMessage());
        }

        return "redirect:/";
    }


    // Method to save profile picture
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

