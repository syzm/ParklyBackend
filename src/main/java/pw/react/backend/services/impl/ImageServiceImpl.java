package pw.react.backend.services.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pw.react.backend.dto.Image.ImageDto;
import pw.react.backend.exceptions.ResourceNotFoundException;
import pw.react.backend.models.CarPark;
import pw.react.backend.models.Image;
import pw.react.backend.repository.ImageRepository;
import pw.react.backend.services.CarParkService;
import pw.react.backend.services.ImageService;

@Service
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final CarParkService carParkService;

    @Autowired
    public ImageServiceImpl(ImageRepository imageRepository,
                            CarParkService carParkService) {
        this.imageRepository = imageRepository;
        this.carParkService = carParkService;
    }
    
    @Override
    public void uploadImage(ImageDto imageDto) {
        CarPark carPark = carParkService.getCarParkById(imageDto.getCarParkId());
        Image image = new Image();
        image.setName(imageDto.getName());
        image.setType(imageDto.getType());
        image.setCarPark(carPark);
        image.setImage(imageDto.getImage());
        Optional<Image> old_image = imageRepository.findByCarParkId(imageDto.getCarParkId());
        if(old_image != null)
        {
            imageRepository.delete(old_image.get());
        }
        imageRepository.save(image);
    }

    @Override
    public ImageDto getImage(Long carParkId) {
        Image image = imageRepository.findByCarParkId(carParkId)
            .orElseThrow(() -> new ResourceNotFoundException("Car Park not found with ID: " + carParkId));
        
        return new ImageDto(
            image.getName(),
            image.getType(),
            image.getCarPark().getId(),
            image.getImage()
        );
    }
}
