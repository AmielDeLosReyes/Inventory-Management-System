package dashboard.IMS.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;

/**
 * Configuration class for OpenAPI documentation.
 *
 * Instruction: navigate to http://localhost:8080/swagger-ui.html to see the documentation
 *
 * @author Amiel De Los Reyes
 */
@Configuration
public class OpenAPIConfig {

    /**
     * Defines custom OpenAPI documentation.
     *
     * @return OpenAPI instance with custom information.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Inventory Management System")
                        .description("The Inventory Management System of the D'Baesic Apparel e-commerce encompasses the organization, tracking, and optimization of product stock, ensuring efficient supply chain management and seamless customer order fulfillment.")
                        .contact(new Contact()
                                .name("Developer: Amiel De Los Reyes")
                                .url("https://github.com/AmielDeLosReyes")));
    }
}
