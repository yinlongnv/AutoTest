package com.dadalong.autotest.control;

import cn.com.dbapp.slab.common.model.dto.CollectionResponse;
import cn.com.dbapp.slab.common.model.dto.SearchRequest;
import cn.com.dbapp.slab.java.commons.models.TypedApiResponse;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dadalong.autotest.model.notice.DetailDTO;
import com.dadalong.autotest.model.notice.MarkReadDTO;
import com.dadalong.autotest.model.response.FilterUserNameResponse;
import com.dadalong.autotest.model.response.LogListResponse;
import com.dadalong.autotest.model.response.NoticeListResponse;
import com.dadalong.autotest.service.INoticeService;
import com.dadalong.autotest.utils.FilterMapUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Api(value="/", description = "通知公告下的全部接口")
@RestController
@RequestMapping("/notice")
public class NoticeControl {

    private final INoticeService iNoticeService;
    private final HttpServletRequest request;

    public NoticeControl(INoticeService iNoticeService, HttpServletRequest request){
        this.iNoticeService = iNoticeService;
        this.request = request;
    }

    @ApiOperation(value="显示通知公告列表，包含筛选查询",httpMethod = "GET")
    @GetMapping("/listWithSearch")
    public TypedApiResponse listWithSearch(){
        //提取请求中的参数到map中
        SearchRequest searchRequest = new SearchRequest(request);
        //将分页信息和筛选查询到的操作日志列表信息引入pages
        IPage<NoticeListResponse> pages = iNoticeService.listWithSearch(searchRequest);
        //构造响应体pageInfo+tbody形式
        CollectionResponse response = new CollectionResponse<>(pages, new NoticeListResponse());
        return TypedApiResponse.ok().message("listWithSearch-success").data(response);
    }

    @ApiOperation(value="标记已读",httpMethod = "GET")
    @GetMapping("/markRead")
    public TypedApiResponse markRead(MarkReadDTO markReadDTO) {
        if (iNoticeService.markReadAll(markReadDTO)) {
            return TypedApiResponse.ok().message("markRead-success");
        } else {
            return TypedApiResponse.error().message("markRead-failed");
        }
    }

    @ApiOperation(value="查看公告详情",httpMethod = "GET")
    @GetMapping("/detail")
    public TypedApiResponse detail(DetailDTO detailDTO){
        NoticeListResponse noticeListResponse = iNoticeService.detail(detailDTO);
        if (noticeListResponse != null && StringUtils.isNotBlank(noticeListResponse.toString())) {
            return TypedApiResponse.ok().message("detail-success").data(noticeListResponse);
        } else {
            return TypedApiResponse.error().message("detail-failed");
        }
    }

}
