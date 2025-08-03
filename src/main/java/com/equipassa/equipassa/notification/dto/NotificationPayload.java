package com.equipassa.equipassa.notification.dto;

import com.equipassa.equipassa.notification.template.MailTemplate;

import java.util.Map;

public record NotificationPayload(
        MailTemplate template,
        Map<String, Object> model,
        Object[] subjectArgs
) {
}
