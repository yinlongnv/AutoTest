package com.dadalong.autotest.utils;

import cn.com.dbapp.slab.java.commons.models.TypedApiResponse;
import org.springframework.context.annotation.Configuration;

/**
 * 处理上传文件返回信息
 */
@Configuration
public class HandleUploadMsgUtils {
    public TypedApiResponse handleUploadMsgUtils(String uploadMsg) {
        switch (uploadMsg) {
            case "导入失败，请选择文件":
                return TypedApiResponse.error().message("导入失败，请选择文件");
            case "批量导入失败":
                return TypedApiResponse.error().message("批量导入失败");
            case "批量导入成功":
                return TypedApiResponse.ok().message("批量导入成功");
            default:
                return TypedApiResponse.error().message("loading...");
        }
    }
}
