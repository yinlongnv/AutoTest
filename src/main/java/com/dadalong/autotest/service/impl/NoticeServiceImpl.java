package com.dadalong.autotest.service.impl;

import cn.com.dbapp.slab.common.model.dto.SearchRequest;
import cn.com.dbapp.slab.common.model.dto.SlabPage;
import cn.com.dbapp.slab.java.commons.exceptions.ConflictException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dadalong.autotest.bean.v1.mapper.NoticeMapper;
import com.dadalong.autotest.bean.v1.mapper.OperateLogMapper;
import com.dadalong.autotest.bean.v1.mapper.UserMapper;
import com.dadalong.autotest.bean.v1.pojo.OperateLog;
import com.dadalong.autotest.bean.v1.pojo.User;
import com.dadalong.autotest.bean.v1.wrapper.OperateLogWrapper;
import com.dadalong.autotest.model.response.LogListResponse;
import com.dadalong.autotest.service.INoticeService;
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
public class NoticeServiceImpl implements INoticeService {

    @Resource
    private NoticeMapper noticeMapper;

    @Resource
    InsertOperateLogUtils insertOperateLogUtils;

    @Override
    public IPage<LogListResponse> listWithSearch(SearchRequest searchRequest) {
        return null;
    }
//        try {
//            OperateLogWrapper operateLogWrapper = new OperateLogWrapper();
//            operateLogWrapper.ofListWithSearch(searchRequest).orderByDesc("created_at");
//            List<LogListResponse> logListResponseList = new ArrayList<>();
//            Map<String,Object> map = searchRequest.getSearch();
//
//            SlabPage<OperateLog> operateLogSlabPage = new SlabPage<>(searchRequest);
//            IPage<OperateLog> logResults = operateLogMapper.selectPage(operateLogSlabPage, operateLogWrapper);
//            for (OperateLog record : logResults.getRecords()) {
//                LogListResponse logListResponse = new LogListResponse();
//                BeanUtils.copyProperties(record, logListResponse);
//                User user = userMapper.selectById(record.getUserId());
//                if (user != null && StringUtils.isNotBlank(user.toString())) {
//                    BeanUtils.copyProperties(user, logListResponse, "createdAt");
//                } else {
//                    logListResponse.setUsername("root");
//                    logListResponse.setUserNumber("AH19981006000000");
//                    logListResponse.setRole(1);
//                    logListResponse.setLastIp("127.0.0.1");
//                }
//                logListResponseList.add(logListResponse);
//            }
//            SlabPage<LogListResponse> logListResponseSlabPage = new SlabPage<>(searchRequest);
//            logListResponseSlabPage.setRecords(logListResponseList);
//            logListResponseSlabPage.setTotal(logResults.getTotal());
//
//            Object userId = map.get("userId");
//            //插入操作日志
//            insertOperateLogUtils.insertOperateLog(Integer.parseInt(String.valueOf(userId)), LogContentEnumUtils.LOGLIST, OperatePathEnumUtils.LOGLIST);
//            return logListResponseSlabPage;
//        }catch (Exception e){
//            throw new ConflictException("listWithSearchError");
//        }
//    }

}