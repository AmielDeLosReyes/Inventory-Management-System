package dashboard.IMS.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @GetMapping("/add-product")
    public String addProduct() {
        return "addproduct";
    }

    @GetMapping("/sales-report")
    public String salesReport() {
        return "salesreport";
    }

    @GetMapping("/products")
    public String productList() {
        return "products";
    }

    @GetMapping("/404")
    public String notFound() {
        return "404";
    }

    @GetMapping("/blank-page")
    public String blankPage() {
        return "blankpage";
    }
}
