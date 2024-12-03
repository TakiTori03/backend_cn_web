package com.hust.Ecommerce.dtos.general;

import lombok.Data;

@Data
public class ImageResponse {
    private Long id;
    private String path;
    private String contentType;
    private String name;
    private Long size;
    private Boolean isThumbnail;
}
