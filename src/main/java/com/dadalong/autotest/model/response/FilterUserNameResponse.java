package com.dadalong.autotest.model.response;

import com.dadalong.autotest.model.other.UserName;
import lombok.Data;

import java.util.List;

/**
 * 用户名筛选下拉框
 */
@Data
public class FilterUserNameResponse {

    private List<UserName> UserNameOptions;

}
