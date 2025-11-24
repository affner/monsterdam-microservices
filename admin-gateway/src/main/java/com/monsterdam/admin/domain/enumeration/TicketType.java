package com.monsterdam.admin.domain.enumeration;

/**
 * The TicketType enumeration.
 */
public enum TicketType {
    ACCESS_ISSUE("admin.ticket.type.accessIssue"),
    DOCUMENT_VERIFICATION("admin.ticket.type.document.verification"),
    REFUND_REQUEST("admin.ticket.type.refund.request"),
    REPORT_REQUEST("admin.ticket.type.report.request"),
    OTHER("admin.ticket.type.other"),
    CONTENT_ISSUE("admin.ticket.type.content.issue"),
    PAYMENT_ISSUE("admin.ticket.type.payment.issue"),
    HARASSMENT_REPORT("admin.ticket.type.harassment.report"),
    BUG_REPORT("admin.ticket.type.bug.report"),
    ACCOUNT_SUSPENSION("admin.ticket.type.account.suspension"),
    PRIVACY_CONCERN("admin.ticket.type.privacy.concern"),
    FEATURE_REQUEST("admin.ticket.type.feature.request");

    private final String value;

    TicketType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
