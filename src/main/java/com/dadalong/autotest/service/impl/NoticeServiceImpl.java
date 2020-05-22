package com.dadalong.autotest.service.impl;

import cn.com.dbapp.slab.common.model.dto.SearchRequest;
import cn.com.dbapp.slab.common.model.dto.SlabPage;
import cn.com.dbapp.slab.java.commons.exceptions.ConflictException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dadalong.autotest.bean.v1.mapper.*;
import com.dadalong.autotest.bean.v1.pojo.*;
import com.dadalong.autotest.bean.v1.wrapper.NoticeUsersWrapper;
import com.dadalong.autotest.bean.v1.wrapper.NoticeWrapper;
import com.dadalong.autotest.model.notice.DetailDTO;
import com.dadalong.autotest.model.response.NoticeListResponse;
import com.dadalong.autotest.service.INoticeService;
import com.dadalong.autotest.utils.InsertOperateLogUtils;
import com.dadalong.autotest.utils.LogContentEnumUtils;
import com.dadalong.autotest.utils.OperatePathEnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class NoticeServiceImpl implements INoticeService {

    @Resource
    private NoticeMapper noticeMapper;

    @Resource
    private NoticeUsersMapper noticeUsersMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private ApiMapper apiMapper;

    @Resource
    InsertOperateLogUtils insertOperateLogUtils;

    @Override
    public IPage<NoticeListResponse> listWithSearch(SearchRequest searchRequest) {
        try {
            Map<String,Object> map = searchRequest.getSearch();
            int userId = Integer.valueOf(map.get("userId").toString());
            NoticeUsersWrapper noticeUsersWrapper = new NoticeUsersWrapper();
            NoticeWrapper noticeWrapper = new NoticeWrapper();
            List<NoticeUsers> noticeUsersList =  noticeUsersMapper.selectList(noticeUsersWrapper.eq("user_id", userId));
            List<NoticeListResponse> noticeListResponses = new ArrayList<>();
            List<Integer> noticeIds = new ArrayList<>();
            for (NoticeUsers noticeUsers : noticeUsersList) {
                noticeIds.add(noticeUsers.getNoticeId());
            }
            searchRequest.setSearch("noticeIds", noticeIds);
            noticeWrapper.ofListWithSearch(searchRequest).orderByDesc("created_at");
            SlabPage<Notice> noticeSlabPage = new SlabPage<>(searchRequest);
            IPage<Notice> noticeResults = noticeMapper.selectPage(noticeSlabPage, noticeWrapper);
            for (Notice record : noticeResults.getRecords()) {
                NoticeListResponse noticeListResponse = new NoticeListResponse();
                BeanUtils.copyProperties(record, noticeListResponse);
                User user = userMapper.selectById(record.getCreatorId());
                if (user != null && StringUtils.isNotBlank(user.toString())) {
                    noticeListResponse.setUsername(user.getUsername());
                } else {
                    noticeListResponse.setUsername("root");
                }
                noticeListResponses.add(noticeListResponse);
            }
            SlabPage<NoticeListResponse> noticeListResponseSlabPage = new SlabPage<>(searchRequest);
            noticeListResponseSlabPage.setRecords(noticeListResponses);
            noticeListResponseSlabPage.setTotal(noticeResults.getTotal());
            //插入操作日志
            insertOperateLogUtils.insertOperateLog(userId, LogContentEnumUtils.NOTICELIST, OperatePathEnumUtils.NOTICELIST);
            return noticeListResponseSlabPage;
        }catch (Exception e){
            throw new ConflictException("listWithSearchError");
        }
    }

    @Override
    public Boolean markReadAll(Integer userId) {
        NoticeUsersWrapper noticeUsersWrapper = new NoticeUsersWrapper();
        List<NoticeUsers> noticeUsersList = noticeUsersMapper.selectList(noticeUsersWrapper.eq("user_id", userId).ne("is_read","0"));
        if (noticeUsersList != null && StringUtils.isNotBlank(noticeUsersList.toString())) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String date = simpleDateFormat.format(new Date());
            for (NoticeUsers noticeUsers : noticeUsersList) {
                noticeUsers.setIsRead(date);
                noticeUsersMapper.updateById(noticeUsers);
            }
            //插入操作日志
            insertOperateLogUtils.insertOperateLog(userId, LogContentEnumUtils.NOTICEREADALL, OperatePathEnumUtils.NOTICEREADALL);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public NoticeListResponse detail(DetailDTO detailDTO) {
        NoticeUsersWrapper noticeUsersWrapper = new NoticeUsersWrapper();
        NoticeListResponse noticeListResponse = new NoticeListResponse();
        Notice notice = noticeMapper.selectById(detailDTO.getNoticeId());
        BeanUtils.copyProperties(notice, noticeListResponse);
        User user = userMapper.selectById(notice.getCreatorId());
        if (user != null && StringUtils.isNotBlank(user.toString())) {
            noticeListResponse.setUsername(user.getUsername());
        } else {
            noticeListResponse.setUsername("root");
        }
        NoticeUsers noticeUsers = noticeUsersMapper.selectOne(noticeUsersWrapper.eq("notice_id", detailDTO.getNoticeId()).eq("user_id", detailDTO.getUserId()));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = simpleDateFormat.format(new Date());
        noticeUsers.setIsRead(date);
        //插入操作日志
        insertOperateLogUtils.insertOperateLog(detailDTO.getUserId(), LogContentEnumUtils.NOTICEREAD, OperatePathEnumUtils.NOTICEREAD);
        Api api = apiMapper.selectById(notice.getApiId());
        if (api != null && StringUtils.isNotBlank(api.toString())) {
            noticeListResponse.setProjectName(api.getProjectName());
            noticeListResponse.setApiGroup(api.getApiGroup());
            noticeListResponse.setApiName(api.getApiName());
            noticeListResponse.setApiPath(api.getApiPath());
        } else {
            noticeListResponse.setProjectName("演训产品中心");
            noticeListResponse.setApiGroup("产品管理");
            noticeListResponse.setApiName("接口名称");
            noticeListResponse.setApiPath("接口路径");
        }
        //插入操作日志
        insertOperateLogUtils.insertOperateLog(detailDTO.getUserId(), LogContentEnumUtils.NOTICEDETAIL, OperatePathEnumUtils.NOTICEDETAIL);
        return noticeListResponse;
    }

}
