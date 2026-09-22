package com.securedesk.dto;

import jakarta.validation.constraints.NotNull;

public class AssignTicketDto {
    @NotNull
    private Long staffUserId;

    public Long getStaffUserId() { return staffUserId; }
    public void setStaffUserId(Long staffUserId) { this.staffUserId = staffUserId; }
}
