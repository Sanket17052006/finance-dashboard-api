package com.sanket.financedashboardapi.dto.request;

import com.sanket.financedashboardapi.enums.UserStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateStatusRequest {
    @NotNull(message = "Enter Status")
    private UserStatus status;

}
