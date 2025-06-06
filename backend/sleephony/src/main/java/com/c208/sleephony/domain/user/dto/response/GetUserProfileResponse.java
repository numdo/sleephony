package com.c208.sleephony.domain.user.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetUserProfileResponse {

    @NotBlank
    private String email;

    @NotBlank
    private String nickname;

    @NotNull
    private Float height;

    @NotNull
    private Float weight;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @NotBlank
    private String gender;
}
