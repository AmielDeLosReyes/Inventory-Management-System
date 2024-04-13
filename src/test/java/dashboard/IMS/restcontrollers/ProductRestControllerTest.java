package dashboard.IMS.restcontrollers;

import dashboard.IMS.dto.UserDTO;
import dashboard.IMS.entity.Product;
import dashboard.IMS.entity.ProductVariation;
import dashboard.IMS.entity.User;
import dashboard.IMS.repository.ProductRepository;
import dashboard.IMS.repository.ProductVariationRepository;
import dashboard.IMS.repository.SalesRepository;
import dashboard.IMS.restcontroller.ProductRestController;
import dashboard.IMS.service.ProductService;
import dashboard.IMS.service.ProductVariationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Collections;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;



/**
 * Unit tests for the ProductRestController class.
 *
 * @author Amiel De Los Reyes
 * @date 02/26/2024
 */
@ExtendWith(MockitoExtension.class)
public class ProductRestControllerTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductVariationRepository productVariationRepository;

    @Mock
    private ProductService productService;

    @Mock
    private ProductVariationService productVariationService;

    @Mock
    private SalesRepository salesRepository;

    @InjectMocks
    private ProductRestController productRestController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        productRepository = Mockito.mock(ProductRepository.class);
        productVariationRepository = Mockito.mock(ProductVariationRepository.class);
        salesRepository = Mockito.mock(SalesRepository.class);
        productService = Mockito.mock(ProductService.class);
        productVariationService = Mockito.mock(ProductVariationService.class);
        productRestController = new ProductRestController(productService, productVariationService);
        ReflectionTestUtils.setField(productRestController, "productRepository", productRepository);
        ReflectionTestUtils.setField(productRestController, "productVariationRepository", productVariationRepository);
        ReflectionTestUtils.setField(productRestController, "salesRepository", salesRepository);
        mockMvc = MockMvcBuilders.standaloneSetup(productRestController).build();
    }

    @Test
    public void testProductList() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1);

        Product product = new Product();
        product.setId(1);

        ProductVariation productVariation = new ProductVariation();
        productVariation.setId(1);
        productVariation.setProduct(product);

        when(productRepository.findByUserIdAndDeletedFalse(userDTO.getId())).thenReturn(Collections.singletonList(product));
        when(productVariationRepository.findAllByProductUserId(userDTO.getId())).thenReturn(Collections.singletonList(productVariation));
        when(productVariationRepository.getTotalQuantitiesByProductUserId(userDTO.getId())).thenReturn(Collections.emptyList());

        MockHttpServletRequestBuilder requestBuilder = get("/api/products")
                .sessionAttr("loggedInUser", userDTO)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products", hasSize(1))) // checks if the 'products' array has 1 element
                .andExpect(jsonPath("$.products[0].id", is(1))); // checks if the 'id' of the first element in the 'products' array is 1
    }

    @Test
    public void testAddProductForm() throws Exception {
        mockMvc.perform(get("/api/add-product")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    public void testEditProduct() throws Exception {
        mockMvc.perform(get("/api/edit-product/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteProduct() throws Exception {
        // Create a UserDTO representing the logged-in user
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1); // Set the ID of the logged-in user

        // Create a MockHttpSession and set the UserDTO as an attribute
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("loggedInUser", userDTO);

        // Create a Product owned by the logged-in user
        Product product = new Product();
        product.setId(1);
        User user = new User();
        user.setId(1); // Set the ID of the product owner
        product.setUser(user);

        // Stub the productService.getProductById() method to return the product
        when(productService.getProductById(anyInt())).thenReturn(product);

        // Perform the delete request
        mockMvc.perform(post("/api/products/1/delete")
                        .session(session) // Set the session
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent()); // Expecting 204 status code
    }

}
