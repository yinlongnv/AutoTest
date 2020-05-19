package com.dadalong.autotest.utils;

import com.dadalong.autotest.bean.v1.mapper.OperateLogMapper;
import com.dadalong.autotest.bean.v1.pojo.OperateLog;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 插入操作日志工具类
 */
@Configuration
public class InsertOperateLogUtils {

    @Resource
    private OperateLogMapper operateLogMapper;

    public void insertOperateLog(Integer userId, String logContent, String operatePath) {

        OperateLog operateLog = new OperateLog();
        operateLog.setUserId(userId);
        operateLog.setLogContent(logContent);
        operateLog.setOperatePath(operatePath);
        operateLogMapper.insert(operateLog);

    }
}
