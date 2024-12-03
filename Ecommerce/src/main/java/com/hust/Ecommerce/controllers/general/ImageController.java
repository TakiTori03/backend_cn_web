package com.hust.Ecommerce.controllers.general;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hust.Ecommerce.dtos.ApiResponse;
import com.hust.Ecommerce.dtos.CollectionWrapper;
import com.hust.Ecommerce.dtos.general.CloudinaryImageDTO;
import com.hust.Ecommerce.services.general.ImageService;

import lombok.RequiredArgsConstructor;

//controller nay co tac dung upload file len cloudinary va tra ve public url

@RestController
@RequestMapping("/cloudinary-api/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping(value = "/upload-single", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<ApiResponse<?>> uploadSingleImage(@RequestParam("image") MultipartFile image,
            @RequestParam("folder") String folder) {
        CloudinaryImageDTO uploadImageUrl = imageService.uploadImage(image, folder);
        return ResponseEntity
                .ok(ApiResponse.<CloudinaryImageDTO>builder().success(true).payload(uploadImageUrl).build());

    }

    @PostMapping("/upload-multiple")
    public ResponseEntity<ApiResponse<?>> uploadMultipleImages(
            @RequestParam("images") MultipartFile[] images, @RequestParam("folder") String folder) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<CollectionWrapper<CloudinaryImageDTO>>builder()
                        .success(true).payload(new CollectionWrapper<>(Stream.of(images)
                                .map(image -> imageService.uploadImage(image, folder))
                                .collect(Collectors.toList())))
                        .build());
    }

    @DeleteMapping("/single-image")
    public ResponseEntity<Void> deleteImage(@RequestBody String imageName) {
        imageService.deleteImage(imageName);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/multi-image")
    public ResponseEntity<Void> deleteMultipleImages(@RequestBody List<String> imageNames) {
        imageNames.forEach(imageService::deleteImage);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
