package ru.azamatkomaev.storage.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.azamatkomaev.storage.model.Role;
import ru.azamatkomaev.storage.model.Status;
import ru.azamatkomaev.storage.model.User;
import ru.azamatkomaev.storage.repository.RoleRepository;
import ru.azamatkomaev.storage.repository.UserRepository;
import ru.azamatkomaev.storage.service.JwtService;

import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FileControllerTest {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;
    private final ObjectMapper mapper = new ObjectMapper();

    private String AUTH_TOKEN;
    private final String GET_ME_ENDPOINT_PATH = "/api/v1/auth/me";
    private final String FILE_LIST_ENDPOINT_PATH = "/api/v1/files";


    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        this.mockMvc = MockMvcBuilders.
            webAppContextSetup(this.webApplicationContext)
            .apply(springSecurity())
            .build();

        Role defaultRole = roleRepository.findByName("ROLE_USER").orElse(null);
        assert defaultRole != null;

        User user = User.builder()
            .username("general_user")
            .password(passwordEncoder.encode("password12345"))
            .roles(List.of(defaultRole))
            .status(Status.ACTIVE)
            .createdAt(new Date())
            .updatedAt(new Date())
            .build();
        AUTH_TOKEN = jwtService.generateToken(user);
        userRepository.save(user);
    }

    @Test
    @Order(1)
    public void testGetMe() throws Exception {
        RequestBuilder requestBuilder = get(GET_ME_ENDPOINT_PATH)
            .header("Authorization", "Bearer " + AUTH_TOKEN);
        mockMvc.perform(requestBuilder)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.username", is("general_user")));
    }

    @Test
    public void testAddFileWithoutAuthorization() throws Exception {
        RequestBuilder requestBuilder = post(FILE_LIST_ENDPOINT_PATH);
        mockMvc.perform(requestBuilder)
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message", is("Cannot get token: header does not exist or not valid")));
    }

    @Test
    public void testAddFileWithInvalidAuthToken() throws Exception {
        RequestBuilder requestBuilder = post(FILE_LIST_ENDPOINT_PATH)
            .header("Authorization", "abc" + AUTH_TOKEN);
        mockMvc.perform(requestBuilder)
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message", is("Cannot get token: header does not exist or not valid")));
    }

    @Test
    public void testAddFileWithoutNoMultipartHeader() throws Exception {
        RequestBuilder requestBuilder = post(FILE_LIST_ENDPOINT_PATH)
            .header("Authorization", "Bearer " + AUTH_TOKEN);
        mockMvc.perform(requestBuilder)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", is("no multipart boundary was found")));
    }

    @Test
    public void testAddFileWithoutProvidingFile() throws Exception {
        RequestBuilder requestBuilder = post(FILE_LIST_ENDPOINT_PATH)
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .header("Authorization", "Bearer " + AUTH_TOKEN);
        mockMvc.perform(requestBuilder)
            .andExpect(status().isBadRequest());
    }

    @Order(100)
    @Test
    public void testSuccessfullyAddFile() throws Exception {
        System.out.println("50 test....");
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "hello.txt",
            MediaType.TEXT_PLAIN_VALUE,
            "Hello, World!".getBytes()
        );

        RequestBuilder requestBuilder = multipart(FILE_LIST_ENDPOINT_PATH)
            .file(file)
            .header("Authorization", "Bearer " + AUTH_TOKEN);
        mockMvc.perform(requestBuilder)
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").isNumber())

            .andExpect(jsonPath("$.user.id").isNumber())
            .andExpect(jsonPath("$.user.username", is("general_user")))

            .andExpect(jsonPath("$.file.id").isNumber())
            .andExpect(jsonPath("$.file.name", is("hello.txt")))
            .andExpect(jsonPath("$.file.file_path", is("hello.txt")));
    }
}
