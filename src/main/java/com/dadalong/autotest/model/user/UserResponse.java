package com.dadalong.autotest.model.user;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dadalong.autotest.bean.v1.pojo.User;
import lombok.Data;

import java.util.List;

@Data
public class UserResponse {
    Page users;
    PageInfo pageInfo;
}
