package com.team22.webcraft.DTO.UserData;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDataDTO {
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,16}$",
            message = "Your 'ID' must be a string of at least 8 and at most 16 characters containing both alphabetic and numeric characters."
    )
    String userId;

    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,16}$",
            message = "Your 'PW' must be a string of at least 8 and at most 16 characters containing both alphabetic and numeric characters."
    )
    String userPw;
}
