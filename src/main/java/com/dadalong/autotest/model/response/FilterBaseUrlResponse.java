package com.dadalong.autotest.model.response;

import com.dadalong.autotest.model.other.BaseUrl;
import lombok.Data;

import java.util.List;

/**
 * 基础域名BaseUrl筛选下拉框
 */
@Data
public class FilterBaseUrlResponse {

    private List<BaseUrl> BaseUrlOptions;

}
