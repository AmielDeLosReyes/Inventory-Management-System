package dashboard.IMS.controller;

import dashboard.IMS.dto.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import dashboard.IMS.entity.User;
import dashboard.IMS.service.UserService;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup-user")
    public String signupUser(UserDTO userDTO) {
        userService.signupUser(userDTO);
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
    public String login(@RequestParam String username, @RequestParam String password, Model model, HttpServletRequest request) {
        // Authenticate user
        UserDTO authenticatedUser = userService.loginUser(username, password);

        if (authenticatedUser != null) {
            // Authentication successful, set session attribute indicating user is logged in
            HttpSession session = request.getSession();
            session.setAttribute("loggedInUser", authenticatedUser);

            // Redirect to the index page with fullname as query parameter
            String fullName = authenticatedUser.getFullName();
            return "redirect:/?fullname=" + fullName;
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

