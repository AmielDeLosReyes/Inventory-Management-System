package dashboard.IMS.controller;

import dashboard.IMS.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import dashboard.IMS.entity.User;
import dashboard.IMS.service.UserService;

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
}

