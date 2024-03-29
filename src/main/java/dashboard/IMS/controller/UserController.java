package dashboard.IMS.controller;

import dashboard.IMS.dto.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import dashboard.IMS.entity.User;
import dashboard.IMS.service.UserService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Timestamp;


@Controller
public class UserController {

    @Autowired
    private UserService userService;

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
     * @param username Username entered by the user.
     * @param password Password entered by the user.
     * @param model    Model to add attributes.
     * @return Name of the destination page.
     */
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        // Authenticate user
        UserDTO authenticatedUser = userService.loginUser(username, password);

        // Debug Log
        System.out.println("Authenticated User after login attempt: " + authenticatedUser);

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
            // Authentication failed, add error message to the model and return to the login page
            model.addAttribute("error", "Invalid username or password");
            return "/login";
        }
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
}

