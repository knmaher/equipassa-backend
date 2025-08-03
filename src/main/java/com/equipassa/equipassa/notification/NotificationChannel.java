package com.equipassa.equipassa.notification;

import com.equipassa.equipassa.notification.dto.NotificationPayload;
import com.equipassa.equipassa.model.User;

public interface NotificationChannel {
    void send(User user, NotificationPayload payload);
    boolean supports(User user);
}
