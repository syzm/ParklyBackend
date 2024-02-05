package pw.react.backend.services;

import pw.react.backend.dto.Image.ImageDto;

public interface ImageService {
    
    void uploadImage(ImageDto imageDto);

    ImageDto getImage(Long carParkId);
}
