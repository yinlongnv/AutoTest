package com.dadalong.autotest.control;


import cn.com.dbapp.slab.common.model.dto.CollectionResponse;
import cn.com.dbapp.slab.common.model.dto.SearchRequest;
import cn.com.dbapp.slab.java.commons.models.TypedApiResponse;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dadalong.autotest.model.response.ApiListResponse;
import com.dadalong.autotest.model.response.ApiNameListResponse;
import com.dadalong.autotest.model.response.FilterMapResponse;
import com.dadalong.autotest.model.response.TestCaseListResponse;
import com.dadalong.autotest.model.testCase.BatchDTO;
import com.dadalong.autotest.model.testCase.CreateOrEditCaseDTO;
import com.dadalong.autotest.model.user.CreateOrEditUserDTO;
import com.dadalong.autotest.service.IApiService;
import com.dadalong.autotest.service.ITestCaseService;
import com.dadalong.autotest.service.IUserService;
import com.dadalong.autotest.utils.FilterMapUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(value="/", description = "用例管理下的全部接口")
@RestController
@RequestMapping("/case")
public class TestCaseControl {

    @Resource
    FilterMapUtils filterMapUtils;

    private final ITestCaseService iTestCaseService;
    private final IApiService iApiService;
    private final HttpServletRequest request;

    public TestCaseControl(ITestCaseService iTestCaseService, IApiService iApiService, HttpServletRequest request){
        this.iTestCaseService = iTestCaseService;
        this.iApiService = iApiService;
        this.request = request;
    }

    @ApiOperation(value="显示用例列表，包含筛选查询",httpMethod = "GET")
    @GetMapping("/listWithSearch")
    public TypedApiResponse listWithSearch(){
        //提取请求中的参数到map中
        SearchRequest searchRequest = new SearchRequest(request);
        //将分页信息和筛选查询到的用例列表信息引入pages
        IPage<TestCaseListResponse> pages = iTestCaseService.listWithSearch(searchRequest);
        //构造响应体pageInfo+tbody形式
        CollectionResponse response = new CollectionResponse<>(pages, new TestCaseListResponse());
        return TypedApiResponse.ok().message("listWithSearch-success").data(response);
    }

    @ApiOperation(value="创建/编辑用例", httpMethod = "POST")
    @PostMapping("/createOrEdit")
    public @ResponseBody TypedApiResponse createOrEditApi(@RequestBody CreateOrEditCaseDTO createOrEditCaseDTO){
        iTestCaseService.createOrEditCase(createOrEditCaseDTO);
        return TypedApiResponse.ok().message("createOrEdit-success");
    }

    @ApiOperation(value="(批量)删除用例",httpMethod = "POST")
    @PostMapping("/delete")
    public @ResponseBody TypedApiResponse delete(@RequestBody BatchDTO batchDTO){
        iTestCaseService.deleteBatch(batchDTO.getCaseIds());
        return TypedApiResponse.ok().message("delete-success");
    }

    @ApiOperation(value="查看用例详情",httpMethod = "GET")
    @GetMapping("/detail")
    public TypedApiResponse detail(Integer id){
        return TypedApiResponse.ok().message("detail-success").data(iTestCaseService.detail(id));
    }

    @ApiOperation(value="获取三级级联",httpMethod = "GET")
    @GetMapping("/filterMap")
    public FilterMapResponse filterMap() {
        return filterMapUtils.filterMap();
    }

}
