package com.example.demo.dto.patient;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientRequestDto {

    @NotBlank(message = "First name is required")
    @Size(max = 80, message = "First name can be at most 80 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 80, message = "Last name can be at most 80 characters")
    private String lastName;

    @NotBlank(message = "Gender is required")
    @Size(max = 20, message = "Gender can be at most 20 characters")
    private String gender;

    @NotNull(message = "Date of birth is required")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^[0-9+\\-\\s]{7,20}$", message = "Phone number format is invalid")
    private String phone;

    @Email(message = "Email format is invalid")
    @Size(max = 120, message = "Email can be at most 120 characters")
    private String email;

    @Size(max = 500, message = "Address can be at most 500 characters")
    private String address;

    @Size(max = 30, message = "Status can be at most 30 characters")
    private String status;

    @Size(max = 40, message = "Ward id can be at most 40 characters")
    private String wardId;

    @Size(max = 20, message = "Priority can be at most 20 characters")
    private String priority;
}
