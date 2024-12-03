package com.hust.Ecommerce.dtos.general;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CloudinaryImageDTO {
    private String path;
    private String contentType;
    private String name;
    private Long size;
}
