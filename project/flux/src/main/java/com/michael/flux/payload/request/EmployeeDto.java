package com.michael.flux.payload.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EmployeeDto {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
}
