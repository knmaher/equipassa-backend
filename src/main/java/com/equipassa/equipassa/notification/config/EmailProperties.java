package com.equipassa.equipassa.notification.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "equipassa.email")
public class EmailProperties {
    /**
     * The from-address used on all outgoing system emails.
     */
    private String fromAddress;

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(final String fromAddress) {
        this.fromAddress = fromAddress;
    }
}
