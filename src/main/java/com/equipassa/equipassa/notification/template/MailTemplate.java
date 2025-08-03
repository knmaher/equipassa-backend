package com.equipassa.equipassa.notification.template;

public enum MailTemplate {
    RESERVATION_CONFIRMATION("reservation.confirmation"),
    REGISTRATION_VERIFICATION("registration.verification"),
    PASSWORD_RESET("password.reset"),
    MEMBERSHIP_RENEWAL("membership.renewal"),
    INVITATION("invitation");

    private final String baseName;

    MailTemplate(final String baseName) {
        this.baseName = baseName;
    }

    public String fileName() {
        return baseName + ".ftl";
    }

    public String subjectKey() {
        return baseName + ".subject";
    }
}
