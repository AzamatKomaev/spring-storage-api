package ru.azamatkomaev.storage.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
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
    private String jwtAuthToken;
    private final ObjectMapper mapper = new ObjectMapper();
    private final String LOGIN_ENDPOINT_PATH = "/api/v1/auth/login";


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
        jwtAuthToken = jwtService.generateToken(user);
        userRepository.save(user);
    }

    @Test
    public void testGetMe() throws Exception {
        RequestBuilder requestBuilder = get("/api/v1/auth/me")
            .header("Authorization", "Bearer " + jwtAuthToken);
        mockMvc.perform(requestBuilder)
            .andExpect(status().isOk())
            .andDo(print());
    }

}
