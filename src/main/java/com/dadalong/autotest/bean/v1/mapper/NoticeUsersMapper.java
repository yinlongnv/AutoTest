package com.dadalong.autotest.bean.v1.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dadalong.autotest.bean.v1.pojo.NoticeUsers;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface NoticeUsersMapper extends BaseMapper<NoticeUsers> {
}
