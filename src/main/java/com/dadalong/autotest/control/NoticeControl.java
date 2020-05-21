package com.dadalong.autotest.control;

import cn.com.dbapp.slab.common.model.dto.CollectionResponse;
import cn.com.dbapp.slab.common.model.dto.SearchRequest;
import cn.com.dbapp.slab.java.commons.models.TypedApiResponse;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dadalong.autotest.model.response.FilterUserNameResponse;
import com.dadalong.autotest.model.response.LogListResponse;
import com.dadalong.autotest.service.INoticeService;
import com.dadalong.autotest.utils.FilterMapUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Api(value="/", description = "通知公告下的全部接口")
@RestController
@RequestMapping("/notice")
public class NoticeControl {

    @Resource
    FilterMapUtils filterMapUtils;

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
        IPage<LogListResponse> pages = iNoticeService.listWithSearch(searchRequest);
        //构造响应体pageInfo+tbody形式
        CollectionResponse response = new CollectionResponse<>(pages, new LogListResponse());
        return TypedApiResponse.ok().message("listWithSearch-success").data(response);
    }

    @ApiOperation(value="获取用户名筛选项",httpMethod = "GET")
    @GetMapping("/filterUserName")
    public FilterUserNameResponse filterUserName() {
        return filterMapUtils.filterUserName();
    }


    @GetMapping("/demo")
    public String demo() {
        System.out.println("进入controller中的demo方法！");
        /*注意：这里返回值有后缀名，如何省略后缀名后面有介绍*/
        return "extent-reports";
    }

}
