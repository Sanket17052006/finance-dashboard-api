package com.sanket.financedashboardapi.dto.response;

import java.time.LocalDateTime;

import com.sanket.financedashboardapi.enums.Role;
import com.sanket.financedashboardapi.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private String id;
    private String name;
    private String email;
    private Role role;
    private UserStatus status;
    private LocalDateTime createdAt;

}
