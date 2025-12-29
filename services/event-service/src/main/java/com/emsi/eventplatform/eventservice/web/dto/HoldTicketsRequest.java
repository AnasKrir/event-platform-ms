package com.emsi.eventplatform.eventservice.web.dto;

import jakarta.validation.constraints.Min;

public class HoldTicketsRequest {
    @Min(1) public int quantity;
}
