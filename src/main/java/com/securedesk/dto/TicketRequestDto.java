package com.securedesk.dto;

import com.securedesk.enums.TicketCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TicketRequestDto {

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotNull
    private TicketCategory category;

    @NotNull
    private Long createdByUserId;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public TicketCategory getCategory() { return category; }
    public void setCategory(TicketCategory category) { this.category = category; }
    public Long getCreatedByUserId() { return createdByUserId; }
    public void setCreatedByUserId(Long createdByUserId) { this.createdByUserId = createdByUserId; }
}
