package pw.react.backend.dto.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminCreationDto {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;
}