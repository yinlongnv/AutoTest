package com.dadalong.autotest.bean.v1.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dadalong.autotest.bean.v1.pojo.TestCase;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * Created by 78089 on 2020/4/9.
 */
@Mapper
@Component
public interface TestCaseMapper extends BaseMapper<TestCase> {
}
