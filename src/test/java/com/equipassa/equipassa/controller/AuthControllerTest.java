package com.equipassa.equipassa.controller;

import com.equipassa.equipassa.AbstractIntegrationTest;
import com.equipassa.equipassa.dto.AddressRequest;
import com.equipassa.equipassa.dto.UserRequest;
import com.equipassa.equipassa.model.MembershipStatus;
import com.equipassa.equipassa.model.SubscriptionTier;
import com.equipassa.equipassa.model.UserRole;
import com.equipassa.equipassa.repository.OrganizationRepository;
import com.equipassa.equipassa.repository.UserRepository;
import com.equipassa.equipassa.security.dto.LoginRequest;
import com.equipassa.equipassa.security.dto.OrgRegistrationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthControllerTest extends AbstractIntegrationTest {

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
    void testRegisterOrg() throws Exception {
        final AddressRequest addressRequest = new AddressRequest(
                "123 Main St", "Cityville", "State", "12345", "Country"
        );

        final OrgRegistrationRequest orgRegistrationRequest = new OrgRegistrationRequest(
                "Test Organization AG", "Test", "User", "tuser@testorg.com",
                "Pass@word1", SubscriptionTier.FREE, addressRequest
        );

        final String requestJson = objectMapper.writeValueAsString(orgRegistrationRequest);

        mockMvc.perform(post("/api/auth/register-org")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.organizationId").exists())
                .andExpect(jsonPath("$.organizationName").value("Test Organization AG"))
                .andExpect(jsonPath("$.adminUserId").exists())
                .andExpect(jsonPath("$.adminEmail").value("tuser@testorg.com"));
    }

    @Test
    void testRegisterOrg_DuplicateOrganizationName() throws Exception {
        final String duplicateOrgName = "Duplicate Organization AG";

        final AddressRequest addressRequest = new AddressRequest(
                "123 Main St", "Cityville", "State", "12345", "Country"
        );

        final OrgRegistrationRequest orgRegistrationRequest = new OrgRegistrationRequest(
                duplicateOrgName, "Test", "User", "tuser@testorg.com",
                "Pass@word1", SubscriptionTier.FREE, addressRequest
        );

        final String requestJson = objectMapper.writeValueAsString(orgRegistrationRequest);

        mockMvc.perform(post("/api/auth/register-org")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                )
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/auth/register-org")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Organization name already exist: " + duplicateOrgName));
    }

    @Test
    void testRegisterOrg_DuplicateAdminEmail() throws Exception {
        final String duplicateAdminEmail = "tuser@testorg.com";

        final AddressRequest addressRequest1 = new AddressRequest(
                "123 Main St", "Cityville", "State", "12345", "Country"
        );
        final AddressRequest addressRequest2 = new AddressRequest(
                "456 Main St", "Cityville", "State", "12345", "Country"
        );
        final OrgRegistrationRequest orgRegistrationRequest1 = new OrgRegistrationRequest(
                "Unique Organization AG", "Test", "User", duplicateAdminEmail,
                "Pass@word1", SubscriptionTier.FREE, addressRequest1
        );
        final OrgRegistrationRequest orgRegistrationRequest2 = new OrgRegistrationRequest(
                "Another Organization AG", "Test", "User", duplicateAdminEmail,
                "Pass@word1", SubscriptionTier.FREE, addressRequest2
        );

        final String requestJson = objectMapper.writeValueAsString(orgRegistrationRequest1);
        final String requestJson2 = objectMapper.writeValueAsString(orgRegistrationRequest2);

        mockMvc.perform(post("/api/auth/register-org")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                )
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/auth/register-org")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson2)
                )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Email already registered: " + duplicateAdminEmail));
    }

    @Test
    void testRegisterUser() throws Exception {
        final AddressRequest addressRequest = new AddressRequest(
                "1 Test Way", "Testville", "State", "54321", "Country"
        );
        final UserRequest request = new UserRequest(
                "John", "Doe", "user@example.com", "Pass@word1",
                MembershipStatus.ACTIVE, "1234567890", addressRequest
        );

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.email").value("user@example.com"))
                .andExpect(jsonPath("$.userRole").value("USER"));
    }

    @Test
    void testRegisterUser_DuplicateEmail() throws Exception {
        final AddressRequest addressRequest = new AddressRequest(
                "1 Test Way", "Testville", "State", "54321", "Country"
        );
        final UserRequest request = new UserRequest(
                "John", "Doe", "dup@example.com", "Pass@word1",
                MembershipStatus.ACTIVE, "1234567890", addressRequest
        );

        final String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Email already registered"));
    }

    @Test
    void testLoginSuccess() throws Exception {
        userRepository.deleteAll();
        organizationRepository.deleteAll();

        final var org = organizationRepository.save(
                com.equipassa.equipassa.util.EntityUtil.createOrganization());

        final var user = com.equipassa.equipassa.util.EntityUtil.createUser(
                "Jane", "Doe", "login@test.com", UserRole.USER, true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setOrganization(org);
        userRepository.save(user);

        final LoginRequest loginRequest = new LoginRequest("login@test.com", "Verified@User1");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.mfaRequired").value(false))
                .andExpect(jsonPath("$.userRole").value("USER"));
    }

    @Test
    void testLoginInvalidPassword() throws Exception {
        userRepository.deleteAll();
        organizationRepository.deleteAll();

        final var org = organizationRepository.save(
                com.equipassa.equipassa.util.EntityUtil.createOrganization());

        final var user = com.equipassa.equipassa.util.EntityUtil.createUser(
                "Jane", "Doe", "wrongpass@test.com", UserRole.USER, true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setOrganization(org);
        userRepository.save(user);

        final LoginRequest loginRequest = new LoginRequest("wrongpass@test.com", "WrongPassword1");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Invalid username or password"));
    }
}