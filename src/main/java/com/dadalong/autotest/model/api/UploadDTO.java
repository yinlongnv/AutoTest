package com.dadalong.autotest.model.api;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by 78089 on 2020/5/15.
 */
@Data
public class UploadDTO {
    private MultipartFile file;
    private Integer userId;
    private String baseUrl;
}
