package com.dadalong.autotest.model.testCase;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * 批量导入用例数据
 */
@Data
public class UploadDTO {
    private MultipartFile file;
    private Integer userId;
    private String projectName;
    private String apiGroup;
    private String apiMerge;//apiName apiPath格式
}
