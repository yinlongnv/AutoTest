package com.dadalong.autotest.bean.v1.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dadalong.autotest.bean.v1.pojo.Api;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface ApiMapper extends BaseMapper<Api> {
}
