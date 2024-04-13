package dashboard.IMS.restcontrollers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dashboard.IMS.controller.SalesController;
import dashboard.IMS.dto.UserDTO;
import dashboard.IMS.entity.Product;
import dashboard.IMS.entity.ProductVariation;
import dashboard.IMS.entity.Sales;
import dashboard.IMS.entity.User;
import dashboard.IMS.repository.ProductVariationRepository;
import dashboard.IMS.repository.SalesRepository;
import dashboard.IMS.repository.UserRepository;
import dashboard.IMS.restcontroller.SalesRestController;
import dashboard.IMS.service.SalesService;
import dashboard.IMS.service.UserService;
import dashboard.IMS.utilities.PdfUtil;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import static org.mockito.Mockito.when;

/**
 * Unit tests for the SalesController class.
 *
 * @author Amiel De Los Reyes
 * @date 02/26/2024
 */
class SalesRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductVariationRepository productVariationRepository;

    @Mock
    private SalesRepository salesRepository;

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SalesService salesService;

    @InjectMocks
    private SalesRestController salesController;

    @Mock
    private PdfUtil pdfUtil;

    /**
     * Initialize MockMvc and mocks before each test.
     */
    @BeforeEach
    void setUp() {
        // Initialize mocks
        productVariationRepository = Mockito.mock(ProductVariationRepository.class);
        salesRepository = Mockito.mock(SalesRepository.class);
        userService = Mockito.mock(UserService.class);
        userRepository = Mockito.mock(UserRepository.class);
        salesService = Mockito.mock(SalesService.class);
        pdfUtil = Mockito.mock(PdfUtil.class); // Initialize PdfUtil mock


        // Initialize controller and inject mocks
        salesController = new SalesRestController();
        ReflectionTestUtils.setField(salesController, "productVariationRepository", productVariationRepository);
        ReflectionTestUtils.setField(salesController, "salesRepository", salesRepository);
        ReflectionTestUtils.setField(salesController, "userService", userService);
        ReflectionTestUtils.setField(salesController, "userRepository", userRepository);
        ReflectionTestUtils.setField(salesController, "salesService", salesService);
        ReflectionTestUtils.setField(salesController, "pdfUtil", pdfUtil); // Inject PdfUtil mock

        // Initialize MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(salesController).build();
    }


    /**
     * Test for the sellProductVariation method.
     * @throws Exception if there is an error during test execution
     */
    @Test
    void sellProductVariationTest() throws Exception {
        // Mocking
        Integer productVariationId = 1;
        int quantity = 5;
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1);
        User user = new User();
        user.setId(1);
        ProductVariation productVariation = new ProductVariation();
        productVariation.setQuantity(quantity + 1);
        Product product = new Product();
        product.setCostPrice(BigDecimal.valueOf(10));
        product.setSellingPrice(BigDecimal.valueOf(20));
        productVariation.setProduct(product);

        // Mock the HttpSession to return the UserDTO
        HttpSession session = Mockito.mock(HttpSession.class);
        Mockito.when(session.getAttribute("loggedInUser")).thenReturn(userDTO);

        // Mock the HttpServletRequest
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getSession()).thenReturn(session);

        // Mock the behavior of userRepository.findUserById()
        when(userRepository.findUserById(userDTO.getId())).thenReturn(user);

        // Mock the behavior of productVariationRepository.findByIdAndProductUserId()
        when(productVariationRepository.findByIdAndProductUserId(productVariationId, user.getId())).thenReturn(Optional.of(productVariation));

        // Perform
        mockMvc.perform(MockMvcRequestBuilders.post("/api/sell-product-variation")
                        .param("productVariationId", productVariationId.toString())
                        .param("quantity", Integer.toString(quantity))
                        .sessionAttr("loggedInUser", userDTO))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        // Verify
        verify(userRepository, times(1)).findUserById(userDTO.getId());
        verify(productVariationRepository, times(1)).findByIdAndProductUserId(productVariationId, user.getId());
        verify(productVariationRepository, times(1)).save(any(ProductVariation.class));
        verify(salesRepository, times(1)).save(any(Sales.class));
    }

    /**
     * Test for the refundProductVariation method.
     * @throws Exception if there is an error during test execution
     */
    @Test
    void refundProductVariationTest() throws Exception {
        // Mocking
        Integer productVariationId = 1;
        int quantity = 5;
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1);
        User user = new User();
        user.setId(1);
        ProductVariation productVariation = new ProductVariation();
        productVariation.setQuantity(quantity + 1);
        Product product = new Product();
        product.setCostPrice(BigDecimal.valueOf(10));
        product.setSellingPrice(BigDecimal.valueOf(20));
        productVariation.setProduct(product);

        // Mock the HttpSession to return the UserDTO
        HttpSession session = Mockito.mock(HttpSession.class);
        Mockito.when(session.getAttribute("loggedInUser")).thenReturn(userDTO);

        // Mock the HttpServletRequest
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getSession()).thenReturn(session);

        // Mock the behavior of userRepository.findUserById()
        when(userRepository.findUserById(userDTO.getId())).thenReturn(user);

        // Mock the behavior of productVariationRepository.findByIdAndProductUserId()
        when(productVariationRepository.findByIdAndProductUserId(productVariationId, user.getId())).thenReturn(Optional.of(productVariation));

        // Mock the behavior of salesRepository.findByProductVariationIdAndUserIdOrderByTransactionDateDesc()
        List<Sales> salesRecords = new ArrayList<>();
        Sales salesRecord = new Sales();
        salesRecord.setQuantitySold(quantity);
        salesRecord.setQuantityRefunded(0);
        salesRecords.add(salesRecord);
        when(salesRepository.findByProductVariationIdAndUserIdOrderByTransactionDateDesc(productVariationId, user.getId())).thenReturn(salesRecords);

        // Perform
        mockMvc.perform(MockMvcRequestBuilders.post("/api/refund-product-variation")
                        .param("productVariationId", productVariationId.toString())
                        .param("quantity", String.valueOf(quantity))
                        .sessionAttr("loggedInUser", userDTO))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        // Verify
        verify(userRepository, times(1)).findUserById(userDTO.getId());
        verify(productVariationRepository, times(1)).findByIdAndProductUserId(productVariationId, user.getId());
        verify(salesRepository, times(1)).findByProductVariationIdAndUserIdOrderByTransactionDateDesc(productVariationId, user.getId());
        verify(salesRepository, times(2)).save(any(Sales.class));
    }


}
