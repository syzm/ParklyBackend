package pw.react.backend.security.jwt.models;

import jakarta.validation.constraints.NotEmpty;

import java.io.Serial;
import java.io.Serializable;

public record JwtRequest(@NotEmpty String email, @NotEmpty String password) implements Serializable {
    @Serial
    private static final long serialVersionUID = 5926468583005150707L;
}
