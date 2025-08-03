package com.equipassa.equipassa.reservation.pipeline;

import com.equipassa.equipassa.model.Tool;
import com.equipassa.equipassa.model.User;
import com.equipassa.equipassa.repository.ToolRepository;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Component
@Order(10)
public class OrganizationValidationHandler implements ReservationHandler {

    private final ToolRepository toolRepository;

    OrganizationValidationHandler(ToolRepository toolRepository) {
        this.toolRepository = toolRepository;
    }

    @Override
    public ReservationContext handle(ReservationContext reservationContext) {

        final Tool tool = toolRepository.findById(reservationContext.request().toolId())
                .orElseThrow(() -> new RuntimeException("Tool not found"));

        final User user = reservationContext.draft().getUser();

        if (user.getOrganization() == null
                || tool.getOrganization() == null
                || !user.getOrganization().getId().equals(tool.getOrganization().getId())) {
            throw new AccessDeniedException("you are not allowed to reserve this tool");
        }

        reservationContext.draft().setTool(tool);
        return reservationContext;
    }
}
