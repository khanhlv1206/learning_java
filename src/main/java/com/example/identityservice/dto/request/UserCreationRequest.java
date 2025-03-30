package com.example.identityservice.dto.request;



import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    @Size(min = 5, message = "USERNAME_INVALID")
    private String username;
   @Size(min = 8, message = " PASSWORD_INVALID")
   String password;
   String firstName;
   String lastName;
   LocalDate dob;



}
