package dashboard.IMS.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller class for handling HTTP requests related to navigation.
 * Responsible for directing users to various pages.
 *
 * @author Amiel De Los Reyes
 * @date 02/20/2024
 */
@Controller
public class HomeController {

    /**
     * Directs users to the index page.
     *
     * @return Name of the index page.
     */
    @GetMapping("/")
    public String index() {
        return "index";
    }

    /**
     * Directs users to the login page.
     *
     * @return Name of the login page.
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * Directs users to the signup page.
     *
     * @return Name of the signup page.
     */
    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    /**
     * Directs users to the add product page.
     *
     * @return Name of the add product page.
     */
    @GetMapping("/add-product")
    public String addProduct() {
        return "addproduct";
    }

    /**
     * Directs users to the sales report page.
     *
     * @return Name of the sales report page.
     */
    @GetMapping("/sales-report")
    public String salesReport() {
        return "salesreport";
    }

    /**
     * Directs users to the product list page.
     *
     * @return Name of the product list page.
     */
    @GetMapping("/products")
    public String productList() {
        return "products";
    }

    /**
     * Directs users to the 404 error page.
     *
     * @return Name of the 404 error page.
     */
    @GetMapping("/404")
    public String notFound() {
        return "404";
    }

    /**
     * Directs users to the blank page.
     *
     * @return Name of the blank page.
     */
    @GetMapping("/blank-page")
    public String blankPage() {
        return "blankpage";
    }
}
