package com.equipassa.equipassa.security.service;

import com.equipassa.equipassa.model.*;
import com.equipassa.equipassa.notification.dto.InvitationEvent;
import com.equipassa.equipassa.repository.ActionTokenRepository;
import com.equipassa.equipassa.repository.UserRepository;
import com.equipassa.equipassa.security.dto.InviteUserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvitationServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ActionTokenRepository actionTokenRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private ApplicationEventPublisher events;

    @InjectMocks
    private InvitationService invitationService;

    private User admin;

    @BeforeEach
    void setUp() {
        admin = new User();
        admin.setId(1L);
        admin.setFirstname("Admin");
        admin.setOrganization(new Organization());
    }

    @Test
    void sendInvitation_publishesEvent() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(actionTokenRepository.save(any(ActionToken.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        InviteUserRequest req = new InviteUserRequest("invite@test.com", UserRole.MEMBER);
        invitationService.sendInvitation(req, 1L);

        ArgumentCaptor<InvitationEvent> captor = ArgumentCaptor.forClass(InvitationEvent.class);
        verify(events).publishEvent(captor.capture());
        assertThat(captor.getValue().token().getInviteEmail()).isEqualTo("invite@test.com");
    }
}

