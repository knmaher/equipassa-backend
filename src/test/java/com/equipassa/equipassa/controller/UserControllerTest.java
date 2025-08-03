package com.equipassa.equipassa.controller;

import com.equipassa.equipassa.AbstractIntegrationTest;
import com.equipassa.equipassa.dto.AddressRequest;
import com.equipassa.equipassa.dto.UserProfileRequest;
import com.equipassa.equipassa.model.User;
import com.equipassa.equipassa.model.UserRole;
import com.equipassa.equipassa.repository.OrganizationRepository;
import com.equipassa.equipassa.repository.UserRepository;
import com.equipassa.equipassa.util.EntityUtil;
import com.equipassa.equipassa.util.JwtTestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void testGetProfile() throws Exception {
        final var org = organizationRepository.save(EntityUtil.createOrganization());
        var user = EntityUtil.createUser("John", "Doe", "john@test.com", UserRole.USER, true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setOrganization(org);
        user = userRepository.save(user);

        final String token = JwtTestUtil.generateTestJwtToken(
                user.getEmail(), user.getId(), user.getRole().name(), org.getId(), 60
        );

        mockMvc.perform(get("/api/user/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.email").value("john@test.com"))
                .andExpect(jsonPath("$.firstname").value("John"))
                .andExpect(jsonPath("$.lastname").value("Doe"));
    }

    @Test
    void testUpdateProfile() throws Exception {
        final var org = organizationRepository.save(EntityUtil.createOrganization());
        var user = EntityUtil.createUser("Jane", "Doe", "jane@test.com", UserRole.USER, true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setOrganization(org);
        user = userRepository.save(user);

        final String token = JwtTestUtil.generateTestJwtToken(
                user.getEmail(), user.getId(), user.getRole().name(), org.getId(), 60
        );

        final AddressRequest addressRequest = new AddressRequest(
                "Street 1", "City", "State", "54321", "Country"
        );
        final UserProfileRequest profileRequest = new UserProfileRequest(
                "Janet", "Smith", "janet@example.com", "+491234", addressRequest, false
        );

        mockMvc.perform(post("/api/user/me")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(profileRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname").value("Janet"))
                .andExpect(jsonPath("$.lastname").value("Smith"))
                .andExpect(jsonPath("$.email").value("janet@example.com"))
                .andExpect(jsonPath("$.addressRequest.street").value("Street 1"));

        final User updated = userRepository.findById(user.getId()).orElseThrow();
        assertThat(updated.getFirstname()).isEqualTo("Janet");
        assertThat(updated.getEmail()).isEqualTo("janet@example.com");
        assertThat(updated.getAddress().getStreet()).isEqualTo("Street 1");
    }

    @Test
    void testGetProfileUnauthorized() throws Exception {
        mockMvc.perform(get("/api/user/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testUpdateProfileUnauthorized() throws Exception {
        final AddressRequest addressRequest = new AddressRequest(
                "Street 1", "City", "State", "54321", "Country"
        );
        final UserProfileRequest profileRequest = new UserProfileRequest(
                "Janet", "Smith", "janet@example.com", "+491234", addressRequest, false
        );

        mockMvc.perform(post("/api/user/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(profileRequest)))
                .andExpect(status().isUnauthorized());
    }
}