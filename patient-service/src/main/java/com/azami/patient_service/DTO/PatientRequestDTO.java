package com.azami.patient_service.DTO;


import com.azami.patient_service.DTO.Validators.CreatePatientValidationGroup;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PatientRequestDTO {

    @NotBlank
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    @NotBlank(message = "Email is required!")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "DateOfBirth is required")
    private String DateOfBirth;

    @NotBlank(message = "Registered Date is required", groups = CreatePatientValidationGroup.class)
    private String registeredDate;
}
