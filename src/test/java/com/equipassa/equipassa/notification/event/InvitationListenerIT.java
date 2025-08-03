//package com.equipassa.equipassa.notification.event;
//
//import com.equipassa.equipassa.AbstractIntegrationTest;
//import com.equipassa.equipassa.model.*;
//import com.equipassa.equipassa.notification.dto.InvitationEvent;
//import com.equipassa.equipassa.repository.ActionTokenRepository;
//import com.equipassa.equipassa.repository.OrganizationRepository;
//import com.equipassa.equipassa.repository.UserRepository;
//import com.equipassa.equipassa.util.EntityUtil;
//import com.icegreen.greenmail.junit5.GreenMailExtension;
//import com.icegreen.greenmail.util.ServerSetupTest;
//import jakarta.mail.internet.MimeMessage;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.RegisterExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.ApplicationEventPublisher;
//import org.springframework.test.context.DynamicPropertyRegistry;
//import org.springframework.test.context.DynamicPropertySource;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.UUID;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//@Transactional
//class InvitationListenerIT extends AbstractIntegrationTest {
//
//    @RegisterExtension
//    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP);
//
//    @DynamicPropertySource
//    static void mailProps(DynamicPropertyRegistry registry) {
//        registry.add("spring.mail.host", () -> "localhost");
//        registry.add("spring.mail.port", () -> greenMail.getSmtp().getPort());
//    }
//
//    @Autowired
//    private ApplicationEventPublisher events;
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private OrganizationRepository organizationRepository;
//    @Autowired
//    private ActionTokenRepository tokenRepository;
//
//    @Test
//    void listenerSendsEmail() throws Exception {
//        Organization org = organizationRepository.save(EntityUtil.createOrganization());
//        User admin = EntityUtil.createUser("Admin", "User", "admin@org.com", UserRole.ORG_ADMIN, true);
//        admin.setOrganization(org);
//        admin = userRepository.save(admin);
//
//        ActionToken token = new ActionToken();
//        token.setToken(UUID.randomUUID().toString());
//        token.setUserId(admin.getId());
//        token.setType(ActionTokenType.INVITATION);
//        token.setExpiresAt(LocalDateTime.now().plusDays(1));
//        token.setConsumed(false);
//        token.setVisibility(TokenVisibility.PRIVATE);
//        token.setInviteEmail("invitee@test.com");
//        token.setInviteRole(UserRole.MEMBER);
//        token = tokenRepository.save(token);
//
//        events.publishEvent(new InvitationEvent(token));
//
//        greenMail.waitForIncomingEmail(1);
//        MimeMessage[] messages = greenMail.getReceivedMessages();
//        assertThat(messages).hasSize(1);
//        assertThat(messages[0].getAllRecipients()[0].toString()).isEqualTo("invitee@test.com");
//    }
//}
//
