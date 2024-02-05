package pw.react.backend.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import pw.react.backend.dto.Image.ImageDto;
import pw.react.backend.services.ImageService;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping
public class ImageController {

    private static final Logger log = LoggerFactory.getLogger(UserSpotController.class);
    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/car_park/{carParkId}/image/upload")
    public void uploadImage(@PathVariable Long carParkId, 
                            @RequestParam(name = "image") MultipartFile file) {
        ImageDto imageDto = new ImageDto();
        try {
            imageDto.setImage(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        imageDto.setName(file.getOriginalFilename());
        imageDto.setType(file.getContentType());
        imageDto.setCarParkId(carParkId);
        imageService.uploadImage(imageDto);
    }
    
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(
        value = "/car_park/{carParkId}/image",
        produces = MediaType.IMAGE_JPEG_VALUE
    )
    public @ResponseBody byte[] getImage(@PathVariable Long carParkId) {
        ImageDto imageDto = imageService.getImage(carParkId);
        return imageDto.getImage();
    }
}
