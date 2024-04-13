package dashboard.IMS.restcontrollers;
import dashboard.IMS.dto.UserDTO;
import dashboard.IMS.entity.Product;
import dashboard.IMS.entity.ProductVariation;
import dashboard.IMS.entity.User;
import dashboard.IMS.repository.ProductRepository;
import dashboard.IMS.repository.ProductVariationRepository;
import dashboard.IMS.restcontroller.HomeRestController;
import dashboard.IMS.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit tests for the HomeRestController class.
 *
 * @author Amiel De Los Reyes
 * @date 02/20/2024
 */
@ExtendWith(MockitoExtension.class)
public class HomeRestControllerTest {

    @Mock
    private ProductVariationRepository productVariationRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductService productService;

    @InjectMocks
    private HomeRestController homeRestController;

    private MockMvc mockMvc;

    /**
     * Setup method to prepare for each test case.
     */
    @BeforeEach
    public void setup() {
        productRepository = Mockito.mock(ProductRepository.class);
        productVariationRepository = Mockito.mock(ProductVariationRepository.class);
        homeRestController = new HomeRestController(productService);
        ReflectionTestUtils.setField(homeRestController, "productRepository", productRepository);
        ReflectionTestUtils.setField(homeRestController, "productVariationRepository", productVariationRepository);
        mockMvc = MockMvcBuilders.standaloneSetup(homeRestController).build();
    }

    /**
     * Test case for the index method.
     *
     * @throws Exception if an error occurs during the test execution
     */
    @Test
    public void testIndex() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1);

        User user = new User();
        user.setId(1);

        Product product = new Product();
        product.setId(1);
        product.setUser(user);

        ProductVariation productVariation = new ProductVariation();
        productVariation.setId(1);
        productVariation.setProduct(product);

        when(productRepository.findByUserIdAndDeletedFalse(userDTO.getId())).thenReturn(Collections.singletonList(product));
        when(productVariationRepository.findAllByProductUserId(userDTO.getId())).thenReturn(Collections.singletonList(productVariation));

        mockMvc.perform(get("/api/")
                        .sessionAttr("loggedInUser", userDTO)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * Test case for handling a 404 error.
     *
     * @throws Exception if an error occurs during the test execution
     */
    @Test
    public void testNotFound() throws Exception {
        mockMvc.perform(get("/api/404")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("404 error"));
    }

    /**
     * Test case for accessing a blank page.
     *
     * @throws Exception if an error occurs during the test execution
     */
    @Test
    public void testBlankPage() throws Exception {
        mockMvc.perform(get("/api/blank-page")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("This is a blank page"));
    }
}
