package com.dadalong.autotest.service.impl;

import cn.com.dbapp.slab.common.model.dto.SearchRequest;
import cn.com.dbapp.slab.common.model.dto.SlabPage;
import cn.com.dbapp.slab.java.commons.exceptions.ConflictException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dadalong.autotest.bean.v1.mapper.OperateLogMapper;
import com.dadalong.autotest.bean.v1.mapper.UserMapper;
import com.dadalong.autotest.bean.v1.pojo.OperateLog;
import com.dadalong.autotest.bean.v1.pojo.User;
import com.dadalong.autotest.bean.v1.wrapper.OperateLogWrapper;
import com.dadalong.autotest.model.response.LogListResponse;
import com.dadalong.autotest.service.IOperateLogService;
import com.dadalong.autotest.utils.InsertOperateLogUtils;
import com.dadalong.autotest.utils.LogContentEnumUtils;
import com.dadalong.autotest.utils.OperatePathEnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class OperateLogServiceImpl implements IOperateLogService {

    @Resource
    private OperateLogMapper operateLogMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    InsertOperateLogUtils insertOperateLogUtils;

    @Override
    public IPage<LogListResponse> listWithSearch(SearchRequest searchRequest) {
        try {
            OperateLogWrapper operateLogWrapper = new OperateLogWrapper();
            operateLogWrapper.ofListWithSearch(searchRequest).orderByDesc("created_at");
            List<LogListResponse> logListResponseList = new ArrayList<>();
            Map<String,Object> map = searchRequest.getSearch();

            SlabPage<OperateLog> operateLogSlabPage = new SlabPage<>(searchRequest);
            IPage<OperateLog> logResults = operateLogMapper.selectPage(operateLogSlabPage, operateLogWrapper);
            for (OperateLog record : logResults.getRecords()) {
                LogListResponse logListResponse = new LogListResponse();
                BeanUtils.copyProperties(record, logListResponse);
                User user = userMapper.selectById(record.getUserId());
                if (user != null && StringUtils.isNotBlank(user.toString())) {
                    BeanUtils.copyProperties(user, logListResponse, "createdAt");
                } else {
                    logListResponse.setUsername("root");
                    logListResponse.setUserNumber("AH19981006000000");
                    logListResponse.setRole(1);
                    logListResponse.setLastIp("127.0.0.1");
                }
                logListResponseList.add(logListResponse);
            }
            SlabPage<LogListResponse> logListResponseSlabPage = new SlabPage<>(searchRequest);
            logListResponseSlabPage.setRecords(logListResponseList);
            logListResponseSlabPage.setTotal(logResults.getTotal());

            Object userId = map.get("userId");
            //插入操作日志
            insertOperateLogUtils.insertOperateLog((Integer) userId, LogContentEnumUtils.LOGLIST, OperatePathEnumUtils.LOGLIST);
            return logListResponseSlabPage;
        }catch (Exception e){
            throw new ConflictException("listWithSearchError");
        }
    }

    @Override
    public List<LogListResponse> exportAllLogs(SearchRequest searchRequest) {
        Map<String,Object> map = searchRequest.getSearch();
        Object userId = map.get("userId");
        List<LogListResponse> logListResponseList = new ArrayList<>();
        List<OperateLog> operateLogList = operateLogMapper.selectList(null);//查询全部
        for (OperateLog operateLog : operateLogList) {
            LogListResponse logListResponse = new LogListResponse();
            BeanUtils.copyProperties(operateLog, logListResponse);
            User user = userMapper.selectById(operateLog.getUserId());
            if (user != null && StringUtils.isNotBlank(user.toString())) {
                BeanUtils.copyProperties(user, logListResponse, "createdAt");
            } else {
                logListResponse.setUsername("root");
                logListResponse.setUserNumber("AH19981006000000");
                logListResponse.setRole(1);
                logListResponse.setLastIp("127.0.0.1");
            }
            logListResponseList.add(logListResponse);
        }
        //插入操作日志
        insertOperateLogUtils.insertOperateLog((Integer) userId, LogContentEnumUtils.LOGEXPORT, OperatePathEnumUtils.LOGEXPORT);
        return logListResponseList;
    }
}
