package com.sanket.financedashboardapi.dto.request;

import com.sanket.financedashboardapi.enums.Role;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateRoleRequest {
    @NotNull(message = "It is compulsory to add Role")
    private Role role;

}
