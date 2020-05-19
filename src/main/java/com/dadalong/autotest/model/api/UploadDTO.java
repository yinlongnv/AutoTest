package com.dadalong.autotest.model.api;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * 批量导入接口文档
 */
@Data
public class UploadDTO {
    private MultipartFile file;
    private Integer userId;
    private String baseUrl;
}
