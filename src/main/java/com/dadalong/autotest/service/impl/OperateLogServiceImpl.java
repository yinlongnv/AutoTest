package com.dadalong.autotest.service.impl;

import cn.com.dbapp.slab.common.model.dto.SearchRequest;
import cn.com.dbapp.slab.common.model.dto.SlabPage;
import cn.com.dbapp.slab.java.commons.exceptions.ConflictException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dadalong.autotest.bean.v1.mapper.OperateLogMapper;
import com.dadalong.autotest.bean.v1.mapper.TestCaseMapper;
import com.dadalong.autotest.bean.v1.mapper.UserMapper;
import com.dadalong.autotest.bean.v1.pojo.OperateLog;
import com.dadalong.autotest.bean.v1.pojo.User;
import com.dadalong.autotest.bean.v1.wrapper.OperateLogWrapper;
import com.dadalong.autotest.bean.v1.wrapper.UserWrapper;
import com.dadalong.autotest.model.response.LogListResponse;
import com.dadalong.autotest.model.response.TestCaseListResponse;
import com.dadalong.autotest.model.user.CreateOrEditUserDTO;
import com.dadalong.autotest.service.IOperateLogService;
import com.dadalong.autotest.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class OperateLogServiceImpl implements IOperateLogService {

    @Resource
    private OperateLogMapper operateLogMapper;
    @Resource
    private UserMapper userMapper;

    /**
     * 设置每页10条记录
     */
    private static final Integer size = 10;

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiServiceImpl.class);
    @Override
    public IPage<LogListResponse> listWithSearch(SearchRequest searchRequest) {
        try {
            OperateLogWrapper wrapper = new OperateLogWrapper();
            wrapper.ofListWithSearch(searchRequest);
            List<LogListResponse> logListResponseList = new ArrayList<>();

            SlabPage<OperateLog> operateLogSlabPage = new SlabPage<>(searchRequest);
            IPage<OperateLog> logResults = operateLogMapper.selectPage(operateLogSlabPage,wrapper);
            for (OperateLog record : logResults.getRecords()) {
                LogListResponse logListResponse = new LogListResponse();
                BeanUtils.copyProperties(record, logListResponse);
                User user = userMapper.selectById(record.getUserId());
                BeanUtils.copyProperties(user, logListResponse);
                logListResponseList.add(logListResponse);
            }
            SlabPage<LogListResponse> logListResponseSlabPage = new SlabPage<>(searchRequest);
            logListResponseSlabPage.setRecords(logListResponseList);
            logListResponseSlabPage.setTotal(logResults.getTotal());
            return logListResponseSlabPage;
        }catch (Exception e){
            LOGGER.error("listWithSearchError",e);
            throw new ConflictException("listWithSearchError");//暂时没啥用
        }
    }

    @Override
    public List<User> getUserList() {
        UserWrapper userWrapper = new UserWrapper();
        userWrapper.getUserList();
       return userMapper.selectList(userWrapper);
    }
}
