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

import java.util.Arrays;
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
public class UserControllerTest {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;
    private final ObjectMapper mapper = new ObjectMapper();
    private final String GET_ALL_USERS_ENDPOINT_PATH = "/api/v1/users";

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        this.mockMvc = MockMvcBuilders.
            webAppContextSetup(this.webApplicationContext)
            .apply(springSecurity())
            .build();

        Role defaultRole = roleRepository.findByName("ROLE_USER").orElse(null);
        assert defaultRole != null;

        String[] usernames = {"Azamat", "Vladimir", "Mikhail"};
        Arrays.stream(usernames).forEach(username -> {
            User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode("password12345"))
                .roles(List.of(defaultRole))
                .status(Status.ACTIVE)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
            userRepository.save(user);
        });
    }

    @Test
    public void testGetAllUsers() throws Exception {
        RequestBuilder requestBuilder = get(GET_ALL_USERS_ENDPOINT_PATH);
        mockMvc.perform(requestBuilder)
            .andDo(print())
            .andExpect(status().isOk());
    }
}
