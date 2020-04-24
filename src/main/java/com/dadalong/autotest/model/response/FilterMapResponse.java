package com.dadalong.autotest.model.response;

import lombok.Data;

import java.util.Map;

/**
 * Created by 78089 on 2020/4/24.
 */
@Data
public class FilterMapResponse {

    private Map<Map<String, String>, Map<String, String>> filterMap;

}
