package pw.react.backend.dto.Image;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImageDto {

    @NotBlank
    private String name;

    @NotBlank
    private String type;

    @NotBlank
    private Long carParkId;

    @NotBlank
    private byte[] image;
}
