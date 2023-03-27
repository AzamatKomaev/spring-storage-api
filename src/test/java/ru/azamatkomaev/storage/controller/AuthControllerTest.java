package ru.azamatkomaev.storage.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections4.map.ReferenceMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.azamatkomaev.storage.model.Role;
import ru.azamatkomaev.storage.model.Status;
import ru.azamatkomaev.storage.model.User;
import ru.azamatkomaev.storage.repository.RoleRepository;
import ru.azamatkomaev.storage.repository.UserRepository;
import ru.azamatkomaev.storage.request.LoginRequest;
import ru.azamatkomaev.storage.request.RegisterRequest;

import javax.lang.model.type.ReferenceType;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
public class AuthControllerTest {

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
    private final String REGISTER_ENDPOINT_PATH = "/api/v1/auth/register";
    private final String LOGIN_ENDPOINT_PATH = "/api/v1/auth/login";
    private final String GET_ME_ENDPOINT_PATH = "/api/v1/auth/me";


    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.
            webAppContextSetup(this.webApplicationContext)
            .apply(springSecurity())
            .build();

        roleRepository.deleteAll();
        userRepository.deleteAll();

        Role defaultRole = Role.builder().name("user").build();
        User user = User.builder()
            .username("general_user")
            .password(passwordEncoder.encode("password12345"))
            .roles(List.of(defaultRole))
            .status(Status.ACTIVE)
            .createdAt(new Date())
            .updatedAt(new Date())
            .build();

        roleRepository.save(defaultRole);
        userRepository.save(user);
    }

    @Test
    public void testGetMeWithInvalidHeader() throws Exception {
        RequestBuilder requestBuilder = get(GET_ME_ENDPOINT_PATH)
            .header("Authorization", "not_valid_header");
        mockMvc.perform(requestBuilder)
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message", is("Cannot get token: header does not exist or not valid")));
    }

    @Test
    public void testRegisterUserWithEmptyBody() throws Exception {
        RequestBuilder requestBuilder = post(REGISTER_ENDPOINT_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content("");
        mockMvc.perform(requestBuilder)
            .andExpect(status().is(400))
            .andExpect(jsonPath("$.body", is("Required request body is missing")));
    }

    @Test
    public void testRegisterUserWithPasswordSizeLessThanEIGHT() throws Exception {
        RegisterRequest request = RegisterRequest.builder()
            .username("Azamat")
            .password("azamat1")
            .build();

        RequestBuilder requestBuilder = post(REGISTER_ENDPOINT_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(request));
        mockMvc.perform(requestBuilder)
            .andExpect(status().is(400))
            .andExpect(jsonPath("$.username").doesNotExist())
            .andExpect(jsonPath("$.password", is("password should contain more than 8 symbols")));
    }

    @Test
    public void testSuccessfulRegisterUser() throws Exception {
        String username = "Azamat";

        RegisterRequest request = RegisterRequest.builder()
            .username(username)
            .password("azamat12345")
            .build();

        RequestBuilder requestBuilder = post(REGISTER_ENDPOINT_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(request));
        mockMvc.perform(requestBuilder)
            .andDo(print())
            .andExpect(status().is(201))
            .andExpect(jsonPath("$.id").isNumber())
            .andExpect(jsonPath("$.username", is(username)));
    }

    @Test
    public void testLoginUserWithEmptyBody() throws Exception {
        RequestBuilder requestBuilder = post(LOGIN_ENDPOINT_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content("");
        mockMvc.perform(requestBuilder)
            .andExpect(status().is(400))
            .andExpect(jsonPath("$.body", is("Required request body is missing")));
    }

    @Test
    public void testLoginUserWithNullValues() throws Exception {
        LoginRequest request = new LoginRequest();

        RequestBuilder requestBuilder = post(REGISTER_ENDPOINT_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(request));
        mockMvc.perform(requestBuilder)
            .andExpect(status().is(400))
            .andExpect(jsonPath("$.username", is("username field cannot be empty")))
            .andExpect(jsonPath("$.password", is("password field cannot be empty")));
    }

    @Test
    public void testLoginWithNonExistingUsername() throws Exception {
        String username = "invalid_general_user";
        LoginRequest request = LoginRequest.builder()
            .username(username)
            .password("password12345")
            .build();
        RequestBuilder requestBuilder = post(LOGIN_ENDPOINT_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(request));
        mockMvc.perform(requestBuilder)
            .andExpect(status().is(404))
            .andExpect(jsonPath("$.message", is("Cannot find any user with username: " + username)));
    }

    @Test
    public void testLoginWithInvalidPassword() throws Exception {
        LoginRequest request = LoginRequest.builder()
            .username("general_user")
            .password("not_valid_password")
            .build();

        RequestBuilder requestBuilder = post(LOGIN_ENDPOINT_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(request));
        mockMvc.perform(requestBuilder)
            .andExpect(status().is(400))
            .andExpect(jsonPath("$.message", is("Bad credentials")));
    }

    @Test
    public void testSuccessfulLogin() throws Exception {
        LoginRequest request = LoginRequest.builder()
            .username("general_user")
            .password("password12345")
            .build();

        RequestBuilder requestBuilder = post(LOGIN_ENDPOINT_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(request));
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
            .andExpect(status().is(201))
            .andExpect(jsonPath("$.token").exists())
            .andReturn();

        Map<String, String> responseBody = mapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>(){});
        String token = responseBody.get("token");

        requestBuilder = get(GET_ME_ENDPOINT_PATH)
            .header("Authorization", "Bearer " + token);
        mockMvc.perform(requestBuilder)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").isNumber())
            .andExpect(jsonPath("$.username", is("general_user")));
    }
}
