package com.hust.Ecommerce.services.general;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.hust.Ecommerce.dtos.general.CloudinaryImageDTO;

import com.hust.Ecommerce.exceptions.payload.UploadImageException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final Cloudinary cloudinary;

    @Override
    public CloudinaryImageDTO uploadImage(MultipartFile file, String uploadFolder) {
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap(
                            "resource_type", "auto",
                            "folder", uploadFolder));
            return new CloudinaryImageDTO()
                    .setPath(uploadResult.get("url").toString())
                    .setContentType(
                            uploadResult.get("resource_type").toString() + "/" + uploadResult.get("format").toString())
                    .setName(uploadResult.get("public_id").toString())
                    .setSize(Long.parseLong(uploadResult.get("bytes").toString()));
        } catch (IOException e) {
            throw new UploadImageException("[cloudinary] can't upload file");
        }

    }

    // Xóa ảnh theo public_id
    public String deleteImage(String publicId) {
        try {
            Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            return result.get("result").toString();
        } catch (IOException e) {
            throw new UploadImageException("[cloudinary] can't delete image");
        }

    }

}
