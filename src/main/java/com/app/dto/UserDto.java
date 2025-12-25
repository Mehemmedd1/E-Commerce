package com.app.dto;
import com.app.model.Orders;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@Builder
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private Set<String> roles;
    private List<Orders> orders;
}