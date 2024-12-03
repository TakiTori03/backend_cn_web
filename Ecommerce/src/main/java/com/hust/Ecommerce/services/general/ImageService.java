package com.hust.Ecommerce.services.general;

import org.springframework.web.multipart.MultipartFile;

import com.hust.Ecommerce.dtos.general.CloudinaryImageDTO;

public interface ImageService {
    public CloudinaryImageDTO uploadImage(MultipartFile file, String uploadFolder);

    public String deleteImage(String publicId);
}
